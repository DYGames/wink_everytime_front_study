package com.example.estudy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okio.IOException

class SigninActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        findViewById<Button>(R.id.signin_button).setOnClickListener {
            val rb = FormBody.Builder()
                .add("id", findViewById<TextView>(R.id.signin_id).text.toString())
                .add("password", findViewById<TextView>(R.id.signin_pw).text.toString())
                .build()
            val rq =
                Request.Builder().addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .url("http://3.143.134.73:3000/signin").post(rb).build()
            val client = OkHttpClient()
            client.newCall(rq).enqueue(object :
                Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("DY", e.message.toString())
                    runOnUiThread {
                        Toast.makeText(this@SigninActivity, "Login Failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    Thread {
                        Log.d("DY", response.code.toString())
                        if (response.code == 200) {
                            val intent = Intent(this@SigninActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            runOnUiThread {
                                Toast.makeText(this@SigninActivity, "Login Failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }.start()
                }
            })
        }

        findViewById<Button>(R.id.signin_signup).setOnClickListener {
            val intent = Intent(this@SigninActivity, SignupActivity::class.java)
            startActivity(intent)
        }
    }
}