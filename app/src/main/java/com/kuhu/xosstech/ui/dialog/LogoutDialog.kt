package com.kuhu.xosstech.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import androidx.databinding.DataBindingUtil
import com.kuhu.xosstech.R
import com.kuhu.xosstech.databinding.LayoutDialogLogoutBinding

class LogoutDialog(context: Context) : Dialog(context) {

    private lateinit var binding: LayoutDialogLogoutBinding

    var onCancelClick: (() -> Unit)? = null
    var onOkClick: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_dialog_logout, null, false)
        setContentView(binding.root)

        binding.buttonCancel.setOnClickListener {
            onCancelClick?.invoke()
            dismiss()
        }

        binding.buttonOk.setOnClickListener {
            onOkClick?.invoke()
            dismiss()
        }
    }
}