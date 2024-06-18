package com.kuhu.xosstech.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.kuhu.xosstech.R
import com.kuhu.xosstech.data.Notification
import com.kuhu.xosstech.databinding.ItemNotificationBinding

class NotificationAdapter(private val notifications: List<Notification>):RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemNotificationBinding = DataBindingUtil.inflate(inflater, R.layout.item_notification, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notification = notifications[position]
        holder.bind(notification)
    }

    override fun getItemCount(): Int {
        return notifications.size
    }

    inner class ViewHolder(private val binding: ItemNotificationBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(notification: Notification) {
            binding.notificationData = notification
            binding.executePendingBindings()
        }
    }
}