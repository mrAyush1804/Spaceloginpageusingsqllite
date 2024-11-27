package com.example.loginsingupwithotp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.service.chooser.AdditionalContentContract.Columns

class DatabaseHelper(context: Context):SQLiteOpenHelper(context, DATABASE_NAME,null,
    DATABASE_VERSION) {

    companion object{
        private const val DATABASE_NAME="Userdatabase.db"
        private const val DATABASE_VERSION=1
        private const val Table_name="data"
        private const val COLOUMN_ID="ID"
        private const val COLOUMN_USERNAME="Username"
        private const val COLOUMN_PASSWORD="password"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val createTableQuery=("CREATE TABLE $Table_name("+
                "$COLOUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "$COLOUMN_USERNAME TEXT,"+
                "$COLOUMN_PASSWORD TEXT)")
        p0?.execSQL(createTableQuery)

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        val dropTableQuery ="DROP TABLE IF EXISTS $Table_name"
        p0?.execSQL(dropTableQuery)
        onCreate()

    }
}