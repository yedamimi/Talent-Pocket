package com.example.ctm1

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatListAdapter(private val chatRoomList: List<ChatRoom>) :
    RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvLastMessage: TextView = itemView.findViewById(R.id.tvLastMessage)
        val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_room, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chatRoom = chatRoomList[position]

        holder.tvName.text = chatRoom.name
        holder.tvLastMessage.text = chatRoom.lastMessage
        holder.tvCategory.text = chatRoom.category

        // 🔥 클릭 이벤트 추가
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context

            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("writer", chatRoom.name)
            intent.putExtra("title", chatRoom.lastMessage) // 임시
            intent.putExtra("category", chatRoom.category)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = chatRoomList.size
}