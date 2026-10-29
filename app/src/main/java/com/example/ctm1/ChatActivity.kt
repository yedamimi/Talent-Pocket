package com.example.ctm1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * 채팅 화면 (1:1 및 1:n 그룹 채팅 공용)
 */
class ChatActivity : BaseActivity() {

    private lateinit var tvChatWriter: TextView
    private lateinit var tvChatTitle: TextView
    private lateinit var tvChatCategory: TextView
    private lateinit var recyclerViewChat: RecyclerView
    private lateinit var etMessage: EditText
    private lateinit var btnSend: Button
    private lateinit var btnCompleteTransaction: Button

    private lateinit var chatAdapter: ChatAdapter
    private val chatList = mutableListOf<ChatMessage>()

    // 리뷰 작성 완료 후 콜백
    private val reviewLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val isCompleted = result.data?.getBooleanExtra("review_completed", false) ?: false
            if (isCompleted) {
                val rating = result.data?.getFloatExtra("rating", 0.0f) ?: 0.0f
                sendAutoReviewMessage(rating)

                // [시연용] 거래 완료 상태 저장
                saveTradeStatusFinished()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // 상단 메뉴 설정 (채팅 아이콘 활성화)
        setupTopMenu("chat")

        // Intent 데이터 수신
        val writer = intent.getStringExtra("writer") ?: "익명"
        val title = intent.getStringExtra("title") ?: "제목 없음"
        val category = intent.getStringExtra("category") ?: "카테고리 없음"
        val isGroup = intent.getBooleanExtra("isGroup", false)

        initViews()
        setupHeader(writer, title, category, isGroup) // 최신 인원수 반영 로직 포함
        setupRecyclerView()
        setupClickListeners()
        addInitialMessages(writer, isGroup)

        // [핵심] 그룹 채팅 입장 시 2초간 나타나는 커스텀 팝업 표시
        if (isGroup) {
            showCustomToast(writer)
        }
    }

    /**
     * 2초 후 자동으로 사라지는 커스텀 팝업 알림
     */
    private fun showCustomToast(writer: String) {
        val container = findViewById<FrameLayout>(R.id.custom_toast_container) ?: return
        val inflater = LayoutInflater.from(this)
        val toastView = inflater.inflate(R.layout.layout_custom_toast, container, false)

        // 메시지 텍스트 설정
        val tvToastMessage = toastView.findViewById<TextView>(R.id.tvToastMessage)
        tvToastMessage.text = "${writer} 님과 덕우들이 기다리고 있어요.\n지금 바로 인사를 나눠보세요!"

        // 화면에 추가
        container.addView(toastView)
        container.visibility = View.VISIBLE

        // [효과] 나타날 때 부드럽게
        toastView.alpha = 0f
        toastView.animate().alpha(1f).setDuration(300).start()

        // [핵심] 2초(2000ms) 후에 사라지게 설정
        Handler(Looper.getMainLooper()).postDelayed({
            // 사라질 때 서서히 흐려지는 애니메이션
            toastView.animate()
                .alpha(0f)
                .setDuration(500)
                .withEndAction {
                    container.removeView(toastView)
                    if (container.childCount == 0) {
                        container.visibility = View.GONE
                    }
                }
                .start()
        }, 2000)
    }

    private fun initViews() {
        tvChatWriter = findViewById(R.id.tvChatWriter)
        tvChatTitle = findViewById(R.id.tvChatTitle)
        tvChatCategory = findViewById(R.id.tvChatCategory)
        recyclerViewChat = findViewById(R.id.recyclerViewChat)
        etMessage = findViewById(R.id.etMessage)
        btnSend = findViewById(R.id.btnSend)
        btnCompleteTransaction = findViewById(R.id.btnCompleteTransaction)
    }

    private fun setupClickListeners() {
        btnSend.setOnClickListener {
            val message = etMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                val myMessage = ChatMessage(message, "나", true)
                chatAdapter.addMessage(myMessage)
                recyclerViewChat.scrollToPosition(chatAdapter.itemCount - 1)
                etMessage.text.clear()
            }
        }

        btnCompleteTransaction.setOnClickListener {
            val intent = Intent(this, ReviewActivity::class.java)
            reviewLauncher.launch(intent)
        }
    }

    private fun setupHeader(writer: String, title: String, category: String, isGroup: Boolean) {
        if (isGroup && title.contains("토익")) {
            val sharedPref = getSharedPreferences("TradeStatus", Context.MODE_PRIVATE)
            val currentCount = sharedPref.getInt("toeic_current_count", 3)
            tvChatWriter.text = "👥 $writer 님의 그룹 채팅 ($currentCount/5)"
        } else {
            tvChatWriter.text = if (isGroup) "👥 $writer 님의 그룹 채팅" else "$writer 님과의 채팅"
        }
        tvChatTitle.text = "재능: $title"
        tvChatCategory.text = "카테고리: $category"
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter(chatList)
        recyclerViewChat.layoutManager = LinearLayoutManager(this)
        recyclerViewChat.adapter = chatAdapter
    }

    private fun sendAutoReviewMessage(rating: Float) {
        val reviewMessage = ChatMessage("✨ 리뷰 작성이 완료되었습니다! ($rating 점)", "시스템", false)
        chatAdapter.addMessage(reviewMessage)
        recyclerViewChat.scrollToPosition(chatAdapter.itemCount - 1)

        btnCompleteTransaction.isEnabled = false
        btnCompleteTransaction.text = "리뷰 완료"
        btnCompleteTransaction.alpha = 0.5f
    }

    private fun addInitialMessages(writer: String, isGroup: Boolean) {
        if (isGroup) {
            chatAdapter.addMessage(ChatMessage("새로운 그룹원이 입장했습니다.", "시스템", false))
            chatAdapter.addMessage(ChatMessage("안녕하세요! 그룹 스터디 채팅방입니다.", writer, false))
        } else {
            chatAdapter.addMessage(ChatMessage("안녕하세요! 거래 문의 편하게 남겨주세요.", writer, false))
        }
    }

    private fun saveTradeStatusFinished() {
        val sharedPref = getSharedPreferences("TradeStatus", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("finished_trade_1", true)
            apply()
        }
    }
}