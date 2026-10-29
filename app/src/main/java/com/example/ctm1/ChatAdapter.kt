package com.example.ctm1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(
    private val chatList: MutableList<ChatMessage>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_MY = 1
        private const val VIEW_TYPE_OTHER = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (chatList[position].isMine) VIEW_TYPE_MY else VIEW_TYPE_OTHER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_MY) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat_my, parent, false)
            MyMessageViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat_other, parent, false)
            OtherMessageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chat = chatList[position]

        if (holder is MyMessageViewHolder) {
            holder.tvMyMessage.text = chat.message
        } else if (holder is OtherMessageViewHolder) {
            holder.tvOtherSender.text = chat.sender
            holder.tvOtherMessage.text = chat.message
        }
    }

    override fun getItemCount(): Int = chatList.size

    fun addMessage(chatMessage: ChatMessage) {
        chatList.add(chatMessage)
        notifyItemInserted(chatList.size - 1)
    }

    class MyMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMyMessage: TextView = itemView.findViewById(R.id.tvMyMessage)
    }

    class OtherMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvOtherSender: TextView = itemView.findViewById(R.id.tvOtherSender)
        val tvOtherMessage: TextView = itemView.findViewById(R.id.tvOtherMessage)
    }
}