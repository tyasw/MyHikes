package com.example.tyasw.myhikes

/* ************************************* */
/* Created by Jianna Zhang on 4/1/2018.  */
/* ************************************* */

/*
AES supports key lengths of 128, 192 and 256 bit. there is 128 bits per block.
One case has to be handled with block modes though: what happens if the last block is not exactly
128 bit? That's where padding comes into play, that is, filling the missing bits of the block up.
The simplest of which just fills the missing bits with zeros. There is practically no security
implication in the choice of padding in AES.
 */
/*
Symmetric Encryption: the key use to encrypt is the same as the key used to decrypt
AES is an example of Asy
 */
// show this page: https://developer.android.com/reference/javax/crypto/Cipher

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.GeneralSecurityException
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

class AdvEncryptionStand @Throws(GeneralSecurityException::class)

internal constructor() {

    private var cipher: Cipher? = null
    private var rand: SecureRandom? = null
    private lateinit var secretKey : SecretKey

    /*
 ciphers are not overridable: it is final
 A cipher must be generated using getInstance function with arguments (Algorithm, Modes,
 and/or Paddings: see page: https://developer.android.com/reference/javax/crypto/Cipher

 AES, AES_128, AES_256, ChaCha, etc...
 */
    /*
    in the init:
    1. determin if the doFinal is going to encrypt or decrypt (the opMode)
    2. the encryp  or decrypt key
    3. the randomness of the key
    4. the SecretKey class implements the Key interface (read more in the Developer page)
    */

    init {
        cipher = Cipher.getInstance(ALGORITHM)
        rand = SecureRandom()
        val keyGen = KeyGenerator.getInstance(ALGORITHM)
        keyGen.init(256)
        secretKey = keyGen.generateKey()
    }

//    fun getAllAliasesFromKeystore() {
//        val ks: KeyStore = KeyStore.getInstance("AndroidKeyStore").apply {
//            load(null)
//        }
//
//        val aliases: Enumeration<String> = ks.aliases()
//    }
//
//    fun getKeyFromKeystore() {
//
//    }
//
//    fun putKeyInKeystore() {
//        val kpg: KeyPairGenerator = KeyPairGenerator.getInstance(
//                KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
//
//        val parameterSpec: KeyGenParameterSpec = KeyGenParameterSpec.Builder(
//                alias,
//                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
//                .run {
//                    setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
//                    build()
//                }
//        kpg.initialize(parameterSpec)
//
//        val kp = kpg.generateKeyPair()
//    }

    fun crypt(opMode: Int, message: String, key: SecretKey): String? {
        return try {
            cipher?.init(opMode, key)
            val messageBytes = message.toByteArray(charset("ISO-8859-1"))
            val encodedBytes = cipher?.doFinal(messageBytes)

            /* ISO (The International Standards Organization) defines the standard character sets
            // for different alphabets/languages.
            // Information technology — 8-bit single-byte coded graphic character sets
            Ref page: https://www.w3schools.com/charsets/ref_html_8859.asp
            */
            String(encodedBytes!!, charset("ISO-8859-1"))
        } catch (e: Exception) {
            null
        }
    }

    fun getKey() : SecretKey {
        return secretKey
    }

    fun getKeyBytes(): ByteArray {
        return secretKey.encoded
    }

    companion object {
        private var ALGORITHM = "AES"
    }
}