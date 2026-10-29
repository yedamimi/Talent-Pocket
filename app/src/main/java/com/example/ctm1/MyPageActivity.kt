package com.example.ctm1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MyPageActivity : BaseActivity() {

    private lateinit var btnSchedule: Button
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    private lateinit var userName: TextView
    private lateinit var userUniv: TextView
    private lateinit var userEmail: TextView
    private lateinit var profileImage: ImageView

    private lateinit var btnLiked: ImageView
    private lateinit var btnLogout: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)

        // 1. 공통 상단 메뉴 설정
        setupTopMenu("profile")

        // 2. 뷰 초기화
        initViews()

        // 3. 탭 레이아웃 & 뷰페이저2 설정
        setupTabLayoutWithViewPager()

        // 4. 클릭 이벤트 설정
        setupClickListeners()

        // 5. 버튼 클릭 이벤트
        btnLogout.setOnClickListener {
            // 자동로그인 정보 제거
            val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
            sharedPref.edit().clear().apply()

            // 로그인 화면 이동
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        btnLiked.setOnClickListener {
            val intent = Intent(this, LikedActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initViews() {
        btnSchedule = findViewById(R.id.btnSchedule)
        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)

        userName = findViewById(R.id.userName)
        userUniv = findViewById(R.id.userUniv)
        userEmail = findViewById(R.id.userEmail)
        profileImage = findViewById(R.id.profileImage)

        btnLiked = findViewById(R.id.btnLiked)
        btnLogout = findViewById(R.id.btnLogout)
    }

    private fun setupTabLayoutWithViewPager() {
        val sellerName = userName.text.toString()
        val pagerAdapter = MyPagePagerAdapter(this, true, sellerName)
        viewPager.adapter = pagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when(position) {
                0 -> "게시글"
                1 -> "포트폴리오"
                else -> "리뷰"
            }
        }.attach()
    }

    private fun setupClickListeners() {
        btnSchedule.setOnClickListener {
            val dialog = ScheduleDialog()
            dialog.show(supportFragmentManager, "schedule")
        }
    }
}