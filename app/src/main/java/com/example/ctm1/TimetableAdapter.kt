package com.example.ctm1

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView

class TimetableAdapter(private val context: Context, private val scheduleData: Array<IntArray>) : BaseAdapter() {

    private val days = arrayOf("", "월", "화", "수", "목", "금")
    private val rowCount = 5
    private val colCount = 6
    private val MAIN_PINK = "#B70050" // 모든 글자색 고정

    override fun getCount(): Int = rowCount * colCount
    override fun getItem(position: Int): Any? = null
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_timetable_cell, parent, false)

        val cellLayout = view.findViewById<LinearLayout>(R.id.cellLayout)
        val tvCellText = view.findViewById<TextView>(R.id.tvCellText)

        val row = position / colCount
        val col = position % colCount

        // 1. 기본 스타일 초기화
        cellLayout.setBackgroundColor(Color.WHITE)
        tvCellText.text = ""
        tvCellText.setTextColor(Color.parseColor("#374151"))
        tvCellText.textSize = 11f
        tvCellText.setTypeface(null, Typeface.NORMAL)

        if (row == 0) { // 요일 헤더
            cellLayout.setBackgroundColor(Color.parseColor("#F3F4F6"))
            tvCellText.text = days[col]
            tvCellText.setTypeface(null, Typeface.BOLD)
        } else if (col == 0) { // 교시 헤더
            cellLayout.setBackgroundColor(Color.parseColor("#F3F4F6"))
            tvCellText.text = "${row}교시"
        } else {
            // 2. 인원수별 배경색 그라데이션 (2/5 단계에 #FFF0F5 적용)
            when (position) {
                13 -> { // 월요일 3교시 - 1/5 (가장 연함)
                    setApplyCellStyle(cellLayout, tvCellText, "차미리사홀\n(1/5)", "#FDFBFC")
                }
                26 -> { // 화요일 4교시 - 2/5 (기준 색상)
                    setApplyCellStyle(cellLayout, tvCellText, "차미리사홀\n(2/5)", "#FFF0F5")
                }
                10 -> { // 목요일 1교시 - 3/5 (중간)
                    setApplyCellStyle(cellLayout, tvCellText, "차미리사홀\n(3/5)", "#FFDDEB")
                }
                21 -> { // 수요일 3교시 - 4/5 (가장 진함)
                    setApplyCellStyle(cellLayout, tvCellText, "차미리사관\n(4/5)", "#FFBBD7")
                }
                else -> {
                    val isClass = scheduleData[row - 1][col - 1]
                    if (isClass == 1) {
                        cellLayout.setBackgroundColor(Color.parseColor("#E5E7EB"))
                        tvCellText.text = "수업"
                        tvCellText.setTextColor(Color.parseColor("#9CA3AF"))
                    }
                }
            }
        }
        return view
    }

    /**
     * 배경색 그라데이션 + 메인 컬러 텍스트 설정 함수
     */
    private fun setApplyCellStyle(layout: LinearLayout, textView: TextView, text: String, bgColor: String) {
        layout.setBackgroundColor(Color.parseColor(bgColor))
        textView.text = text
        textView.setTextColor(Color.parseColor(MAIN_PINK)) // 텍스트 컬러 고정
        textView.textSize = 9f
        textView.setTypeface(null, Typeface.BOLD)
        textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
    }
}