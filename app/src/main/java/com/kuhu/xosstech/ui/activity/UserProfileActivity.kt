package com.kuhu.xosstech.ui.activity

import android.animation.Animator
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.kuhu.xosstech.R
import com.kuhu.xosstech.network.ApiConfig
import com.kuhu.xosstech.ui.fragment.PersonalInfoFragment
import com.kuhu.xosstech.ui.fragment.ProfessionalInfoFragment
import com.kuhu.xosstech.util.SharedPreferencesManager
import com.kuhu.xosstech.viewmodel.ProfileViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_user_profile.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response.error
import java.io.File

class UserProfileActivity : AppCompatActivity() {

    private lateinit var sharedPreferences:SharedPreferencesManager

    private val viewModel: ProfileViewModel by lazy {
        ProfileViewModel(ApiConfig.BASE_URL)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        // Initialization
        sharedPreferences = SharedPreferencesManager(this)

        // Catch & Send USER ID From Another Activity
        val receivedData = intent.getIntExtra("USER_ID",0)

        val personalInfoFragment = PersonalInfoFragment.newInstance(receivedData)
        val professionalInfoFragment = ProfessionalInfoFragment.newInstance(receivedData)

        val fragments = listOf(personalInfoFragment, professionalInfoFragment)

        viewPagerProfile.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = fragments.size

            override fun createFragment(position: Int): Fragment = fragments[position]
        }

        TabLayoutMediator(tabsProfile, viewPagerProfile) { tab, position ->
            when (position) {
                0 -> tab.text = "Personal Info"
                1 -> tab.text = "Professional Info"
            }
        }.attach()

        imvProfilePicUserProfile.setOnClickListener {
            viewModel.onImageViewClicked()
        }

        viewModel.openGalleryEvent.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let {
                // Handle the event, request permissions, and open the gallery
                if(receivedData == 0)
                {
                    requestGalleryPermission()
                }
            }
        })

        layoutProfileLike.setOnClickListener {
            viewModel.sendRequest("Bearer "+sharedPreferences.getLoginToken().toString(),receivedData)
        }

        // Load Profile Picture
        viewModel.userProfile.observe(this, Observer { userProfile ->
            userProfile?.profilePicture?.let { imageUrl ->
                Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.dp_cover)
                    .error(R.drawable.demo_dp)
                    .into(imvProfilePicUserProfile, object : Callback {
                        override fun onSuccess() {
                            // Image loaded successfully
                        }

                        override fun onError(e: Exception?) {
                            Log.e("Picasso", "Image loading failed: $e")
                        }
                    })
            }
        })

        if(receivedData > 0)
        {
            viewModel.getOtherProfile("Bearer "+sharedPreferences.getLoginToken().toString(),receivedData)
        }
        else
        {
            viewModel.getUserProfile("Bearer "+sharedPreferences.getLoginToken().toString())
            cardViewProfileActivity.visibility = View.GONE
        }

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            anim.visibility = View.VISIBLE
            tvUploadAnimationTitle.visibility = View.VISIBLE
            anim.playAnimation()
        },1000)

        anim.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                tvUploadAnimationTitle.visibility = View.GONE
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }

        })
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openGallery()
            } else {
                // Handle the case when permission is not granted
                // You may want to show a message to the user
            }
        }

    private fun requestGalleryPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                openGallery()
            }
            else -> {
                // Request the permission
                requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val selectedImageUri = result.data?.data
                selectedImageUri?.let {
                    val filePart = createFilePartFromUri(it)
                    Picasso.get()
                        .load(it)
                        .placeholder(R.drawable.dp_cover)
                        .error(R.drawable.demo_dp)
                        .into(imvProfilePicUserProfile)
                    viewModel.uploadProfile("Bearer "+sharedPreferences.getLoginToken().toString(), filePart)
                }
            }
        }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(galleryIntent)
    }

    private fun createFilePartFromUri(uri: Uri): MultipartBody.Part {
        val contentResolver = contentResolver
        val file = File(getRealPathFromUri(uri))
        val requestBody: RequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData("profile_picture", file.name, requestBody)
    }

    private fun getRealPathFromUri(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            it.moveToFirst()
            val columnIndex = it.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            return it.getString(columnIndex)
        }
        return ""
    }
}