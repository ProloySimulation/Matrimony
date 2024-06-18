package com.kuhu.xosstech.viewmodel

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.kuhu.xosstech.ui.dialog.CommonDialog

class CustomDialogViewModel(private val message: String, private val listener: CommonDialog.OnOkClickListener) : BaseObservable() {

    @get:Bindable
    var messageText: String = message

    fun onOkButtonClick() {
        listener.onOkClick()
    }

    interface OnOkClickListener {
        fun onOkClick()
    }
}