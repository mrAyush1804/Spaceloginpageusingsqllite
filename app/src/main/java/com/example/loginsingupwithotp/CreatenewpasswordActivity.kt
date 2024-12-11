package com.example.loginsingupwithotp

import DatabaseHelper
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CreatenewpasswordActivity : AppCompatActivity() {
    private lateinit var EditTextnewpassword:EditText
    private lateinit var confirmpaswor:EditText
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var Buttonrestpassword:Button
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.createnewpassword)

      databaseHelper= DatabaseHelper.getInstance(this)
        inslized()
        Buttonrestpassword.setOnClickListener {
            udpadepassword(EditTextnewpassword,confirmpaswor)
        }




    }
   fun inslized()
    {
        EditTextnewpassword=findViewById(R.id.passwordcreate)
        confirmpaswor=findViewById(R.id.confirmpassword)
        Buttonrestpassword=findViewById(R.id.RESET_PASSWORD)
    }
    fun udpadepassword(editText1: EditText, editText2: EditText) {
        val newPassword = editText1.text.toString()
        val confirmPassword = editText2.text.toString()
        val phoneNumber = intent.getStringExtra("number")


        if (newPassword == confirmPassword) {
            Toast.makeText(this, "PASSWORD UPDATED SUCCESSFULLY", Toast.LENGTH_LONG).show()
            databaseHelper.updatePasswordBasedOnPhone(phoneNumber.toString().trim(),newPassword)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            Toast.makeText(this, "PASSWORD DOESN'T MATCH", Toast.LENGTH_LONG).show()
        }
    }

}