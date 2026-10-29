package com.example.ctm1

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import java.util.Calendar

class AddPostActivity : BaseActivity() {

    private lateinit var etCategory: EditText
    private lateinit var etDeadline: EditText
    private lateinit var btnSubmit: Button

    private var selectedCategory: String = ""
    private var selectedDeadline: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        setupTopMenu("add")

        etCategory = findViewById(R.id.etAddCategory)
        etDeadline = findViewById(R.id.etDeadline)
        btnSubmit = findViewById(R.id.btnRegisterSubmit)

        // 카테고리 선택
        etCategory.setOnClickListener {
            showCategoryDialog()
        }

        // 모집기간 클릭 → 캘린더 열기
        etDeadline.setOnClickListener {
            showDatePicker()
        }

        btnSubmit.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // 날짜 선택 다이얼로그
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(this, { _, y, m, d ->
            val selectedDate = "${y}-${m + 1}-${d}"
            selectedDeadline = selectedDate
            etDeadline.setText(selectedDate)
        }, year, month, day)

        datePicker.show()
    }

    private fun showCategoryDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_category, null)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val design = dialogView.findViewById<TextView>(R.id.itemDesign)
        val programming = dialogView.findViewById<TextView>(R.id.itemProgramming)
        val language = dialogView.findViewById<TextView>(R.id.itemLanguage)
        val etc = dialogView.findViewById<TextView>(R.id.itemEtc)

        val items = listOf(design, programming, language, etc)

        val clickListener = { category: String, view: TextView ->
            items.forEach { it.isSelected = false }
            view.isSelected = true

            selectedCategory = category
            etCategory.setText(category)

            dialog.dismiss()
        }

        design.setOnClickListener { clickListener("디자인", design) }
        programming.setOnClickListener { clickListener("프로그래밍", programming) }
        language.setOnClickListener { clickListener("어학", language) }
        etc.setOnClickListener { clickListener("기타", etc) }

        dialog.show()
    }
}