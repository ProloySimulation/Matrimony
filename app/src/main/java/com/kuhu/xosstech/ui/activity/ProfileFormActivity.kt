package com.kuhu.xosstech.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import com.kuhu.xosstech.R
import com.kuhu.xosstech.ui.ProfilePagerAdapter
import com.kuhu.xosstech.ui.fragment.BasicProfileFragment
import com.kuhu.xosstech.ui.fragment.ProProfileFragment

class ProfileFormActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var indicatorContainer: LinearLayout
    private lateinit var indicators: Array<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_form)

        viewPager = findViewById(R.id.viewPager)
        indicatorContainer = findViewById(R.id.indicatorContainer)


        val adapter = ProfilePagerAdapter(supportFragmentManager)
        adapter.addFragment(BasicProfileFragment())
        adapter.addFragment(ProProfileFragment())

        viewPager.adapter = adapter

        // Initialize indicators
        indicators = arrayOf(
            findViewById(R.id.indicator1),
            findViewById(R.id.indicator2)
            // Add more indicators as needed
        )

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                updateIndicators(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun updateIndicators(position: Int) {
        for (i in indicators.indices) {
            indicators[i].setBackgroundResource(
                if (i == position) R.drawable.indicator_selected else R.drawable.indicator_unselected
            )
        }
    }
}