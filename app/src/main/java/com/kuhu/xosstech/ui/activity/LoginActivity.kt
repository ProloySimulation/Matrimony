package com.kuhu.xosstech.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.Toast
import com.kuhu.xosstech.MainActivity
import com.kuhu.xosstech.R
import com.kuhu.xosstech.network.ApiConfig
import com.kuhu.xosstech.util.CustomLoadingScreen
import com.kuhu.xosstech.util.GoogleAccountHelper
import com.kuhu.xosstech.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var email:String
    private lateinit var loadingScreen: CustomLoadingScreen

    private val viewModel: LoginViewModel by lazy {
        LoginViewModel(ApiConfig.BASE_URL)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loadingScreen = CustomLoadingScreen(this)
        val emailAccounts = GoogleAccountHelper.getGoogleAccountEmails(this)
        val adapter = ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,emailAccounts)
        etLoginEmail.setAdapter(adapter)

        btnLoginGetOtp.setOnClickListener {
            email = etLoginEmail.text.toString().trim()

            if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showLoadingScreen()
                viewModel.registerEmail(email)
            }

            else {
                Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.responseMessage.observe(this) { message ->
            viewModel.loginEmail(email)
        }

        viewModel.loginMessage.observe(this){message ->
            hideLoadingScreen()
            if(message.equals("OTP sent to your email"))
            {
                val intent = Intent(this, OtpActivity::class.java)
                intent.putExtra("EMAIL", email)
                startActivity(intent)
            }
        }
    }

    private fun showLoadingScreen() {
        loadingScreen.show()
    }

    private fun hideLoadingScreen() {
        loadingScreen.dismiss()
    }
}