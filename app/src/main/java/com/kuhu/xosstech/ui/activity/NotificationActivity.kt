package com.kuhu.xosstech.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuhu.xosstech.R
import com.kuhu.xosstech.data.Notification
import com.kuhu.xosstech.network.ApiConfig
import com.kuhu.xosstech.ui.adapter.NotificationAdapter
import com.kuhu.xosstech.util.SharedPreferencesManager
import com.kuhu.xosstech.viewmodel.NotificationViewModel
import kotlinx.android.synthetic.main.activity_notification.*

class NotificationActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferencesManager

    private val viewModel: NotificationViewModel by lazy {
        NotificationViewModel(ApiConfig.BASE_URL)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        sharedPreferences = SharedPreferencesManager(this)
        recyclerNotification.layoutManager = LinearLayoutManager(this)

        viewModel.notificationList.observe(this, Observer { notifications->
            updateUI(notifications)
        })

        viewModel.notificationList("Bearer "+sharedPreferences.getLoginToken().toString())
    }

    private fun updateUI(notifications: List<Notification>) {
        val adapter = NotificationAdapter(notifications)
        recyclerNotification.adapter = adapter
    }
}