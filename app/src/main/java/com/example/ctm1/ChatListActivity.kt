package com.example.ctm1

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ChatListActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatListAdapter
    private val chatRoomList = mutableListOf<ChatRoom>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_list)

        // 1. 공통 상단 메뉴 설정
        setupTopMenu("chat")

        // 2. BaseActivity에서 보낸 알림 신호 확인 및 실행
        if (intent.getBooleanExtra("SHOW_CHAT_ALARM", false)) {
            showChatCustomToast()
        }

        // 3. 리사이클러뷰 및 데이터 설정
        recyclerView = findViewById(R.id.recyclerViewChatList)
        setupRecyclerView()
        loadDummyData()
    }

    private fun setupRecyclerView() {
        adapter = ChatListAdapter(chatRoomList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun loadDummyData() {
        // 기존 더미 데이터 유지
        chatRoomList.add(ChatRoom("홍길동", "안녕하세요!", "디자인"))
        chatRoomList.add(ChatRoom("김개발", "문의 드려요!", "프로그래밍"))
        chatRoomList.add(ChatRoom("이영어", "수업 가능할까요?", "어학"))

        adapter.notifyDataSetChanged()
    }
}