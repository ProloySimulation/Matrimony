package com.kuhu.xosstech.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.kuhu.xosstech.MainActivity
import com.kuhu.xosstech.R
import com.kuhu.xosstech.network.ApiConfig
import com.kuhu.xosstech.util.CustomLoadingScreen
import com.kuhu.xosstech.util.SharedPreferencesManager
import com.kuhu.xosstech.viewmodel.OtpViewModel
import kotlinx.android.synthetic.main.activity_otp.*

class OtpActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferencesManager
    private lateinit var loadingScreen: CustomLoadingScreen

    private val viewModel: OtpViewModel by lazy {
        OtpViewModel(ApiConfig.BASE_URL)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        // Initialize SharedPreferencesManager
        sharedPreferences = SharedPreferencesManager(this)
        loadingScreen = CustomLoadingScreen(this)

        btnOtpVerify.setOnClickListener {
            showLoadingScreen()
            val otp = otpView.text.toString()
            val receivedEmail: String? = intent.getStringExtra("EMAIL")
            if (receivedEmail != null) {
                viewModel.otpVerify(otp,receivedEmail)
            }
        }

        viewModel.userProfile.observe(this, Observer { userProfile ->
            if(userProfile.profileInfo?.fullName.isNullOrEmpty()){
                val intent = Intent(this, ProfileFormActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            else{
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        })

        viewModel.responseMessage.observe(this){data ->
            viewModel.getUserProfile("Bearer "+data.token)
            sharedPreferences.saveLoginToken(data.token)
            data.user?.let { sharedPreferences.saveUserId(it.id) }
        }
    }

    private fun showLoadingScreen() {
        loadingScreen.show()
    }

    private fun hideLoadingScreen() {
        loadingScreen.dismiss()
    }
}