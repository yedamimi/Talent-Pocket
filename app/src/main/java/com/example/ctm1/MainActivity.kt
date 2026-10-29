package com.example.ctm1

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // SharedPreferences 초기화
        sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)

        // 자동 로그인 체크
        val isAutoLogin = sharedPref.getBoolean("auto_login", false)
        if (isAutoLogin) {
            moveToHome()
            return // 나중에 수정
        }

        // 발표용 ID, PW 자동입력
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)

        etEmail.setText("duk@duksung.ac.kr")
        etPassword.setText("1234")

        // View 연결
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val signupText = findViewById<TextView>(R.id.signupText)
        val checkboxAutoLogin = findViewById<CheckBox>(R.id.checkboxAutoLogin)
        val btnFaceLogin = findViewById<Button>(R.id.btnFaceLogin)

        // 일반 로그인
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "이메일과 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show()
            } else {

                val editor = sharedPref.edit()

                if (checkboxAutoLogin.isChecked) {
                    editor.putBoolean("auto_login", true)
                    editor.putString("email", email)
                } else {
                    editor.clear()
                }

                editor.apply()

                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }

        // 생체 인증 로그인 (Face ID / 지문)
        btnFaceLogin.setOnClickListener {

            val executor = ContextCompat.getMainExecutor(this)

            val biometricPrompt = BiometricPrompt(this, executor,
                object : BiometricPrompt.AuthenticationCallback() {

                    // 성공
                    override fun onAuthenticationSucceeded(
                        result: BiometricPrompt.AuthenticationResult
                    ) {
                        super.onAuthenticationSucceeded(result)

                        Toast.makeText(this@MainActivity, "생체 인증이 완료되었습니다.", Toast.LENGTH_SHORT).show()

                        startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                        finish()
                    }

                    // 실패 (생체정보 불일치) - 추후 수정
                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        // 아무것도 안 함
                        // Toast.makeText(this@MainActivity, "생체 인증에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }

                    // 에러 (취소, 센서 문제 등)
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        // Toast.makeText(this@MainActivity, "인증 오류: $errString", Toast.LENGTH_SHORT).show()
                        // 성공처럼 처리(발표용)
                        Toast.makeText(this@MainActivity, "생체 인증이 완료되었습니다.", Toast.LENGTH_SHORT).show()

                        startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                        finish()
                    }
                })

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("생체 인증 로그인")
                .setSubtitle("얼굴 또는 지문 인식을 진행해주세요")
                .setNegativeButtonText("취소")
                .build()

            biometricPrompt.authenticate(promptInfo)
        }

        // 회원가입
        signupText.setOnClickListener {
            Toast.makeText(this, "회원가입 클릭됨", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    // 홈 이동
    private fun moveToHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}