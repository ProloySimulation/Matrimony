package com.kuhu.xosstech.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuhu.xosstech.data.UserProfile
import com.kuhu.xosstech.databinding.FragmentAcceptBinding
import com.kuhu.xosstech.network.ApiConfig
import com.kuhu.xosstech.ui.activity.OnlineActivity
import com.kuhu.xosstech.ui.adapter.AcceptedAdapter
import com.kuhu.xosstech.util.SharedPreferencesManager
import com.kuhu.xosstech.viewmodel.RequestViewModel


class AcceptFragment : Fragment(), AcceptedAdapter.OnButtonClickListener {

    private lateinit var binding: FragmentAcceptBinding
    private lateinit var sharedPreferences: SharedPreferencesManager

    private val viewModel: RequestViewModel by lazy {
        RequestViewModel(ApiConfig.BASE_URL)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAcceptBinding.inflate(inflater,container,false)
        val view = binding.root

        // Initialize SharedPreferencesManager
        sharedPreferences = SharedPreferencesManager(requireActivity())
        binding.recyclerAcceptedList.layoutManager = LinearLayoutManager(requireContext())

        viewModel.acceptedRequest.observe(requireActivity(), Observer { profiles->
            updateUI(profiles)
        })

        viewModel.acceptedList("Bearer "+ sharedPreferences.getLoginToken().toString())

        return view
    }

    private fun updateUI(profiles: List<UserProfile>) {
        val adapter = AcceptedAdapter(profiles, AcceptedAdapter.VIEW_TYPE_PROFILE_MATCH)
        adapter.setOnButtonClickListener(this)
        binding.recyclerAcceptedList.adapter = adapter
    }

    override fun onProfileView(profileId: Int) {
        val intent = Intent(requireActivity(), OnlineActivity::class.java)
        intent.putExtra("USER_ID", profileId)
        startActivity(intent)
    }
}