package com.example.tyasw.myhikes

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.Context
import android.content.ContentValues

/**
 * Created by tyasw on 5/29/18.
 */
class MyDBHandler(context: Context, name: String?,
                  factory: SQLiteDatabase.CursorFactory?, version: Int) :
                        SQLiteOpenHelper(context, DATABASE_NAME,
                                factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_HIKES_TABLE)
        db.execSQL(CREATE_SUPPLIES_TABLE)
        db.execSQL(CREATE_SUPPLYTYPES_TABLE)
        db.execSQL(CREATE_CONTACTS_TABLE)
        db.execSQL(CREATE_CONTACTINFO_TABLE)
        db.execSQL(CREATE_ACCOUNTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIKES)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUPPLIES)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUPPLYTYPES)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTINFO)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS)

        onCreate(db)
    }

    // ------------------------ HIKES table methods -------------------------//
    fun addHike(hike: Hike) {
        val values = ContentValues()
        values.put(HIKES_COLUMN_ID, hike.id)
        values.put(HIKES_USER_ID, hike.userId)
        values.put(HIKES_NAME, hike.name)
        values.put(HIKES_LENGTH, hike.length)
        values.put(HIKES_DIFFICULTY, hike.difficulty)

        val db = this.writableDatabase
        db.insert(TABLE_HIKES, null, values)
        db.close()
    }

    fun deleteHike(hikeTitle: String): Boolean {
        var result = false

        val query = "SELECT * FROM $TABLE_HIKES WHERE $HIKES_NAME = \"$hikeTitle\""

        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            // SQL should automatically delete entries with foreign keys referencing this one?
            val id = Integer.parseInt(cursor.getString(0))
            db.delete(TABLE_HIKES, HIKES_COLUMN_ID + " = ?", arrayOf(id.toString()))
            cursor.close()
            result = true
        }

        db.close()
        return result
    }

    fun findHikeById(id: Int): Hike? {
        val query = "SELECT * FROM $TABLE_HIKES WHERE $HIKES_COLUMN_ID = $id"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)

        var hike : Hike? = null

        if (cursor.moveToFirst()) {
            val hikeId = Integer.parseInt(cursor.getString(0))
            val userId = Integer.parseInt(cursor.getString(1))
            val name = cursor.getString(2)
            val length = cursor.getString(3).toDouble()
            val difficulty = cursor.getString(4)

            hike = Hike(hikeId, userId, name, length, difficulty)
            cursor.close()
        }
        db.close()
        return hike
    }

    // Find all hikes that user has created
    fun findAllHikes(userId: Int): List<Hike> {
        val query = "SELECT * FROM $TABLE_HIKES WHERE $HIKES_USER_ID = \"$userId\""

        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)

        var hikes = ArrayList<Hike>()

        while (cursor.moveToNext()) {
            val id = Integer.parseInt(cursor.getString(0))
            //val userId = Integer.parseInt(cursor.getString(1))
            val name = cursor.getString(2)
            val length = cursor.getString(3).toDouble()
            val difficulty = cursor.getString(4)
            val hike = Hike(id, userId, name, length, difficulty)
            hikes.add(hike)
        }
        cursor.close()
        db.close()
        return hikes
    }

    // Update db with changes, might need to update SUPPLIES, SUPPLYTYPES,
    // CONTACTS, AND CONTACTINFO as well with new supplies and contacts
    fun modifyHike(hike: Hike) {
        val hikeId = hike.id
        var userId = hike.userId
        val name = hike.name
        val length = hike.length
        val difficulty = hike.difficulty

        val query = "UPDATE $TABLE_HIKES " +
                "SET $HIKES_NAME = \"$name\", " +
                "$HIKES_LENGTH = \"$length\", " +
                "$HIKES_DIFFICULTY = \"$difficulty\" " +
                "WHERE $HIKES_COLUMN_ID = \"$hikeId\""

        val db = this.writableDatabase
        db.execSQL(query)
        db.close()
    }

    // ------------------------- SUPPLY table methods -------------------------//
    fun addSupply(supply: Supply) {
        val values = ContentValues()
        values.put(SUPPLY_ID, supply.supplyId)
        values.put(HIKE_ID, supply.hikeId)
        values.put(SUPPLY_QUANTITY, supply.quantity)

        val db = this.writableDatabase
        db.insert(TABLE_SUPPLIES, null, values)
        db.close()
    }

    fun deleteSupplyById(id: Int) : Boolean {
        var result = false

        val query = "SELECT * FROM $TABLE_SUPPLIES WHERE $SUPPLY_ID = \"$id\""

        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            // SQL should automatically delete entries with foreign keys referencing this one?
            val supplyTableId = Integer.parseInt(cursor.getString(0))
            db.delete(TABLE_SUPPLYTYPES, SUPPLYTYPES_ID + " = ?", arrayOf(supplyTableId.toString()))
            cursor.close()
            result = true
        }

        db.close()
        return result
    }

    fun modifySupply(supply: Supply) {
        var id = supply.id
        var supplyId = supply.supplyId
        var hikeId = supply.hikeId
        var quantity = supply.quantity

        // Hopefully, SQL will cascade this to other referencing tables
        val query = "UPDATE $TABLE_SUPPLIES " +
                "SET $SUPPLY_ID = \"$supplyId\", " +
                "$HIKE_ID = \"$hikeId\" "
                "WHERE $SUPPLY_COLUMN_ID = \"$id\""

        val db = this.writableDatabase
        db.execSQL(query)
        db.close()
    }

    fun findAllSupplies(hike: Int): List<Supply> {
        val query = "SELECT * FROM $TABLE_HIKES WHERE $HIKE_ID = \"$hike\""

        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)

        var supplies = ArrayList<Supply>()

        while (cursor.moveToNext()) {
            val id = Integer.parseInt(cursor.getString(0))
            val supplyId = Integer.parseInt(cursor.getString(1))
            val hikeId = Integer.parseInt(cursor.getString(2))
            val quantity = cursor.getString(3).toDouble()

            val supply = Supply(id, supplyId, hikeId, quantity)
            supplies.add(supply)
        }
        cursor.close()
        db.close()
        return supplies
    }

    // ----------------------- SUPPLYTYPE table methods -----------------------//
    fun addSupplyType(supplyType: SupplyType) {
        val values = ContentValues()
        values.put(SUPPLYTYPES_ID, supplyType.id)
        values.put(SUPPLYTYPES_NAME, supplyType.name)

        val db = this.writableDatabase
        db.insert(TABLE_SUPPLYTYPES, null, values)
        db.close()
    }

    fun deleteSupplyType(supplyTypeName: String) : Boolean {
        var result = false

        val query = "SELECT * FROM $TABLE_SUPPLYTYPES WHERE $SUPPLYTYPES_NAME = \"$supplyTypeName\""

        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            // SQL should automatically delete entries with foreign keys referencing this one?
            val id = Integer.parseInt(cursor.getString(0))
            db.delete(TABLE_SUPPLYTYPES, SUPPLYTYPES_ID + " = ?", arrayOf(id.toString()))
            cursor.close()
            result = true
        }

        db.close()
        return result
    }

    fun modifySupplyType(supplyType: SupplyType) {
        val id = supplyType.id
        val supplyName = supplyType.name

        // Hopefully, SQL will cascade this to other referencing tables
        val query = "UPDATE $TABLE_SUPPLYTYPES " +
                "SET $SUPPLYTYPES_NAME = \"$supplyName\" " +
                "WHERE $SUPPLYTYPES_ID = \"$id\""

        val db = this.writableDatabase
        db.execSQL(query)
        db.close()
    }

    fun findSupplyType(id: Int): SupplyType? {
        val query = "SELECT * FROM $TABLE_SUPPLYTYPES WHERE $SUPPLYTYPES_ID = $id"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)

        var supplyType : SupplyType? = null

        if (cursor.moveToFirst()) {
            val supplyTypeId = Integer.parseInt(cursor.getString(0))
            val name = cursor.getString(1)

            supplyType = SupplyType(supplyTypeId, name)
            cursor.close()
        }
        db.close()
        return supplyType
    }

    // ----------------------- CONTACTS table methods -----------------------//
    /* Visit this website: http://www.sqlitetutorial.net/sqlite-primary-key/ */
    fun addContact(contact: Contact) {
        val values = ContentValues()
        values.put(CONTACT_ID, contact.contactId)
        values.put(CONTACT_HIKE_ID, contact.hikeId)

        val db = this.writableDatabase
        db.insert(TABLE_CONTACTS, null, values)
        db.close()
    }

    // Remove the relationship between a contact and a hike, get id from
    // CONTACTINFO entry
    fun deleteContact(id: Int) : Boolean {
        var result = false

        val query = "SELECT * FROM $TABLE_CONTACTS WHERE $CONTACT_ID = \"$id\""

        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            // SQL should automatically delete entries with foreign keys referencing this one?
            val contactId = Integer.parseInt(cursor.getString(0))
            db.delete(TABLE_SUPPLYTYPES, SUPPLYTYPES_ID + " = ?", arrayOf(contactId.toString()))
            cursor.close()
            result = true
        }

        db.close()
        return result
    }

    // PROBABLY WON'T USE THIS, NO NEED TO MODIFY ANYTHING, BUT WE'LL
    // DO IT ANYWAY
    // Don't need to modify, delete old entry, and replace with new one
    fun modifyContact(id: Int) {
        val query = "SELECT * FROM $TABLE_CONTACTS WHERE $CONTACT_ID = \"$id\""

        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            val contactId = Integer.parseInt(cursor.getString(0))
            val hikeId = Integer.parseInt(cursor.getString(1))
            val newContact = Contact(contactId, hikeId)

            deleteContact(contactId)
            addContact(newContact)

            cursor.close()
        }
        db.close()
    }

    fun findAllContacts(id: Int): List<Contact> {
        val query = "SELECT * FROM $TABLE_CONTACTS WHERE $CONTACT_HIKE_ID = \"$id\""

        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)

        var contacts = ArrayList<Contact>()

        while (cursor.moveToNext()) {
            val contactId = Integer.parseInt(cursor.getString(0))
            val hikeId = Integer.parseInt(cursor.getString(1))

            val contact = Contact(contactId, hikeId)
            contacts.add(contact)
        }
        cursor.close()
        db.close()
        return contacts
    }

    // ---------------------- CONTACTINFO table methods ---------------------//
    // There are no methods to modify or lookup contacts, so that a
    // malicious user can't look up the contact information of people not
    // part of the current hiking plan

    fun addContactInfo(contactInfo: ContactInfo) {
        val values = ContentValues()
        values.put(CONTACTINFO_ID, contactInfo.id)
        values.put(CONTACT_NAME, contactInfo.name)
        values.put(CONTACT_PHONE, contactInfo.phone)

        val db = this.writableDatabase
        db.insert(TABLE_CONTACTINFO, null, values)
        db.close()
    }

    fun deleteContactInfo(id: Int) : Boolean {
        var result = false

        val query = "SELECT * FROM $TABLE_CONTACTINFO WHERE $CONTACTINFO_ID = \"$id\""

        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            val contactInfoId = Integer.parseInt(cursor.getString(0))
            db.delete(TABLE_CONTACTINFO, CONTACTINFO_ID + " = ?", arrayOf(contactInfoId.toString()))
            cursor.close()
            result = true
        }

        db.close()
        return result
    }

    // ----------------------- ACCOUNTS table methods -----------------------//
    // There are no methods currently to modify or view account information,
    // for obvious security reasons
    fun addAccount(account: Account) {
        val values = ContentValues()
        values.put(ACCOUNT_ID, account.id)
        values.put(USERNAME, account.username)
        values.put(PASSWORD, account.encryptedPassword)

        val db = this.writableDatabase
        db.insert(TABLE_ACCOUNTS, null, values)
        db.close()
    }

    fun deleteAccount(id: Int): Boolean {
        var result = false

        val query = "SELECT * FROM $TABLE_ACCOUNTS WHERE $ACCOUNT_ID = \"$id\""

        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            val accountId = Integer.parseInt(cursor.getString(0))
            db.delete(TABLE_ACCOUNTS, ACCOUNT_ID + " = ?", arrayOf(accountId.toString()))
            cursor.close()
            result = true
        }

        db.close()
        return result
    }

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "bookDB.db"

        // Table names
        val TABLE_HIKES = "hikes"
        val TABLE_SUPPLIES = "supplies"
        val TABLE_SUPPLYTYPES = "supplyTypes"
        val TABLE_CONTACTS = "contacts"
        val TABLE_CONTACTINFO = "contactInfo"
        val TABLE_ACCOUNTS = "accounts"

        // HIKES table columns
        val HIKES_COLUMN_ID = "_id"
        val HIKES_USER_ID = "userId"
        val HIKES_NAME = "name"
        val HIKES_LENGTH = "length"
        val HIKES_DIFFICULTY = "difficulty"

        // SUPPLIES table columns
        val SUPPLY_COLUMN_ID = "_id"
        val SUPPLY_ID = "supplyId"
        val HIKE_ID = "hikeId"
        val SUPPLY_QUANTITY = "quantity"

        // SUPPLYTYPES table columns
        val SUPPLYTYPES_ID = "_id"
        val SUPPLYTYPES_NAME = "name"

        // CONTACTS table columns
        // might need to update to reflect primary key
        val CONTACT_ID = "contactId"
        val CONTACT_HIKE_ID = "hikeId"      // Maybe just reuse HIKE_ID

        // CONTACTINFO table columns
        val CONTACTINFO_ID = "_id"
        val CONTACT_NAME = "name"
        val CONTACT_PHONE = "phone"

        // ACCOUNTS table columns
        val ACCOUNT_ID = "_id"
        val USERNAME = "username"
        val PASSWORD = "encryptedPassword"

        // Create statements
        val CREATE_HIKES_TABLE = ("CREATE TABLE " + TABLE_HIKES
                + "("
                + HIKES_COLUMN_ID + " INTEGER PRIMARY KEY,"
                + HIKES_USER_ID + " INTEGER,"
                + HIKES_NAME + " TEXT,"
                + HIKES_LENGTH + " REAL,"
                + HIKES_DIFFICULTY + " TEXT"
                + ")")

        val CREATE_SUPPLIES_TABLE = ("CREATE TABLE " + TABLE_SUPPLIES
                + "("
                + SUPPLY_ID + " INTEGER,"
                + HIKE_ID + " INTEGER,"
                + SUPPLY_QUANTITY + " REAL,"
                + "PRIMARY KEY ($SUPPLY_ID, $HIKE_ID)"
                + ")")

        val CREATE_SUPPLYTYPES_TABLE = ("CREATE TABLE " + TABLE_SUPPLYTYPES
                + "("
                + SUPPLYTYPES_ID + " INTEGER PRIMARY KEY,"
                + SUPPLYTYPES_NAME + " TEXT"
                + ")")

        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_CONTACTS
                + "("
                + CONTACT_ID + " INTEGER,"
                + CONTACT_HIKE_ID + " INTEGER,"
                + "PRIMARY KEY ($CONTACT_ID, $CONTACT_HIKE_ID)"
                + ")")

        val CREATE_CONTACTINFO_TABLE = ("CREATE TABLE " + TABLE_CONTACTINFO
                + "("
                + CONTACTINFO_ID + " INTEGER PRIMARY KEY,"
                + CONTACT_NAME + " TEXT,"
                + CONTACT_PHONE + " TEXT"
                + ")")

        val CREATE_ACCOUNTS_TABLE = ("CREATE TABLE " + TABLE_ACCOUNTS
                + "("
                + ACCOUNT_ID + " INTEGER PRIMARY KEY,"
                + USERNAME + " TEXT,"
                + PASSWORD + " TEXT"
                + ")")
    }
}