package com.example.loginsingupwithotp

import DatabaseHelper
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

@Suppress("DEPRECATION")
class MainActivity2 : AppCompatActivity() {
   private  lateinit var databaseHelper: DatabaseHelper
     lateinit var recyclerView: RecyclerView
    private lateinit var adapter: recycalerviewadpter
    private lateinit var validationManager: ValidationManager
    private  lateinit var EditText_name:EditText
    private lateinit var EditText_passwor:EditText
    private lateinit var EditText_email:EditText
    private lateinit var EditText_phone:EditText
    private lateinit var textViewlogin: TextView
    var datalist:List<DatabaseHelper.User> = ArrayList<DatabaseHelper.User>()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        window.statusBarColor=ContextCompat.getColor(this,R.color.black)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        databaseHelper=DatabaseHelper.getInstance(this)



        validationManager = ValidationManager(
            emailValidator = EmailValidator(),
            passwordValidator = Password(),
            nameValidator = namevalidator(),
            phoneNumberValidator = PhoneNumberValidator()

        )
        Inslizes()
        val logingo=findViewById<TextView>(R.id.text_login)
        logingo.setOnClickListener{
            startActivity(Intent(this,MainActivity::class.java))
        }


        val btnsingup = findViewById<Button>(R.id.singupButton_first)
        btnsingup.setOnClickListener {
            val name = EditText_name.text.toString()
            val email = EditText_email.text.toString()
            val password = EditText_passwor.text.toString()
            val phoneNumber = EditText_phone.text.toString()

            if (validationManager.validateAll(email, password, name, phoneNumber).isEmpty()) {
                val intent = Intent(this, MainActivity3::class.java)
                intent.putExtra("name", name)
                intent.putExtra("email", email)
                intent.putExtra("password", password)
                intent.putExtra("phoneNumber", phoneNumber)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please fill in valid details.", Toast.LENGTH_SHORT).show()
            }
        }


        val validationMap = mapOf(
            EditText_email to validationManager::validateEmail,
            EditText_name to validationManager::validateName,
            EditText_passwor to validationManager::validatePassword

        )
        validationMap.forEach { (field, validator) ->
            applyValidation(field, validator)
        }




    }

  /*  private fun singupdatabase(name:String ,email:String,password:String,Phone:String)
    {

        val errors = validationManager.validateAll(
            EditText_email.text.toString(),
            EditText_passwor.text.toString(),
            EditText_name.text.toString(),
            EditText_phone.text.toString()
        )


        if (errors.isEmpty()) {
            val name=EditText_name.text.toString()
            val email = EditText_email.text.toString()
            val password = EditText_passwor.text.toString()
            val Phoneno= EditText_phone.text.toString()
            val result = databaseHelper.insertUser(name,email,password)
            val intent = Intent(this, MainActivity3::class.java).apply {
                putExtra("name", name)
                putExtra("email", email)
                putExtra("password", password)
                putExtra("phoneNumber", Phoneno)
            }
            startActivity(intent)
        } else {
            Toast.makeText(this, "Please fill in all the required fields", Toast.LENGTH_SHORT).show()
        }

    }
*/
    private fun applyValidation(editText: EditText, validateFunction: (String) -> String?) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val errorMessage = validateFunction(s.toString())
                editText.error = errorMessage
                if (errorMessage != null) {
                    println("Validation Failed: $errorMessage")
                }
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
        EditText_phone=findViewById(R.id.edittext_phone)

    }
}

