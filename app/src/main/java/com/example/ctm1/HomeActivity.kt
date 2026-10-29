package com.example.ctm1

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TalentAdapter

    // 전체 리스트 (원본)
    private val talentList: MutableList<Talent>
        get() = TalentRepository.talentList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setupTopMenu("home")

        recyclerView = findViewById(R.id.talentRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 카테고리 뷰 연결
        val categories = listOf(
            findViewById<TextView>(R.id.categoryAll),
            findViewById<TextView>(R.id.categoryDesign),
            findViewById<TextView>(R.id.categoryProgramming),
            findViewById<TextView>(R.id.categoryLanguage),
            findViewById<TextView>(R.id.categoryOther)
        )

        // 카테고리 클릭 이벤트
        categories.forEach { category ->
            category.setOnClickListener {
                selectCategory(category, categories)

                val filtered = when (category.id) {
                    R.id.categoryDesign -> talentList.filter { it.category == "디자인" }
                    R.id.categoryProgramming -> talentList.filter { it.category == "프로그래밍" }
                    R.id.categoryLanguage -> talentList.filter { it.category == "어학" }
                    R.id.categoryOther -> talentList.filter { it.category == "기타" }
                    else -> talentList
                }

                updateAdapter(filtered.toMutableList())
            }
        }

        // 기본 전체 선택
        selectCategory(categories[0], categories)
        updateAdapter(talentList)
    }

    // 카테고리 UI 색상 변경
    private fun selectCategory(selected: TextView, allCategories: List<TextView>) {
        allCategories.forEach {
            it.setBackgroundResource(R.drawable.bg_chip_unselected)
            it.setTextColor(Color.parseColor("#4B5563"))
        }

        selected.setBackgroundResource(R.drawable.bg_chip_selected)
        selected.setTextColor(Color.WHITE)
    }

    // RecyclerView
    private fun updateAdapter(list: MutableList<Talent>) {
        adapter = TalentAdapter(list)
        recyclerView.adapter = adapter

        // 카드 클릭
        adapter.onItemClick = { talent ->
            val intent = Intent(this, TalentDetailActivity::class.java)
            intent.putExtra("category", talent.category)
            intent.putExtra("title", talent.title)
            intent.putExtra("sellerName", talent.writer)
            intent.putExtra("price", talent.price)
            intent.putExtra("description", talent.description)
            intent.putExtra("date", talent.date)
            intent.putExtra("maxCount", talent.maxCount)
            startActivity(intent)
        }

        // 찜 클릭
        adapter.onLikeClick = { talent, position ->
            if (!talent.isLiked) {
                adapter.notifyItemChanged(position)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateAdapter(talentList)
    }
}