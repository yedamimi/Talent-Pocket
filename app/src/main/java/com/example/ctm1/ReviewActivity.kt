package com.example.ctm1

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ReviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        // 1. 뷰 연결
        val ratingBar = findViewById<RatingBar>(R.id.reviewRatingBar)
        val tvScore = findViewById<TextView>(R.id.tvRatingScore)
        val etContent = findViewById<EditText>(R.id.etReviewInput)
        val tvWarning = findViewById<TextView>(R.id.tvCharCountWarning)
        val tvCharCount = findViewById<TextView>(R.id.tvCharCount)
        val btnSubmit = findViewById<Button>(R.id.btnReviewSubmit)
        val btnCancel = findViewById<Button>(R.id.btnCancel)

        // 2. 별점 변경 리스너
        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            tvScore.text = rating.toString()
        }

        // 3. 실시간 글자 수 감지 및 버튼 활성화 로직
        etContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val length = s?.trim()?.length ?: 0
                tvCharCount.text = "$length / 1000"

                if (length >= 10) {
                    tvWarning.text = "✓ 작성 완료"
                    tvWarning.setTextColor(Color.parseColor("#10B981"))
                    btnSubmit.isEnabled = true
                    btnSubmit.alpha = 1.0f
                } else {
                    tvWarning.text = "최소 10자 이상 작성해주세요"
                    tvWarning.setTextColor(Color.parseColor("#B70050"))
                    btnSubmit.isEnabled = false
                    btnSubmit.alpha = 0.5f
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // 4. 취소 버튼 클릭 시 이전 화면(채팅)으로 그냥 돌아감
        btnCancel.setOnClickListener {
            finish()
        }

        // 5. [수정] 리뷰 등록하기 버튼 클릭 시 로직
        btnSubmit.setOnClickListener {
            val score = ratingBar.rating

            if (score == 0.0f) {
                Toast.makeText(this, "별점을 선택해 주세요!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // A. 채팅방(ChatActivity)으로 결과 데이터 전달
            val resultIntent = Intent()
            resultIntent.putExtra("review_completed", true)
            resultIntent.putExtra("rating", score)

            // RESULT_OK를 설정해야 ChatActivity의 reviewLauncher가 작동함
            setResult(RESULT_OK, resultIntent)

            Toast.makeText(this, "리뷰가 성공적으로 등록되었습니다.", Toast.LENGTH_SHORT).show()

            // B. [해결] HomeActivity로 이동하는 로직을 삭제함.
            // finish()만 호출하여 현재 화면을 닫고, 백스택에 살아있는 ChatActivity로 돌아갑니다.
            finish()
        }
    }
}