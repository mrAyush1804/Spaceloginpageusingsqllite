package com.example.loginsingupwithotp

import DatabaseHelper
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper
    private  lateinit var EditText_name:EditText
    private lateinit var EditText_passwor:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        databaseHelper=DatabaseHelper.getInstance(this)

        val btn=findViewById<Button>(R.id.LoginButton_first)
        Inslizes()
        btn.setOnClickListener {
            val password=EditText_passwor.text.toString()
            val username=EditText_name.text.toString()
            loginuser(username,password)
        }

        val btnsingup=findViewById<TextView>(R.id.text_singup)
        btnsingup.setOnClickListener{
            val intent=Intent(this,MainActivity2::class.java)
            startActivity(intent)
        }
    }

    fun loginuser(username:String,password:String)
    {
       val userexist=databaseHelper.validateUser(username,password)
        if (userexist)
        {
            Toast.makeText(this,"LOGIN SUCCESFULLY",Toast.LENGTH_SHORT).show()
            val intent=Intent(this,MainActivity3::class.java)
            startActivity(intent)
        }else{
            Toast.makeText(this,"LOGIN FAIL",Toast.LENGTH_SHORT).show()
        }
    }

    fun Inslizes()
    {

        EditText_passwor=findViewById(R.id.EditText_first)
        EditText_name=findViewById(R.id.EditText_second)
    }

}

