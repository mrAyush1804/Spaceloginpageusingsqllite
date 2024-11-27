package com.example.loginsingupwithotp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

@Suppress("DEPRECATION")
class MainActivity2 : AppCompatActivity() {
    private lateinit var validationManager: ValidationManager
    private  lateinit var EditText_name:EditText
    private lateinit var EditText_passwor:EditText
    private lateinit var EditText_email:EditText
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        window.statusBarColor=ContextCompat.getColor(this,R.color.black)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main2)
        validationManager = ValidationManager(
            emailValidator = EmailValidator(),
            passwordValidator = Password(),
            nameValidator = namevalidator()
        )



        Inslizes()
        val btnsingup = findViewById<Button>(R.id.singupButton_first)
        btnsingup.setOnClickListener {



            val errors = validationManager.validateAll(
                EditText_email.text.toString(),
                EditText_passwor.text.toString(),
                EditText_name.text.toString()
            )


            if (errors.isEmpty()) {
                val intent = Intent(this, MainActivity3::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "please fill the required fields", Toast.LENGTH_SHORT).show()
            }



        }
        applyValidation(EditText_email,validationManager::validateEmail)
        applyValidation(EditText_name,validationManager::validateName)
        applyValidation(EditText_passwor,validationManager::validatePassword)






    }

    private fun applyValidation(editText: EditText, validateFunction: (String) -> String?) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val errorMessage = validateFunction(s.toString())
                editText.error = errorMessage
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    fun Inslizes()
    {
        EditText_email=findViewById(R.id.edittext_email)
        EditText_passwor=findViewById(R.id.edittext_password)
        EditText_name=findViewById(R.id.edittext_name)
    }
}

