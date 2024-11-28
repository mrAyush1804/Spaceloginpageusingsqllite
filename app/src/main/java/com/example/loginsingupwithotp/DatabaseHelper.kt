import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


class DatabaseHelper private constructor(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "UserDatabase.db"
        private const val DATABASE_VERSION = 1


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
        }
    }

    private val CREATE_USER_TABLE = """
        CREATE TABLE ${UserTable.NAME} (
            ${UserTable.Columns.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${UserTable.Columns.USERNAME} TEXT NOT NULL,
            ${UserTable.Columns.PASSWORD} TEXT NOT NULL,
            ${UserTable.Columns.EMAIL} TEXT NOT NULL UNIQUE
        )
    """.trimIndent()

    private val DROP_USER_TABLE = "DROP TABLE IF EXISTS ${UserTable.NAME}"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_USER_TABLE)
        Log.d("DatabaseHelper", "Database and User table created")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < DATABASE_VERSION) {
            db.execSQL(DROP_USER_TABLE)
            onCreate(db)
        }
    }


    fun insertUser(username: String, email: String, password: String): Long {
        val values = ContentValues().apply {
            put(UserTable.Columns.USERNAME, username)
            put(UserTable.Columns.EMAIL, email)
            put(UserTable.Columns.PASSWORD, password)
        }

        return try {
            writableDatabase.insertOrThrow(UserTable.NAME, null, values)
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error inserting user", e)
            -1L
        }
    }

    fun getUsers(limit: Int = 10, offset: Int = 0): List<User> {
        val userList = mutableListOf<User>()
        val query = "SELECT * FROM ${UserTable.NAME} LIMIT ? OFFSET ?"

        readableDatabase.rawQuery(query, arrayOf(limit.toString(), offset.toString()))
            .use { cursor ->
                if (cursor.moveToFirst()) {
                    do {
                        val id = cursor.getInt(cursor.getColumnIndexOrThrow(UserTable.Columns.ID))
                        val username =
                            cursor.getString(cursor.getColumnIndexOrThrow(UserTable.Columns.USERNAME))
                        val email =
                            cursor.getString(cursor.getColumnIndexOrThrow(UserTable.Columns.EMAIL))
                        val password =
                            cursor.getString(cursor.getColumnIndexOrThrow(UserTable.Columns.PASSWORD))
                        userList.add(User(id, username, email, password))
                    } while (cursor.moveToNext())
                }
            }
        return userList
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

    fun validateUser(username: String, password: String): Boolean {
        val query = """
        SELECT ${UserTable.Columns.PASSWORD} FROM ${UserTable.NAME} 
        WHERE ${UserTable.Columns.USERNAME} = ?
    """
        return try {
            readableDatabase.rawQuery(query, arrayOf(username)).use { cursor ->
                if (cursor.moveToFirst()) {
                    val storedPassword = cursor.getString(
                        cursor.getColumnIndexOrThrow(UserTable.Columns.PASSWORD)
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
