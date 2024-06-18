package com.kuhu.xosstech.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.ViewModelProvider
import com.kuhu.xosstech.R
import com.kuhu.xosstech.viewmodel.StoryBoardViewModel

class StoryBoardActivity : AppCompatActivity() {

    private lateinit var viewModel:StoryBoardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story_board)

        viewModel = ViewModelProvider(this).get(StoryBoardViewModel::class.java)

        viewModel.currentPage.observe(this, { currentPage ->
            updateUI(currentPage)
        })

        findViewById<AppCompatTextView>(R.id.nextTextView).setOnClickListener {
            viewModel.nextPage()
        }

        findViewById<AppCompatTextView>(R.id.skipTextView).setOnClickListener {
            viewModel.skip()
        }

        findViewById<AppCompatButton>(R.id.getStartedButton).setOnClickListener {
            // Handle button click to navigate to the new activity
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun updateUI(currentPage: Int) {
        // Update UI elements based on the current page
        val imageView = findViewById<AppCompatImageView>(R.id.imageView)
        val textView = findViewById<AppCompatTextView>(R.id.textView)

        when (currentPage) {
            0 -> {
                imageView.setImageResource(R.drawable.storyboard_1)
                textView.text = getString(R.string.storyboardone)
            }
            1 -> {
                imageView.setImageResource(R.drawable.storyboard_2)
                textView.text = getString(R.string.storyboard2)
            }
            2 -> {
                imageView.setImageResource(R.drawable.storyboard_3)
                textView.text = getString(R.string.storyboard3)
            }
            else -> {
                // Display the Get Started button on the last screen
                findViewById<AppCompatButton>(R.id.getStartedButton).visibility = View.VISIBLE
                findViewById<AppCompatTextView>(R.id.nextTextView).visibility = View.GONE
                findViewById<AppCompatTextView>(R.id.skipTextView).visibility = View.GONE
                findViewById<LinearLayout>(R.id.dotsLayout).visibility = View.GONE
            }
        }

        // Update dot visibility based on the current page
        updateDotVisibility(currentPage)
    }

    private fun updateDotVisibility(currentPage: Int) {
        val dotsLayout = findViewById<LinearLayout>(R.id.dotsLayout)
        for (i in 0 until dotsLayout.childCount) {
            val dot = dotsLayout.getChildAt(i) as View
            if (i == currentPage) {
                dot.setBackgroundResource(R.drawable.dot_active)
            } else {
                dot.setBackgroundResource(R.drawable.dot_inactive)
            }
        }
    }
}