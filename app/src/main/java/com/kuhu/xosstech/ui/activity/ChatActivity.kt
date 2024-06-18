package com.kuhu.xosstech.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.kuhu.xosstech.R
import com.kuhu.xosstech.ui.fragment.AcceptFragment
import com.kuhu.xosstech.ui.fragment.RequestFragment
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val fragments = listOf(AcceptFragment(), RequestFragment())

        viewPagerRequest.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = fragments.size

            override fun createFragment(position: Int): Fragment = fragments[position]
        }

        TabLayoutMediator(tabsRequest, viewPagerRequest) { tab, position ->
            when (position) {
                0 -> tab.text = "Accepted"
                1 -> tab.text = "New Request"
            }
        }.attach()
    }
}