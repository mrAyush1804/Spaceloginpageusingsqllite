package com.example.loginsingupwithotp

import DatabaseHelper
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
class MainActivity3 : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var Phonnumbertext:TextView
    private lateinit var edit_otp_1:EditText
    private lateinit var edit_otp_2:EditText
    private lateinit var edit_otp_3:EditText
    private lateinit var edit_otp_4:EditText
    private lateinit var Button_verify:Button
    private lateinit var resend:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main3)
        databaseHelper=DatabaseHelper.getInstance(this)

        inSlized()
        setupOtpAutoFocus()

        Phonnumbertext.text = intent.getStringExtra("phoneNumber").orEmpty().toString()


        val otp = generateOTP()
        val expireTime = System.currentTimeMillis() + 300000
        val otpInserted = databaseHelper.insertOTP(Phonnumbertext.text.toString(), otp, expireTime)
        Toast.makeText(this, "OTP Sent: $otp", Toast.LENGTH_SHORT).show()
        Button_verify.setOnClickListener{


            val enteredOTP = edit_otp_1.text.toString() + edit_otp_2.text.toString() +
                    edit_otp_3.text.toString() + edit_otp_4.text.toString()

            val storedOtp =databaseHelper.verifyAndCopyUser(Phonnumbertext.text.toString(),enteredOTP)




            if ( storedOtp) {
                Toast.makeText(this, "OTP Verified Successfully", Toast.LENGTH_SHORT).show()
                val Userveryfy=databaseHelper.verifyAndCopyUser(Phonnumbertext.text.toString(),enteredOTP)

                startActivity(Intent(this, MainActivity4::class.java))
            } else {
                Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show()
            }
        }
        resend.setOnClickListener{
            val otpResent = resendOTP(Phonnumbertext.text.toString())
            Toast.makeText(this, "OTP Resent: $otpResent", Toast.LENGTH_SHORT).show()
        }

    }
    fun inSlized()
    {
        Phonnumbertext=findViewById(R.id.Number_phone)
        edit_otp_1=findViewById(R.id.otp1)
        edit_otp_2=findViewById(R.id.otp2)
        edit_otp_3=findViewById(R.id.otp3)
        edit_otp_4=findViewById(R.id.otp4)
        Button_verify=findViewById(R.id.verify_first)
        resend=findViewById(R.id.click_to_resnd)
    }

    private fun generateOTP(): String {
        val otp = (1000..9999).random().toString()
        return otp
    }

    private fun resendOTP(phoneNumber: String): String {
        val otp = generateOTP()
        val expireTime = System.currentTimeMillis() + 300000
        databaseHelper.insertOTP(phoneNumber, otp, expireTime)
        return otp
    }

    private fun setupOtpAutoFocus() {
        val otpEditTexts = listOf(edit_otp_1, edit_otp_2, edit_otp_3, edit_otp_4)
        for (i in otpEditTexts.indices) {
            otpEditTexts[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1 && i < otpEditTexts.size - 1) {
                        otpEditTexts[i + 1].requestFocus()
                    }
                    else if (s?.length!=1 && i>otpEditTexts.size-1)
                    {
                        otpEditTexts[i-1].requestFocus()
                    }
                    else if (s?.length == 0 && i > 0) {
                        otpEditTexts[i - 1].requestFocus()
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

}