package com.example.btl1.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

import androidx.lifecycle.lifecycleScope
import com.example.btl1.R
import com.example.btl1.dal.SQLiteHelper

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private var password: EditText? = null
    private var email: EditText? = null
    private var login : Button? = null
    private var googlelogin : Button? = null
    private var db : SQLiteHelper? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.loginactivity)
        password = findViewById(R.id.password)
        email = findViewById(R.id.email)
        login = findViewById(R.id.login)
        googlelogin = findViewById(R.id.googlelogin)
        db = SQLiteHelper(this)
        login?.setOnClickListener {
            onClick()
        }


    }
    private fun onClick(){
        lifecycleScope.launch(Dispatchers.IO) {
            val email = email?.text.toString()
            val password = password?.text.toString()
            val iduser = db?.checklogin(email, password)


            withContext(Dispatchers.Main){
                if (iduser == 0){
                    Toast.makeText(application, "Login failed", Toast.LENGTH_SHORT).show()
                }
                if(iduser != null){
                    Toast.makeText(application, "${iduser} + ${email} + ${password}", Toast.LENGTH_SHORT).show()
                    val intent = Intent(application, MainActivity::class.java)
                    intent.putExtra("iduser", iduser.toString())
                    startActivity(intent)
                }
            }
        }

    }
}