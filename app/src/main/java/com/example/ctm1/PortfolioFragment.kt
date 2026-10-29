package com.example.ctm1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PortfolioFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var portfolioAdapter: PortfolioAdapter
    private var portfolioList = mutableListOf<Portfolio>()

    private var isOwner: Boolean = true // 외부에서 전달받는 판매자 여부

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isOwner = arguments?.getBoolean("isOwner") ?: true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_portfolio, container, false)

        recyclerView = view.findViewById(R.id.recyclerPortfolio)
        val btnAddPortfolio = view.findViewById<TextView>(R.id.btnAddPortfolio)

        // 1. 초기 데이터가 비어있으면 더미 데이터 생성
        if (portfolioList.isEmpty()) {
            setupDummyData()
        }

        // 2. RecyclerView 및 Adapter 설정
        portfolioAdapter = PortfolioAdapter(portfolioList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = portfolioAdapter

        // 3. + 버튼 표시/숨김
        btnAddPortfolio.visibility = if (isOwner) View.VISIBLE else View.GONE

        // 4. 판매자만 클릭 가능
        if (isOwner) {
            btnAddPortfolio.setOnClickListener { showPortfolioDialog() }
        }

        return view
    }

    private fun setupDummyData() {
        portfolioList.add(Portfolio(1, "Database", "DB 작업 포트폴리오", R.drawable.port1))
        portfolioList.add(Portfolio(2, "웹 개발", "React 포트폴리오", R.drawable.port2))
        portfolioList.add(Portfolio(3, "TOEIC 수업", "TOEIC 성적표", R.drawable.port3))
        portfolioList.add(Portfolio(4, "일본어 수업", "JLPT 성적표", R.drawable.port4))
        portfolioList.add(Portfolio(5, "OPIc 수업", "OPIc 성적표", R.drawable.port5))
    }

    private fun showPortfolioDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_portfolio, null)
        val dialog = AlertDialog.Builder(requireContext()).setView(dialogView).create()
        dialog.window?.setBackgroundDrawableResource(R.drawable.bg_dialog_rounded)

        val etTitle = dialogView.findViewById<EditText>(R.id.etPortfolioTitle)
        val etDesc = dialogView.findViewById<EditText>(R.id.etPortfolioDesc)
        val btnSubmit = dialogView.findViewById<Button>(R.id.btnSubmitPortfolio)

        btnSubmit.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val desc = etDesc.text.toString().trim()

            if (title.isNotEmpty() && desc.isNotEmpty()) {
                val newId = (portfolioList.maxOfOrNull { it.id } ?: 0) + 1
                val newPortfolio = Portfolio(newId, title, desc, R.drawable.port1)
                portfolioList.add(0, newPortfolio)
                portfolioAdapter.notifyItemInserted(0)
                recyclerView.scrollToPosition(0)

                Toast.makeText(requireContext(), "포트폴리오가 등록되었습니다!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "제목과 설명을 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    companion object {
        fun newInstance(isOwner: Boolean): PortfolioFragment {
            val fragment = PortfolioFragment()
            val args = Bundle()
            args.putBoolean("isOwner", isOwner)
            fragment.arguments = args
            return fragment
        }
    }
}