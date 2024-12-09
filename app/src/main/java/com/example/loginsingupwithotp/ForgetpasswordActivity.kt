package com.example.loginsingupwithotp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

@Suppress("DEPRECATION")
class ForgetpasswordActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black_second)

        setContentView(R.layout.forgetpassword)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val edittextPhoneforgot=findViewById<EditText>(R.id.edittext_phone_forgot)
        val sendotp = findViewById<Button>(R.id.SEND_OTP)
        sendotp.setOnClickListener {
            val flow = "forgot_password"
            val intent = Intent(this, MainActivity3::class.java)
            intent.putExtra("forgetphone",edittextPhoneforgot.text.toString())
            intent.putExtra("flow", flow)
            startActivity(intent)

        }



    }
}
