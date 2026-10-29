package com.example.ctm1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.AdapterView
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ProfilePageActivity : BaseActivity() {

    private lateinit var btnSchedule: Button
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var tvSellerName: TextView
    private lateinit var tvSellerUniv: TextView
    private lateinit var tvSellerEmail: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)

        setupTopMenu("")
        initViews()

        val sellerName = intent.getStringExtra("sellerName") ?: "익명 판매자"
        tvSellerName.text = sellerName
        tvSellerUniv.text = "덕성여자대학교"
        tvSellerEmail.text = "seller@duksung.ac.kr"

        val isOwner = intent.getBooleanExtra("isOwner", false) // 판매자 여부
        setupTabLayoutWithViewPager(isOwner) // 수정: sellerName 전달

        btnSchedule.setOnClickListener { showTimetableDialog(sellerName) }
    }

    private fun initViews() {
        btnSchedule = findViewById(R.id.btnSchedule)
        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)
        tvSellerName = findViewById(R.id.userName)
        tvSellerUniv = findViewById(R.id.userUniv)
        tvSellerEmail = findViewById(R.id.userEmail)
    }

    private fun setupTabLayoutWithViewPager(isOwner: Boolean) {
        val sellerName = tvSellerName.text.toString()
        viewPager.adapter = ProfilePagerAdapter(this, isOwner, sellerName)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when(position) {
                0 -> "게시글"
                1 -> "포트폴리오"
                else -> "리뷰"
            }
        }.attach()
    }

    // 기존 showTimetableDialog 코드 그대로 유지
    private fun showTimetableDialog(sellerName: String) {
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.dialog_schedule_request, null)
        builder.setView(dialogView)

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val tvTitle = dialogView.findViewById<TextView>(R.id.tvSellerTimetableTitle)
        val gridView = dialogView.findViewById<GridView>(R.id.gridViewTimetable)
        tvTitle.text = "${sellerName} 님의 캠퍼스 시간표"

        val mockSchedule = arrayOf(
            intArrayOf(1, 0, 0, 0, 0),
            intArrayOf(1, 1, 0, 0, 0),
            intArrayOf(0, 0, 0, 1, 1),
            intArrayOf(0, 0, 0, 0, 0)
        )

        val adapter = TimetableAdapter(this, mockSchedule)
        gridView.adapter = adapter

        gridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            if (position == 12 || position == 21) {
                dialog.dismiss()
                AlertDialog.Builder(this)
                    .setTitle("그룹 참여 확인")
                    .setMessage("수요일 3교시 [차미리사관 139] 그룹 활동에 참여하시겠습니까?\n(현재 참여 인원: 4/5)")
                    .setPositiveButton("참여하기") { _, _ ->
                        val sharedPref = getSharedPreferences("TradeStatus", Context.MODE_PRIVATE)
                        sharedPref.edit().putInt("toeic_current_count", 5).apply()

                        val intent = Intent(this, ChatActivity::class.java).apply {
                            putExtra("isGroup", true)
                            putExtra("writer", sellerName)
                            putExtra("title", "토익 스피킹 그룹 스터디")
                            putExtra("category", "어학")
                        }
                        startActivity(intent)
                    }
                    .setNegativeButton("취소", null)
                    .show()
            } else {
                Toast.makeText(this, "활성화된 칸(핑크색)을 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }
}