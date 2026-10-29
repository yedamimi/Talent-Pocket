package com.example.ctm1

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import android.os.Bundle

class MyPagePagerAdapter(
    activity: FragmentActivity,
    private val isOwner: Boolean,
    private val sellerName: String
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {

        return when(position) {

            0 -> {
                val fragment = PostsFragment()

                val bundle = Bundle()
                bundle.putString("sellerName", sellerName)
                fragment.arguments = bundle

                fragment
            }

            1 -> {
                val fragment = PortfolioFragment()

                val bundle = Bundle()
                bundle.putBoolean("isOwner", isOwner)
                bundle.putString("sellerName", sellerName)
                fragment.arguments = bundle

                fragment
            }

            else -> ReviewFragment()
        }
    }
}