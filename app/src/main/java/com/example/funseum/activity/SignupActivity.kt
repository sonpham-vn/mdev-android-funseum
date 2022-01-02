package com.example.funseum.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import com.example.funseum.activity.MainActivity
import com.example.funseum.R
import com.example.funseum.model.LoginModel
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        val signinModel = LoginModel ()
        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val login = findViewById<Button>(R.id.btnlogin)
        val loading = findViewById<ProgressBar>(R.id.loading)

        // Observe sign in status
        val statusObserver = Observer<String> { newStatus ->
            if (newStatus.equals("Success")) {
                loading.visibility = View.GONE
                val intent = Intent(this, MainActivity::class.java)
                startActivity (intent)
            } else if (newStatus.equals("Error")) {
                loading.visibility = View.GONE
            }
        }
        signinModel.currentStatus.observe(this, statusObserver)


        //Button Signup
        btnsignup.setOnClickListener{
            loading.visibility = View.VISIBLE
            signinModel.signup(this,username.text.toString(), password.text.toString())
        }

    }

}