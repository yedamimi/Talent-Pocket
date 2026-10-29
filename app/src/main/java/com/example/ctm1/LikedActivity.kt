package com.example.ctm1

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LikedActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TalentAdapter

    // 찜된 재능 목록만 가져오기
    private val likedList: MutableList<Talent>
        get() = TalentRepository.talentList.filter { it.isLiked }.toMutableList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liked)

        setupTopMenu("none")

        recyclerView = findViewById(R.id.likedRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        setupAdapter()
    }

    private fun setupAdapter() {
        // true 전달
        adapter = TalentAdapter(likedList, true)
        recyclerView.adapter = adapter

        // 여기서 삭제 로직 처리 안 함 (Adapter가 처리함)
        adapter.onLikeClick = { _, _ ->
            // 아무 것도 안 해도 됨
        }
    }

    override fun onResume() {
        super.onResume()
        // 찜 상태 반영
        setupAdapter()
    }
}