package com.example.ctm1

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // 로그인 화면으로 이동(MainActivity)
        val loginText2 = findViewById<TextView>(R.id.tvBackToLogin)

        loginText2.setOnClickListener {
            finish() // 로그인 화면으로 돌아가기
        }
    }
}