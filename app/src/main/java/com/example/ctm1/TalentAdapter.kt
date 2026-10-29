package com.example.ctm1

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class TalentAdapter(
    private val talentList: MutableList<Talent>,
    private val isLikePage: Boolean = false
) : RecyclerView.Adapter<TalentAdapter.TalentViewHolder>() {

    var onItemClick: ((Talent) -> Unit)? = null
    var onLikeClick: ((Talent, Int) -> Unit)? = null

    class TalentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val category: TextView = view.findViewById(R.id.itemCategory)
        val title: TextView = view.findViewById(R.id.itemTitle)
        val description: TextView = view.findViewById(R.id.itemDesc)
        val price: TextView = view.findViewById(R.id.itemPrice)
        val writer: TextView = view.findViewById(R.id.itemWriter)
        val date: TextView = view.findViewById(R.id.itemDate)
        val participantCount: TextView = view.findViewById(R.id.itemParticipantCount)
        val likeIcon: ImageView = view.findViewById(R.id.likeIcon)
        val likeCount: TextView = view.findViewById(R.id.likeCount)

        val deadline: TextView = view.findViewById(R.id.itemDeadline)
        val overlay: View = view.findViewById(R.id.viewOverlay)
        val closedText: TextView = view.findViewById(R.id.tvClosed)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TalentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_talent, parent, false)
        return TalentViewHolder(view)
    }

    override fun onBindViewHolder(holder: TalentViewHolder, position: Int) {
        val talent = talentList[position]

        // 1. 기본 데이터
        holder.category.text = talent.category
        holder.title.text = talent.title
        holder.description.text = talent.description
        holder.price.text = talent.price
        holder.writer.text = "👤 ${talent.writer}"
        holder.date.text = "⏱ ${talent.date}"
        holder.deadline.text = "📅 ~ ${talent.deadline}"

        // 날짜 비교
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val todayDate = sdf.parse(sdf.format(Date()))
        val deadlineDate = try {
            sdf.parse(talent.deadline)
        } catch (e: Exception) {
            null
        }

        val isClosed = deadlineDate != null && deadlineDate.before(todayDate)

        if (isClosed) {
            holder.overlay.visibility = View.VISIBLE
            holder.closedText.visibility = View.VISIBLE

            // 클릭 완전 차단
            holder.itemView.setOnClickListener(null)
            holder.itemView.isClickable = false
            holder.itemView.alpha = 0.6f

        } else {
            holder.overlay.visibility = View.GONE
            holder.closedText.visibility = View.GONE

            holder.itemView.alpha = 1.0f

            holder.itemView.setOnClickListener {
                onItemClick?.invoke(talent)
            }
        }

        // 2. 참여 인원
        if (talent.maxCount > 1) {
            holder.participantCount.visibility = View.VISIBLE
            holder.participantCount.text =
                "${talent.currentCount}/${talent.maxCount}명 참여 중"
        } else {
            holder.participantCount.visibility = View.GONE
        }

        // 3. 찜 숫자
        holder.likeCount.text = talent.likeCount.toString()

        // 4. 찜 색상
        holder.likeIcon.setColorFilter(
            if (talent.isLiked) 0xFFFF0000.toInt() else 0xFF6B7280.toInt(),
            PorterDuff.Mode.SRC_IN
        )

        // 5. 찜 클릭
        holder.likeIcon.setOnClickListener {
            talent.isLiked = !talent.isLiked

            if (talent.isLiked) {
                talent.likeCount++
            } else {
                if (talent.likeCount > 0) talent.likeCount--

                if (isLikePage) {
                    talentList.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, talentList.size)
                    return@setOnClickListener
                }
            }

            notifyItemChanged(position)
            onLikeClick?.invoke(talent, position)
        }
    }

    override fun getItemCount(): Int = talentList.size
}