package com.kuhu.xosstech.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuhu.xosstech.R
import com.kuhu.xosstech.data.MatchRequest
import com.kuhu.xosstech.data.UserProfile
import com.kuhu.xosstech.databinding.FragmentRequestBinding
import com.kuhu.xosstech.network.ApiConfig
import com.kuhu.xosstech.ui.adapter.MatchRequestAdapter
import com.kuhu.xosstech.util.SharedPreferencesManager
import com.kuhu.xosstech.viewmodel.RequestViewModel


class RequestFragment : Fragment(), MatchRequestAdapter.OnButtonClickListener {

    private lateinit var binding: FragmentRequestBinding
    private lateinit var sharedPreferences: SharedPreferencesManager

    private val viewModel: RequestViewModel by lazy {
        RequestViewModel(ApiConfig.BASE_URL)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRequestBinding.inflate(inflater,container,false)
        val view = binding.root

        // Initialize SharedPreferencesManager
        sharedPreferences = SharedPreferencesManager(requireActivity())
        binding.recyclerNewRequest.layoutManager = LinearLayoutManager(requireContext())

        viewModel.matchRequest.observe(requireActivity(), Observer { profiles->
            updateUI(profiles)
        })

        viewModel.requestStatus.observe(requireActivity()){message->
            if(message.equals("success"))
             Toast.makeText(requireActivity(),getString(R.string.request_accepted), Toast.LENGTH_SHORT).show()
        }
//        viewModel.matchRequest("Bearer "+"25|ekHn8hOkN0ispPntjEA19HJEtdsb1LScSycL1sr85c15a8fd")
        viewModel.matchRequest("Bearer "+ sharedPreferences.getLoginToken().toString())

        return view
    }

    private fun updateUI(profiles: List<MatchRequest>) {
        Log.e("checkprofile",profiles.toString())
        val adapter = MatchRequestAdapter(profiles)
        adapter.setOnButtonClickListener(this)
        binding.recyclerNewRequest.adapter = adapter
    }

    override fun onButtonClick(profileId: Int) {
        val updatedFields = mutableMapOf<String, String>()
        updatedFields["match_status"] = "Accepted"
        viewModel.acceptRequest("Bearer "+sharedPreferences.getLoginToken().toString(),profileId,updatedFields)
        viewModel.setRequestAccepted(true)
    }

    override fun onRejectButtonClick(profileId: Int) {
        val updatedFields = mutableMapOf<String, String>()
        updatedFields["match_status"] = "Rejected"
        viewModel.acceptRequest("Bearer "+sharedPreferences.getLoginToken().toString(),profileId,updatedFields)
    }

    override fun onProfileView(profileId: Int) {
        val intent = Intent(requireActivity(), UserProfile::class.java)
        intent.putExtra("USER_ID", profileId)
        startActivity(intent)
    }
}