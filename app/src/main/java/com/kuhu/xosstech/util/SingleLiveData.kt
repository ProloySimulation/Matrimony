package com.kuhu.xosstech.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class SingleLiveData<T> : MutableLiveData<T>() {
    private var hasBeenHandled = false

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner) { data ->
            if (!hasBeenHandled) {
                observer.onChanged(data)
                hasBeenHandled = true
            }
        }
    }
}