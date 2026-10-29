package com.example.ctm1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PortfolioAdapter(private val portfolioList: List<Portfolio>) :
    RecyclerView.Adapter<PortfolioAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgPortfolio: ImageView = view.findViewById(R.id.imgPortfolio)
        val tvTitle: TextView = view.findViewById(R.id.tvPortfolioTitle)
        val tvDesc: TextView = view.findViewById(R.id.tvPortfolioDesc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_portfolio, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = portfolioList[position]
        holder.tvTitle.text = item.title
        holder.tvDesc.text = item.description
        holder.imgPortfolio.setImageResource(item.imageResId)
    }

    override fun getItemCount(): Int = portfolioList.size
}