package com.example.ctm1

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    fun setupTopMenu(selected: String) {
        // 버튼 바인딩
        val btnHome = findViewById<FrameLayout>(R.id.btnGoHome)
        val btnChat = findViewById<FrameLayout>(R.id.btnGoChat)
        val btnAdd = findViewById<FrameLayout>(R.id.btnGoAdd)
        val btnProfile = findViewById<FrameLayout>(R.id.btnGoProfile)
        val btnNotification = findViewById<FrameLayout>(R.id.btnGoNotification)

        // 아이콘 바인딩
        val homeIcon = findViewById<ImageView>(R.id.homeIcon)
        val chatIcon = findViewById<ImageView>(R.id.chatIcon)
        val addIcon = findViewById<ImageView>(R.id.addIcon)
        val profileIcon = findViewById<ImageView>(R.id.profileIcon)
        val notificationIcon = findViewById<ImageView>(R.id.notificationIcon)

        val defaultColor = Color.parseColor("#4B5563")
        val selectedColor = Color.parseColor("#B70050")

        // 아이콘 색상 초기화
        homeIcon?.setColorFilter(defaultColor)
        chatIcon?.setColorFilter(defaultColor)
        addIcon?.setColorFilter(defaultColor)
        profileIcon?.setColorFilter(defaultColor)
        notificationIcon?.setColorFilter(defaultColor)

        // 선택 강조
        when (selected) {
            "home" -> homeIcon?.setColorFilter(selectedColor)
            "chat" -> chatIcon?.setColorFilter(selectedColor)
            "add" -> addIcon?.setColorFilter(selectedColor)
            "profile" -> profileIcon?.setColorFilter(selectedColor)
            "notification" -> notificationIcon?.setColorFilter(selectedColor)
        }

        // 채팅 버튼 클릭 시 처리
        btnChat?.setOnClickListener {
            if (selected != "chat") {
                val intent = Intent(this, ChatListActivity::class.java)
                // 화면 이동 후 알림을 띄우라는 신호를 전달
                intent.putExtra("SHOW_CHAT_ALARM", true)
                startActivity(intent)
            }
        }

        // 나머지 버튼 리스너 (필요시 추가)
        btnHome?.setOnClickListener { if (selected != "home") startActivity(Intent(this, HomeActivity::class.java)) }
        btnAdd?.setOnClickListener { if (selected != "add") startActivity(Intent(this, AddPostActivity::class.java)) }
        btnProfile?.setOnClickListener { if (selected != "profile") startActivity(Intent(this, MyPageActivity::class.java)) }
        btnNotification?.setOnClickListener { if (selected != "notification") startActivity(Intent(this, NotificationActivity::class.java)) }
    }

    // 알림창을 실제로 띄우는 공통 함수
    fun showChatCustomToast() {
        val inflater = LayoutInflater.from(this)
        val layout = inflater.inflate(R.layout.layout_custom_chat_toast, null)

        val dialog = Dialog(this)
        dialog.setContentView(layout)

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val params = dialog.window?.attributes
        params?.let {
            it.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            it.y = 150
            it.width = WindowManager.LayoutParams.WRAP_CONTENT
            it.height = WindowManager.LayoutParams.WRAP_CONTENT
            it.flags = it.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
            it.flags = it.flags or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        }
        dialog.window?.attributes = params

        // 내부 버튼 클릭 시 닫기
        layout.findViewById<View>(R.id.btnClose)?.setOnClickListener { dialog.dismiss() }
        layout.findViewById<View>(R.id.btnConfirm)?.setOnClickListener { dialog.dismiss() }

        dialog.show()

        // 3초 후 자동 종료
        Handler(Looper.getMainLooper()).postDelayed({
            if (dialog.isShowing) dialog.dismiss()
        }, 3000)
    }
}