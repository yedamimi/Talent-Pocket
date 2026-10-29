package com.example.ctm1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class TalentDetailActivity : BaseActivity() {

    private lateinit var btnApply: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_talent_detail)

        setupTopMenu("home")
        initTalentDetail()
    }

    private fun initTalentDetail() {
        // 뷰 연결
        val tvCategory = findViewById<TextView>(R.id.tvDetailCategory)
        val tvTitle = findViewById<TextView>(R.id.tvDetailTitle)
        val tvPrice = findViewById<TextView>(R.id.tvDetailPrice)
        val tvSellerInfo = findViewById<TextView>(R.id.tvSellerInfo)
        val tvDescription = findViewById<TextView>(R.id.tvDetailDescription)
        val tvDate = findViewById<TextView>(R.id.tvDetailDate)
        btnApply = findViewById(R.id.btnApply)
        val layoutSellerProfile = findViewById<LinearLayout>(R.id.layoutSellerProfile)

        // Intent에서 값 받기 (수정)
        val category = intent.getStringExtra("category") ?: ""
        val title = intent.getStringExtra("title") ?: ""
        val price = intent.getStringExtra("price") ?: ""
        val sellerName = intent.getStringExtra("sellerName") ?: ""
        val description = intent.getStringExtra("description") ?: ""
        val date = intent.getStringExtra("date") ?: ""
        val maxCount = intent.getIntExtra("maxCount", 1)

        // 뷰에 데이터 세팅
        tvCategory.text = category
        tvTitle.text = title
        tvPrice.text = price
        tvSellerInfo.text = "판매자: $sellerName"
        tvDescription.text = description
        tvDate.text = "등록일: $date"

        // 판매자 프로필 클릭
        layoutSellerProfile.setOnClickListener {
            val myName = "김덕우" // 나중엔 로그인 값으로 변경

            val isOwner = (sellerName == myName)

            val intent = Intent(this, ProfilePageActivity::class.java)
            intent.putExtra("sellerName", sellerName)
            intent.putExtra("isOwner", isOwner)
            startActivity(intent)
        }

        // 신청하기 버튼 클릭
        btnApply.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("writer", sellerName)
            intent.putExtra("title", title)
            intent.putExtra("category", category)
            intent.putExtra("isGroup", maxCount > 1)
            startActivity(intent)
        }
    }

    companion object {
        fun newIntent(
            context: Context,
            category: String,
            title: String,
            price: String,
            writer: String,
            description: String,
            date: String,
            maxCount: Int
        ): Intent {
            return Intent(context, TalentDetailActivity::class.java).apply {
                putExtra("category", category)
                putExtra("title", title)
                putExtra("price", price)
                putExtra("sellerName", writer)
                putExtra("description", description)
                putExtra("date", date)
                putExtra("maxCount", maxCount)
            }
        }
    }
}