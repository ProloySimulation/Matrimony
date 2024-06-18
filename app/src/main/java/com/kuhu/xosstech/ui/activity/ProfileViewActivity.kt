package com.kuhu.xosstech.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuhu.xosstech.R
import com.kuhu.xosstech.data.ProfileView
import com.kuhu.xosstech.databinding.ActivityProfileViewBinding
import com.kuhu.xosstech.network.ApiConfig
import com.kuhu.xosstech.ui.adapter.ProfileViewAdapter
import com.kuhu.xosstech.ui.adapter.ProfileViewAdapter.Companion.VIEW_TYPE_PROFILE_VIEW
import com.kuhu.xosstech.util.SharedPreferencesManager
import com.kuhu.xosstech.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.activity_profile_view.*

class ProfileViewActivity : AppCompatActivity() {

    private lateinit var binding:ActivityProfileViewBinding
    private lateinit var sharedPreferences: SharedPreferencesManager

    private val viewModel: ProfileViewModel by lazy {
        ProfileViewModel(ApiConfig.BASE_URL)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_view)

        // Initialize SharedPreferencesManager
        sharedPreferences = SharedPreferencesManager(this)
        recyclerProfileView.layoutManager = LinearLayoutManager(this)
        // Set the lifecycle owner
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.profileRequest.observe(this, Observer { profiles->
            updateUI(profiles)
        })

        viewModel.navigateBack.observe(this, Observer { navigateBack ->
            if (navigateBack) {
                // Perform navigation back to previous activity or fragment
                onBackPressed()
            }
        })

    /*    imvBackProfileView.setOnClickListener {
            onBackPressed()
        }*/

        viewModel.getProfileView("Bearer "+sharedPreferences.getLoginToken().toString())
    }

    private fun updateUI(profiles: List<ProfileView>) {
        val adapter = ProfileViewAdapter(profiles,VIEW_TYPE_PROFILE_VIEW)
        recyclerProfileView.adapter = adapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}