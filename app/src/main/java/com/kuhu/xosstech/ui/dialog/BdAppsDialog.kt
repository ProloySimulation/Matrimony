package com.kuhu.xosstech.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import androidx.databinding.DataBindingUtil
import com.kuhu.xosstech.R
import com.kuhu.xosstech.databinding.LayoutDialogBdappsBinding

class BdAppsDialog(context: Context) : Dialog(context) {

    private lateinit var binding: LayoutDialogBdappsBinding

    var onCancelClick: (() -> Unit)? = null
    var onOkClick: ((String) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_dialog_bdapps, null, false)
        setContentView(binding.root)

        binding.btnBdAppsSubscribeCancel.setOnClickListener {
            onCancelClick?.invoke()
            dismiss()
        }

        binding.btnBdAppsSubscribe.setOnClickListener {
            onOkClick?.invoke(binding.etBdappsMobileNumber.text.toString())
            dismiss()
        }
    }
}