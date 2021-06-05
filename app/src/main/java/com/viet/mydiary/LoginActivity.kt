package com.viet.mydiary

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences("Password", Context.MODE_PRIVATE)

        var data = sharedPreferences.getString("DATA","")
        if(data!!.isEmpty()){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        btnLogin.setOnClickListener {
            if(editTextPassword.text.isEmpty()){
                Toast.makeText(this,"Please enter password", Toast.LENGTH_SHORT).show()
            }
            else if(editTextPassword.text.toString() == data){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }else{
                Toast.makeText(this,"Incorrect password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}