package com.example.ctm1

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PostsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TalentAdapter
    private lateinit var talentList: MutableList<Talent>

    companion object {
        private const val ARG_SELLER_NAME = "sellerName"

        fun newInstance(sellerName: String): PostsFragment {
            val fragment = PostsFragment()
            val bundle = Bundle()
            bundle.putString(ARG_SELLER_NAME, sellerName)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(
            R.layout.fragment_posts,
            container,
            false
        )

        recyclerView = view.findViewById(R.id.recyclerPosts)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // ProfilePagerAdapter에서 전달된 sellerName 사용
        val sellerName = arguments?.getString(ARG_SELLER_NAME) ?: "익명 판매자"

        // 기존 더미 데이터 그대로
        talentList = mutableListOf(
            Talent(
                category = "디자인",
                title = "UI 디자인 도와드립니다",
                description = "피그마 기반 UI 제작",
                price = "10,000원",
                writer = sellerName,
                date = "2024-03-01",
                maxCount = 1
            ),
            Talent(
                category = "프로그래밍",
                title = "안드로이드 앱 개발 도와드립니다",
                description = "Kotlin 기반 앱 제작",
                price = "20,000원",
                writer = sellerName,
                date = "2024-03-02",
                maxCount = 2
            ),
            Talent(
                category = "영상편집",
                title = "유튜브 영상 편집해드립니다",
                description = "프리미어 프로 사용",
                price = "15,000원",
                writer = sellerName,
                date = "2024-03-03",
                maxCount = 1
            )
        )

        // Adapter 연결
        adapter = TalentAdapter(talentList)
        recyclerView.adapter = adapter

        // 클릭 이벤트
        adapter.onItemClick = { talent ->
            val intent = Intent(requireContext(), TalentDetailActivity::class.java)
            intent.putExtra("category", talent.category)
            intent.putExtra("title", talent.title)
            intent.putExtra("sellerName", talent.writer)
            intent.putExtra("price", talent.price)
            intent.putExtra("description", talent.description)
            intent.putExtra("date", talent.date)
            intent.putExtra("maxCount", talent.maxCount)
            startActivity(intent)
        }

        return view
    }
}