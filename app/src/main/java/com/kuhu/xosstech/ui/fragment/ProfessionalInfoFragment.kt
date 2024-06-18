package com.kuhu.xosstech.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.kuhu.xosstech.databinding.FragmentProfessionalInfoBinding
import com.kuhu.xosstech.network.ApiConfig
import com.kuhu.xosstech.util.SharedPreferencesManager
import com.kuhu.xosstech.viewmodel.ProfileViewModel


class ProfessionalInfoFragment : Fragment() {

    private lateinit var binding: FragmentProfessionalInfoBinding
    private lateinit var sharedPreferences: SharedPreferencesManager

    private val viewModel: ProfileViewModel by lazy {
        ProfileViewModel(ApiConfig.BASE_URL)
    }

    companion object{
        private const val ARG_USER_ID = "user_id"

        fun newInstance(userId: Int): ProfessionalInfoFragment {
            val fragment = ProfessionalInfoFragment()
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
        // Inflate the layout for this fragment
        binding = FragmentProfessionalInfoBinding.inflate(inflater,container,false)
        val view = binding.root

        sharedPreferences = SharedPreferencesManager(requireActivity())

        // Set the lifecycle owner
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.userProfile.observe(requireActivity(), Observer { userProfile->
            binding.userProfile = userProfile
        })

        viewModel.setSharedPreferences(sharedPreferences)
        viewModel.getUserProfile("Bearer "+sharedPreferences.getLoginToken().toString())

        // Checking Others UserId
        val userId = arguments?.getInt(ProfessionalInfoFragment.ARG_USER_ID)
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
}