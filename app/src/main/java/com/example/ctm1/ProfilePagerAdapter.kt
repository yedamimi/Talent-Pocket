package com.example.ctm1

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ProfilePagerAdapter(
    fa: FragmentActivity,
    private val isOwner: Boolean,
    private val sellerName: String
) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 3 // 게시글, 포트폴리오, 리뷰

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> PostsFragment.newInstance(sellerName) // newInstance로 sellerName 전달
            1 -> PortfolioFragment.newInstance(isOwner)
            2 -> ReviewFragment()
            else -> throw IllegalStateException("Invalid position $position")
        }
    }
}