import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


class DatabaseHelper private constructor(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "UserDatabase.db"
        private const val DATABASE_VERSION = 2


        @Volatile
        private var INSTANCE: DatabaseHelper? = null

        fun getInstance(context: Context): DatabaseHelper {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: DatabaseHelper(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    object UserTable {
        const val NAME = "users"

        object Columns {
            const val ID = "id"
            const val USERNAME = "username"
            const val PASSWORD = "password"
            const val EMAIL = "email"
            const val phonenumber="Phonenumber"
        }
    }

    object OTPTable {
        const val NAME = "OTP"

        object Columns {
            const val ID = "id"
            const val Phonenumber = "Phone_number"
            const val OTP = "otp"
            const val EXPIRETIME = "Expire_TIME"
        }

    }
    object VerifiedUsersTable {
        const val NAME = "VerifiedUsersTable"

        object Columns {
            const val ID = "id"
            const val USERNAME = "username"
            const val EMAIL = "email"
            const val PASSWORD = "password"
            const val phonenumber="Phonenumber"
        }
    }
    private val CREATE_VERIFIED_USERS_TABLE = """
    CREATE TABLE ${VerifiedUsersTable.NAME} (
        ${VerifiedUsersTable.Columns.ID} INTEGER PRIMARY KEY,
        ${VerifiedUsersTable.Columns.USERNAME} TEXT NOT NULL,
        ${VerifiedUsersTable.Columns.EMAIL} TEXT NOT NULL UNIQUE,
        ${VerifiedUsersTable.Columns.PASSWORD} TEXT NOT NULL,
        ${VerifiedUsersTable.Columns.phonenumber} TEXT NOT NULL UNIQUE
    )
""".trimIndent()



    private val CREATE_USER_TABLE = """
        CREATE TABLE ${UserTable.NAME} (
            ${UserTable.Columns.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${UserTable.Columns.USERNAME} TEXT NOT NULL,
            ${UserTable.Columns.PASSWORD} TEXT NOT NULL,
            ${UserTable.Columns.EMAIL} TEXT NOT NULL UNIQUE,
             ${VerifiedUsersTable.Columns.phonenumber} TEXT NOT NULL UNIQUE
        )
    """.trimIndent()

    private val CREATE_OTP_TABLE = """
    CREATE TABLE ${OTPTable.NAME}(
            ${OTPTable.Columns.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${OTPTable.Columns.Phonenumber} TEXT NOT NULL,
            ${OTPTable.Columns.OTP} TEXT NOT NULL,
            ${OTPTable.Columns.EXPIRETIME} INTEGER NOT NULL

    )
  """.trimIndent()

    private val DROP_USER_TABLE = "DROP TABLE IF EXISTS ${UserTable.NAME}"
    private val DROP_OTP_TABLE  =  "DROP TABLE IF EXISTS ${OTPTable.NAME}"


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_USER_TABLE)
        db.execSQL(CREATE_OTP_TABLE)
        db.execSQL(CREATE_VERIFIED_USERS_TABLE)
        Log.d("DatabaseHelper", "Database and User table created")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < DATABASE_VERSION) {
            db.execSQL(DROP_USER_TABLE)
            db.execSQL(DROP_OTP_TABLE)
            onCreate(db)
        }

    }


    fun insertUser(username: String, email: String, password: String,phonenumber:String): Long {
        val values = ContentValues().apply {
            put(UserTable.Columns.USERNAME, username)
            put(UserTable.Columns.EMAIL, email)
            put(UserTable.Columns.PASSWORD, password)
            put(UserTable.Columns.phonenumber,phonenumber)
        }

        return try {
            writableDatabase.insertOrThrow(UserTable.NAME, null, values)
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error inserting user", e)
            -1L
        }
    }

    fun insertOTP(phonenumber:String,OTP:String,Expiretime:Long):Long
    {
        val values=ContentValues().apply {
            put(OTPTable.Columns.OTP,OTP)
            put(OTPTable.Columns.Phonenumber,phonenumber)
            put(OTPTable.Columns.EXPIRETIME,Expiretime)
            validateOTP(phonenumber, OTP)
        }
        return try {
            writableDatabase.replace(OTPTable.NAME, null, values)
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error inserting OTP", e)
            -1L

        }



    }

    fun getUsers(limit: Int = 10, offset: Int = 0): List<User> {
        val userList = mutableListOf<User>()
        val query = "SELECT * FROM ${VerifiedUsersTable.NAME} LIMIT ? OFFSET ?"

        try {
            Log.d("DatabaseHelper", "Query: $query with LIMIT: $limit, OFFSET: $offset")
            readableDatabase.rawQuery(query, arrayOf(limit.toString(), offset.toString()))
                .use { cursor ->
                    Log.d("DatabaseHelper", "Rows fetched: ${cursor.count}")
                    if (cursor.moveToFirst()) {
                        do {
                            val id = cursor.getInt(cursor.getColumnIndexOrThrow(VerifiedUsersTable.Columns.ID))
                            val username = cursor.getString(cursor.getColumnIndexOrThrow(VerifiedUsersTable.Columns.USERNAME))
                            val email = cursor.getString(cursor.getColumnIndexOrThrow(VerifiedUsersTable.Columns.EMAIL))
                            val password = cursor.getString(cursor.getColumnIndexOrThrow(VerifiedUsersTable.Columns.PASSWORD))
                            userList.add(User(id, username, email, password))
                        } while (cursor.moveToNext())
                    }
                }
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error fetching users", e)
        }
        return userList
    }

    fun validateOTP(phoneNumber: String, otp: String): Boolean {
        val query = """
        SELECT ${OTPTable.Columns.EXPIRETIME} 
        FROM ${OTPTable.NAME} 
        WHERE ${OTPTable.Columns.Phonenumber} = ? AND ${OTPTable.Columns.OTP} = ?
    """
        return try {
            readableDatabase.rawQuery(query, arrayOf(phoneNumber, otp)).use { cursor ->
                if (cursor.moveToFirst()) {
                    val expiryTime = cursor.getLong(cursor.getColumnIndexOrThrow(OTPTable.Columns.EXPIRETIME))
                    if (System.currentTimeMillis() <= expiryTime) {
                        deleteOTP(phoneNumber, otp)
                        true
                    } else {
                        false
                    }
                } else {
                    false
                }
            }
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error validating OTP", e)
            false
        }
    }

    private fun deleteOTP(phoneNumber: String, otp: String): Int {
        return writableDatabase.delete(
            OTPTable.NAME,
            "${OTPTable.Columns.Phonenumber} = ? AND ${OTPTable.Columns.OTP} = ?",
            arrayOf(phoneNumber, otp)
        )
    }

    fun verifyAndCopyUser(phoneNumber: String, otp: String): Boolean {
        val db = writableDatabase

        val otpValid = validateOTP(phoneNumber, otp)
        if (!otpValid) {
            Log.e("DatabaseHelper", "Invalid OTP")
            return false
        }

        val query = """
        SELECT * FROM ${UserTable.NAME} 
        WHERE ${UserTable.Columns.phonenumber} = ?
    """
        val user: User? = readableDatabase.rawQuery(query, arrayOf(phoneNumber)).use { cursor ->
            if (cursor.moveToFirst()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(UserTable.Columns.ID))
                val username = cursor.getString(cursor.getColumnIndexOrThrow(UserTable.Columns.USERNAME))
                val email = cursor.getString(cursor.getColumnIndexOrThrow(UserTable.Columns.EMAIL))
                val password = cursor.getString(cursor.getColumnIndexOrThrow(UserTable.Columns.PASSWORD))
                User(id, username, email, password)
            } else {
                null
            }
        }

        if (user == null) {
            Log.e("DatabaseHelper", "User not found for the provided phone number")
            return false
        }

        val values = ContentValues().apply {
            put(VerifiedUsersTable.Columns.ID, user.id)
            put(VerifiedUsersTable.Columns.USERNAME, user.username)
            put(VerifiedUsersTable.Columns.EMAIL, user.email)
            put(VerifiedUsersTable.Columns.PASSWORD, user.password)
            put(VerifiedUsersTable.Columns.phonenumber, phoneNumber)
        }

        return try {
            db.beginTransaction()

            db.insertOrThrow(VerifiedUsersTable.NAME, null, values)
            db.delete(UserTable.NAME, "${UserTable.Columns.ID} = ?", arrayOf(user.id.toString()))

            db.setTransactionSuccessful()
            Log.d("DatabaseHelper", "User verified and copied successfully")
            true
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error copying verified user", e)
            false
        } finally {
            db.endTransaction()
        }
    }


    fun updateUser(id: Int, username: String, email: String, password: String): Int {
        val values = ContentValues().apply {
            put(UserTable.Columns.USERNAME, username)
            put(UserTable.Columns.EMAIL, email)
            put(UserTable.Columns.PASSWORD, password)
        }

        return try {
            writableDatabase.update(
                UserTable.NAME, values,
                "${UserTable.Columns.ID} = ?",
                arrayOf(id.toString())
            )
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error updating user", e)
            -1
        }
    }

    fun deleteUser(id: Int): Int {
        return writableDatabase.delete(
            UserTable.NAME,
            "${UserTable.Columns.ID} = ?",
            arrayOf(id.toString())
        )
    }







    fun varifyvalidateUser(username: String, password: String): Boolean {
        val query = """
        SELECT ${VerifiedUsersTable.Columns.PASSWORD} FROM ${VerifiedUsersTable.NAME} 
        WHERE ${VerifiedUsersTable.Columns.USERNAME} = ?
    """
        return try {
            readableDatabase.rawQuery(query, arrayOf(username)).use { cursor ->
                if (cursor.moveToFirst()) {
                    val storedPassword = cursor.getString(
                        cursor.getColumnIndexOrThrow(VerifiedUsersTable.Columns.PASSWORD)
                    )


                    return storedPassword == password
                }
                false
            }
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error validating user", e)
            false
        }
    }

    fun closeDatabase() {
        writableDatabase.close()
    }

    data class User(
        val id: Int,
        val username: String,
        val email: String,
        val password: String
    )


}
