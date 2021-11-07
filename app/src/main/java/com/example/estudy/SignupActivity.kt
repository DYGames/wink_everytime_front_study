package com.example.estudy

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okio.IOException

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        findViewById<Button>(R.id.signup_button).setOnClickListener {
            val rb = FormBody.Builder()
                .add("id", findViewById<TextView>(R.id.signup_id).text.toString())
                .add("password", findViewById<TextView>(R.id.signup_pw).text.toString())
                .build()
            val rq = Request.Builder().addHeader("Content-Type", "application/x-www-form-urlencoded").url("http://3.143.134.73:3000/signup").post(rb).build()
            val client = OkHttpClient()
            client.newCall(rq).enqueue(object :
                Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("DY", e.message.toString())
                    Toast.makeText(this@SignupActivity, "Upload Failed", Toast.LENGTH_SHORT)
                }

                override fun onResponse(call: Call, response: Response) {
                    Thread {
                        finish()
                    }.start()
                }
            })
        }
    }
}