package com.example.loginsingupwithotp

import DatabaseHelper
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity4 : AppCompatActivity() {
    private  lateinit var databaseHelper: DatabaseHelper
    lateinit var recyclerView: RecyclerView
    private lateinit var adapter: recycalerviewadpter
    var datalist:List<DatabaseHelper.User> = ArrayList<DatabaseHelper.User>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main4)
        databaseHelper=DatabaseHelper.getInstance(this)
        inslized()
        fetchlist()
    }

    private fun fetchlist()
    {
        datalist=databaseHelper!!.getUsers(50,5)
        adapter=recycalerviewadpter(datalist,this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter=adapter

    }
    fun inslized()
    {
        recyclerView=findViewById(R.id.recycleviewfirst)
    }
}