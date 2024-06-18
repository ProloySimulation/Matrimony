package com.kuhu.xosstech.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuhu.xosstech.data.ProfileView
import com.kuhu.xosstech.data.UserProfile
import com.kuhu.xosstech.databinding.FragmentMatchBinding
import com.kuhu.xosstech.network.ApiConfig
import com.kuhu.xosstech.ui.adapter.AcceptedAdapter
import com.kuhu.xosstech.ui.adapter.ProfileViewAdapter
import com.kuhu.xosstech.util.SharedPreferencesManager
import com.kuhu.xosstech.viewmodel.MatchViewModel
import kotlinx.android.synthetic.main.activity_profile_view.*


class MatchFragment : Fragment() {

    private lateinit var binding:FragmentMatchBinding
    private lateinit var sharedPreferences: SharedPreferencesManager

    private val viewModel: MatchViewModel by lazy {
        MatchViewModel(ApiConfig.BASE_URL)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMatchBinding.inflate(inflater,container,false)
        val view = binding.root

        // Initialize SharedPreferencesManager
        sharedPreferences = SharedPreferencesManager(requireActivity())
        binding.recyclerMatchView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerMatchNewMatch.layoutManager = LinearLayoutManager(requireContext())

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.profileRequest.observe(requireActivity(), Observer { profiles->
            updateUI(profiles)
        })

        viewModel.matchRequest.observe(requireActivity(), Observer { profiles->
            updateMatchUI(profiles)
        })

        viewModel.navigateBack.observe(viewLifecycleOwner) { navigateBack ->
            if (navigateBack) {
                // Perform navigation back to previous activity or fragment
                findNavController().popBackStack()
            }
        }

        viewModel.getProfileView("Bearer "+sharedPreferences.getLoginToken().toString())
        viewModel.acceptedList("Bearer "+sharedPreferences.getLoginToken().toString())

        return view
    }

    private fun updateUI(profiles: List<ProfileView>) {
        val adapter = ProfileViewAdapter(profiles, ProfileViewAdapter.VIEW_TYPE_ANOTHER_VIEW)
        binding.recyclerMatchView.adapter = adapter
    }

    private fun updateMatchUI(profiles: List<UserProfile>) {
        val adapter = AcceptedAdapter(profiles,
            AcceptedAdapter.VIEW_TYPE_PROFILE_MATCH_MATCH_FRAGMENT
        )
        binding.recyclerMatchNewMatch.adapter = adapter
    }
}