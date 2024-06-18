package com.kuhu.xosstech.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.kuhu.xosstech.R
import com.kuhu.xosstech.databinding.DialogCommonBinding
import com.kuhu.xosstech.viewmodel.CustomDialogViewModel

class CommonDialog(context: Context, private val message: String,private val onOkClickListener: OnOkClickListener? = null) : Dialog(context) {

    private lateinit var binding: DialogCommonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_common, null, false)
        setContentView(binding.root)

        binding.viewModel = CustomDialogViewModel(message, object : OnOkClickListener {
            override fun onOkClick() {
                onOkClickListener?.onOkClick()
                dismiss()
            }
        })
    }

    interface OnOkClickListener {
        fun onOkClick()
    }
}