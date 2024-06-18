package com.kuhu.xosstech.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.kuhu.xosstech.databinding.FragmentPersonalInfoBinding
import com.kuhu.xosstech.network.ApiConfig
import com.kuhu.xosstech.util.CustomLoadingScreen
import com.kuhu.xosstech.util.SharedPreferencesManager
import com.kuhu.xosstech.viewmodel.ProfileViewModel


class PersonalInfoFragment : Fragment() {

    private lateinit var binding:FragmentPersonalInfoBinding
    private lateinit var sharedPreferences: SharedPreferencesManager
    private lateinit var loadingScreen: CustomLoadingScreen

    private val viewModel: ProfileViewModel by lazy {
        ProfileViewModel(ApiConfig.BASE_URL)
    }

    companion object{
        private const val ARG_USER_ID = "user_id"

        fun newInstance(userId: Int): PersonalInfoFragment {
            val fragment = PersonalInfoFragment()
            val args = Bundle()
            args.putInt(ARG_USER_ID, userId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPersonalInfoBinding.inflate(inflater,container,false)
        val view = binding.root

        loadingScreen = CustomLoadingScreen(requireActivity())
        sharedPreferences = SharedPreferencesManager(requireActivity())
        // Set the lifecycle owner
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.setSharedPreferences(sharedPreferences)
        viewModel.userProfile.observe(requireActivity(), Observer { userProfile->
            binding.userProfile = userProfile
        })

        // Checking Others User Id
        val userId = arguments?.getInt(ARG_USER_ID)
        if (userId != null) {
            viewModel.setUserId(userId)
            if(userId>0)
            {
                viewModel.getOtherProfile("Bearer "+sharedPreferences.getLoginToken().toString(),userId)
            }
            else
                viewModel.getUserProfile("Bearer "+sharedPreferences.getLoginToken().toString())
        }
        else
            viewModel.getUserProfile("Bearer "+sharedPreferences.getLoginToken().toString())

        return view
    }

    private fun showLoadingScreen() {
        loadingScreen.show()
    }

    private fun hideLoadingScreen() {
        loadingScreen.dismiss()
    }
}