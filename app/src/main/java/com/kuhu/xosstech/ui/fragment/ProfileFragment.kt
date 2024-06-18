package com.kuhu.xosstech.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.kuhu.xosstech.R
import com.kuhu.xosstech.databinding.FragmentProfileBinding
import com.kuhu.xosstech.network.ApiConfig
import com.kuhu.xosstech.ui.activity.*
import com.kuhu.xosstech.ui.dialog.LogoutDialog
import com.kuhu.xosstech.util.SharedPreferencesManager
import com.kuhu.xosstech.viewmodel.ProfileViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_user_profile.*


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var sharedPreferences: SharedPreferencesManager

    private val viewModel: ProfileViewModel by lazy {
        ProfileViewModel(ApiConfig.BASE_URL)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        val view = binding.root

        sharedPreferences = SharedPreferencesManager(requireActivity())
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.cardChat.setOnClickListener {
            startActivity(Intent(requireActivity(), ChatActivity::class.java))
        }

        binding.cvOneLogin.setOnClickListener {
            startActivity(Intent(requireActivity(), UserProfileActivity::class.java))
        }

        binding.layoutNotification.setOnClickListener {
            startActivity(Intent(requireActivity(), NotificationActivity::class.java))
        }

        binding.layoutProfileView.setOnClickListener{
            startActivity(Intent(requireActivity(), ProfileViewActivity::class.java))
        }

        binding.cardMatch.setOnClickListener{
            Toast.makeText(requireActivity(),"Coming Soon",Toast.LENGTH_SHORT).show()
        }

        binding.cardLike.setOnClickListener{
            Toast.makeText(requireActivity(),"Coming Soon",Toast.LENGTH_SHORT).show()
        }

        binding.layoutLogout.setOnClickListener {
            val dialog = LogoutDialog(requireActivity())

            dialog.onCancelClick = {

            }

            dialog.onOkClick = {
                viewModel.logOut("Bearer "+sharedPreferences.getLoginToken().toString())
            }

            dialog.show()
        }

        binding.layoutTerms.setOnClickListener {
            showTerms()
        }

        viewModel.logoutStatus.observe(requireActivity()){message->
            if(message.equals("success"))
            {
                sharedPreferences.clearLoginToken()
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }

        viewModel.userProfile.observe(requireActivity(), Observer { userProfile ->
            binding.tvUserId.text = userProfile.username
            userProfile?.apply {
                profilePicture?.let { imageUrl ->
                    Picasso.get()
                        .load(imageUrl)
                        .placeholder(R.drawable.dp_cover)
                        .error(R.drawable.demo_dp)
                        .into(binding.imvProfileDp)

                    Picasso.get()
                        .load(imageUrl)
                        .placeholder(R.drawable.dp_cover)
                        .error(R.drawable.demo_dp)
                        .into(binding.imvProfileCover)
                }
                profileInfo.fullName?.let { fullName ->
                    binding.tvProfileFullName.text = fullName
                }

                if(userProfile.subscriptionStatus.equals("REGISTERED"))
                {
                    binding.tvSubscriptionStatus.text = getString(R.string.you_are_subscribed)
                }
                else
                {
                    binding.tvSubscriptionStatus.text = getString(R.string.you_are_unsubscribed)
                }
            }
        })

        viewModel.navigateBack.observe(viewLifecycleOwner) { navigateBack ->
            if (navigateBack) {
                // Perform navigation back to previous activity or fragment
                findNavController().popBackStack()
            }
        }

        viewModel.getUserProfile("Bearer "+sharedPreferences.getLoginToken().toString())

        return view
    }

    private fun showTerms(){
        val intent = Intent(requireContext(), WebViewActivity::class.java).apply {
            putExtra(WebViewActivity.EXTRA_URL,"https://xosstech.com/projects/Matrimony/privacy.html")
            putExtra(WebViewActivity.SUCCESS_URL,"https://xosstech.com/")
        }
        startActivity(intent)
    }
}