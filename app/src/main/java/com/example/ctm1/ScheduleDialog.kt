package com.example.ctm1

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class ScheduleDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_schedule, null)

        // XML에 있는 실제 ID 사용
        val etImageUrl =
            view.findViewById<EditText>(R.id.etImageUrl)

        val btnSubmit =
            view.findViewById<Button>(R.id.btnSubmitSchedule)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .create()

        btnSubmit.setOnClickListener {

            val imageUrl = etImageUrl.text.toString()

            if (imageUrl.isNotEmpty()) {

                dialog.dismiss()

            }

        }

        return dialog
    }
}