package com.kuhu.xosstech.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuhu.xosstech.data.UserProfile
import com.kuhu.xosstech.databinding.FragmentSearchBinding
import com.kuhu.xosstech.network.ApiConfig
import com.kuhu.xosstech.ui.activity.UserProfileActivity
import com.kuhu.xosstech.ui.adapter.SearchAdapter
import com.kuhu.xosstech.util.SharedPreferencesManager
import com.kuhu.xosstech.viewmodel.SearchViewModel


class SearchFragment : Fragment(), SearchAdapter.OnButtonClickListener {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var sharedPreferences: SharedPreferencesManager

    private val viewModel: SearchViewModel by lazy {
        SearchViewModel(ApiConfig.BASE_URL,sharedPreferences)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater,container,false)
        val view = binding.root

        // Initialize SharedPreferencesManager
        sharedPreferences = SharedPreferencesManager(requireActivity())
        binding.searchRecycler.layoutManager = LinearLayoutManager(requireContext())

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.searchList.observe(requireActivity(), Observer { profiles->
            updateUI(profiles)
        })

        viewModel.navigateBack.observe(viewLifecycleOwner) { navigateBack ->
            if (navigateBack) {
                // Perform navigation back to previous activity or fragment
                findNavController().popBackStack()
            }
        }

        return view
    }

    private fun updateUI(profiles: List<UserProfile>) {
        val adapter = SearchAdapter(profiles)
        adapter.setOnButtonClickListener(this)
        binding.searchRecycler.adapter = adapter
    }

    override fun onProfileView(profileId: Int) {
        val intent = Intent(requireActivity(), UserProfileActivity::class.java)
        intent.putExtra("USER_ID", profileId)
        startActivity(intent)
    }
}