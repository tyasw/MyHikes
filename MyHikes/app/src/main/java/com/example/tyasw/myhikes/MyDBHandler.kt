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
        db.execSQL(CREATE_CONTACTS_TABLE)
        db.execSQL(CREATE_ACCOUNTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIKES)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUPPLIES)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS)

        onCreate(db)
    }

    // ------------------------ HIKES table methods -------------------------//
    fun addHike(hike: Hike) {
        val values = ContentValues()
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

    fun deleteAllHikes(userId: Int) {
        val query = "SELECT * FROM $TABLE_HIKES WHERE $HIKES_USER_ID = $userId"

        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val id = Integer.parseInt(cursor.getString(0))
            db.delete(TABLE_HIKES, HIKES_COLUMN_ID + " = ?", arrayOf(id.toString()))
        }
        cursor.close()
        db.close()
    }

    fun findHike(userId: Int, hikeName: String): Hike? {
        val query = "SELECT * FROM $TABLE_HIKES WHERE $HIKES_USER_ID = $userId " +
                "AND $HIKES_NAME = $hikeName"

        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)

        var hike : Hike? = null

        if (cursor.moveToFirst()) {
            val hikeId = Integer.parseInt(cursor.getString(0))
            val user = Integer.parseInt(cursor.getString(1))
            val name = cursor.getString(2)
            val length = cursor.getString(3).toDouble()
            val difficulty = cursor.getString(4)
            val latitude = cursor.getDouble(5)
            val longitude = cursor.getDouble(6)

            hike = Hike(hikeId, user, name, length, difficulty, latitude, longitude)
            cursor.close()
        }
        db.close()
        return hike
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
            val latitude = cursor.getDouble(5)
            val longitude = cursor.getDouble(6)

            hike = Hike(hikeId, userId, name, length, difficulty, latitude, longitude)
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
            val latitude = cursor.getDouble(5)
            val longitude = cursor.getDouble(6)

            val hike = Hike(id, userId, name, length, difficulty, latitude, longitude)
            hikes.add(hike)
        }
        cursor.close()
        db.close()
        return hikes
    }

    // Update db with changes, might need to update SUPPLIES and CONTACTS,
    // as well with new supplies and contacts
    fun modifyHike(hike: Hike) {
        val hikeId = hike.id
        var userId = hike.userId
        val name = hike.name
        val length = hike.length
        val difficulty = hike.difficulty
        val latitude = hike.latitude
        val longitude = hike.longitude

        val query = "UPDATE $TABLE_HIKES " +
                "SET $HIKES_NAME = \"$name\", " +
                "$HIKES_LENGTH = \"$length\", " +
                "$HIKES_DIFFICULTY = \"$difficulty\", " +
                "$HIKES_LATITUDE = \"$latitude\", " +
                "$HIKES_LONGITUDE = \"$longitude\" "
                "WHERE $HIKES_COLUMN_ID = \"$hikeId\""

        val db = this.writableDatabase
        db.execSQL(query)
        db.close()
    }

    // ------------------------- SUPPLY table methods -------------------------//
    fun addSupply(supply: Supply) {
        val values = ContentValues()
        values.put(HIKE_ID, supply.hikeId)
        values.put(SUPPLY_NAME, supply.name)
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
            val supplyId = Integer.parseInt(cursor.getString(0))
            db.delete(TABLE_SUPPLIES, SUPPLY_ID + " = ?", arrayOf(supplyId.toString()))
            cursor.close()
            result = true
        }

        db.close()
        return result
    }

    fun modifySupply(supply: Supply) {
        val id = supply.id
        val hikeId = supply.hikeId
        val name = supply.name
        val quantity = supply.quantity

        val query = "UPDATE $TABLE_SUPPLIES " +
                "SET $HIKE_ID = \"$hikeId\", " +
                "$SUPPLY_NAME = \"$name\", " +
                "$SUPPLY_QUANTITY = \"$quantity\" "
                "WHERE $SUPPLY_ID = \"$id\""

        val db = this.writableDatabase
        db.execSQL(query)
        db.close()
    }

    fun findAllSupplies(hike: Int): ArrayList<Supply> {
        val query = "SELECT * FROM $TABLE_SUPPLIES WHERE $HIKE_ID = \"$hike\""

        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)

        var supplies = ArrayList<Supply>()

        while (cursor.moveToNext()) {
            val id = Integer.parseInt(cursor.getString(0))
            val hikeId = Integer.parseInt(cursor.getString(1))
            val name = cursor.getString(2)
            val quantity = cursor.getString(3).toDouble()

            val supply = Supply(id, hikeId, name, quantity)
            supplies.add(supply)
        }
        cursor.close()
        db.close()
        return supplies
    }

    // ----------------------- CONTACTS table methods -----------------------//
    /* Visit this website: http://www.sqlitetutorial.net/sqlite-primary-key/ */
    fun addContact(contact: Contact) {
        val values = ContentValues()
        values.put(CONTACT_ID, contact.contactId)
        values.put(CONTACT_HIKE_ID, contact.hikeId)
        values.put(CONTACT_NAME, contact.name)
        values.put(CONTACT_PHONE, contact.phone)

        val db = this.writableDatabase
        db.insert(TABLE_CONTACTS, null, values)
        db.close()
    }

    fun deleteContact(id: Int) : Boolean {
        var result = false

        val query = "SELECT * FROM $TABLE_CONTACTS WHERE $CONTACT_ID = \"$id\""

        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            // SQL should automatically delete entries with foreign keys referencing this one?
            val contactId = Integer.parseInt(cursor.getString(0))
            db.delete(TABLE_CONTACTS, CONTACT_ID + " = ?", arrayOf(contactId.toString()))
            cursor.close()
            result = true
        }

        db.close()
        return result
    }


    // Will not be used, user can edit contacts using built-in Android apps
//    fun modifyContact(contact: Contact) {
//        val contactId = contact.contactId
//        val hikeId = contact.hikeId
//        val name = contact.name
//        val phone = contact.phone
//
//        val query = "UPDATE $TABLE_CONTACTS " +
//                "SET $CONTACT_HIKE_ID = \"$hikeId\", " +
//                "$CONTACT_NAME = \"$name\", " +
//                "$CONTACT_PHONE = \"$phone\" " +
//                "WHERE $CONTACT_ID = \"$contactId\""
//
//        val db = this.writableDatabase
//        db.execSQL(query)
//        db.close()
//    }

    fun findAllContacts(id: Int): ArrayList<Contact> {
        val query = "SELECT * FROM $TABLE_CONTACTS WHERE $CONTACT_HIKE_ID = \"$id\""

        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)

        var contacts = ArrayList<Contact>()

        while (cursor.moveToNext()) {
            val contactId = Integer.parseInt(cursor.getString(0))
            val hikeId = Integer.parseInt(cursor.getString(1))
            val name = cursor.getString(2)
            val phone = cursor.getString(3)

            val contact = Contact(contactId, hikeId, name, phone)
            contacts.add(contact)
        }
        cursor.close()
        db.close()
        return contacts
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
        private val DATABASE_NAME = "hikesDB.db"

        // Table names
        val TABLE_HIKES = "hikes"
        val TABLE_SUPPLIES = "supplies"
        val TABLE_CONTACTS = "contacts"
        val TABLE_ACCOUNTS = "accounts"

        // HIKES table columns
        val HIKES_COLUMN_ID = "_id"
        val HIKES_USER_ID = "userId"
        val HIKES_NAME = "name"
        val HIKES_LENGTH = "length"
        val HIKES_DIFFICULTY = "difficulty"
        val HIKES_LATITUDE = "latitude"
        val HIKES_LONGITUDE = "longitude"

        // SUPPLIES table columns
        val SUPPLY_ID = "_id"
        val HIKE_ID = "hikeId"
        val SUPPLY_NAME = "name"
        val SUPPLY_QUANTITY = "quantity"

        // CONTACTS table columns
        // might need to update to reflect primary key
        val CONTACT_ID = "_id"
        val CONTACT_HIKE_ID = "hikeId"      // Maybe just reuse HIKE_ID
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
                + HIKES_DIFFICULTY + " TEXT,"
                + HIKES_LATITUDE + " REAL,"
                + HIKES_LONGITUDE + " REAL"
                + ")")

        val CREATE_SUPPLIES_TABLE = ("CREATE TABLE " + TABLE_SUPPLIES
                + "("
                + SUPPLY_ID + " INTEGER PRIMARY KEY,"
                + HIKE_ID + " INTEGER,"
                + HIKES_NAME + " TEXT,"
                + SUPPLY_QUANTITY + " REAL"
                + ")")

        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_CONTACTS
                + "("
                + CONTACT_ID + " INTEGER PRIMARY KEY,"
                + CONTACT_HIKE_ID + " INTEGER,"
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