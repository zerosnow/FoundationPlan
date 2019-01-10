package com.example.archerlei.foundationplan.main

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.archerlei.foundationplan.R
import com.example.archerlei.foundationplan.main.presenter.FoundationPlanPresenter
import java.lang.Exception

/**
 * @description 新增新基金的弹框
 * Created by archerlei on 2018/12/29
 */
class AddMoreFoundationDialog(context: Context, private val presenter: FoundationPlanPresenter) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_more_foundation_dialog)
        setTitle(R.string.please_input_foundation_info)
        findViewById<Button>(R.id.add_confirm).setOnClickListener {
            try {
                val code = findViewById<EditText>(R.id.et_foundation_code).text.toString()
                val baseLine = findViewById<EditText>(R.id.et_base_line).text.toString().toFloat()

                if (code.length < 6) {
                    Toast.makeText(context, "基金代码长度必须为6位", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                presenter.addData(code, baseLine)
                dismiss()
            } catch (e: Exception) {
                return@setOnClickListener
            }
        }
    }
}