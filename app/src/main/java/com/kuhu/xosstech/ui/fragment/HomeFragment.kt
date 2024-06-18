package com.kuhu.xosstech.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuhu.xosstech.R
import com.kuhu.xosstech.data.UserProfile
import com.kuhu.xosstech.databinding.FragmentHomeBinding
import com.kuhu.xosstech.network.ApiConfig
import com.kuhu.xosstech.ui.activity.UserProfileActivity
import com.kuhu.xosstech.ui.activity.WebViewActivity
import com.kuhu.xosstech.ui.adapter.ProfileAdapter
import com.kuhu.xosstech.ui.dialog.BdAppsDialog
import com.kuhu.xosstech.ui.dialog.CommonDialog
import com.kuhu.xosstech.util.SharedPreferencesManager
import com.kuhu.xosstech.viewmodel.HomeViewModel


class HomeFragment : Fragment(), ProfileAdapter.OnButtonClickListener {

    private lateinit var sharedPreferences: SharedPreferencesManager
    private lateinit var binding: FragmentHomeBinding

    private val viewModel: HomeViewModel by lazy {
        HomeViewModel(ApiConfig.BASE_URL)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        val view = binding.root

        // Initialize SharedPreferencesManager
        sharedPreferences = SharedPreferencesManager(requireActivity())
        binding.recyclerHomeSuggestedList.layoutManager = LinearLayoutManager(requireContext())

        viewModel.suggestedProfiles.observe(requireActivity(), Observer { profiles->
            updateUI(profiles)
        })

        viewModel.responseMessage.observe(requireActivity()){message->
            Toast.makeText(requireActivity(),message,Toast.LENGTH_SHORT).show()
        }

        viewModel.suggestedList("Bearer "+sharedPreferences.getLoginToken().toString())
        viewModel.getUserProfile("Bearer "+sharedPreferences.getLoginToken().toString())

        return view
    }

    private fun updateUI(profiles: List<UserProfile>) {
        val adapter = ProfileAdapter(profiles)
        adapter.setOnButtonClickListener(this)
        binding.recyclerHomeSuggestedList.adapter = adapter
    }

    override fun onButtonClick(profileId: Int) {
        viewModel.sendRequest("Bearer "+sharedPreferences.getLoginToken().toString(),profileId)
    }

    override fun onButtonMessage(profileId: Int) {
        Toast.makeText(requireActivity(),"Need To Match Both Side",Toast.LENGTH_SHORT).show()
    }

    override fun onProfileView(profileId: Int) {
        // Observe the userProfile LiveData only once
        //Bdapps
        /*viewModel.userProfile.observeOnce(viewLifecycleOwner) { userProfile ->
            if (userProfile.subscriptionStatus.equals("REGISTERED")) {
                // If user is registered, open UserProfileActivity
                val intent = Intent(requireActivity(), UserProfileActivity::class.java)
                intent.putExtra("USER_ID", profileId)
                startActivity(intent)
            } else {
                // If user is not registered, show the dialog
                showRegistrationDialog()
            }
        }*/

        //bkash

        viewModel.userProfile.observe(viewLifecycleOwner) { userProfile ->
            val subscriptionDate = userProfile.subscriptionDate // Assuming subscriptionDate is a String
            if (viewModel.isWithinOneMonth(subscriptionDate)) {
                // If subscriptionDate is within one month, take action
                val intent = Intent(requireActivity(), UserProfileActivity::class.java)
                intent.putExtra("USER_ID", profileId)
                startActivity(intent)
            } else {
                // If subscriptionDate is not within one month, show the dialog for registration
                showBkashDialog()
            }
        }
    }

    private fun showRegistrationDialog() {
        // Create an instance of BdAppsDialog
        val bdAppsDialog = BdAppsDialog(requireContext())

        // Set up onCancelClick listener
        bdAppsDialog.onCancelClick = {
            bdAppsDialog.dismiss()
        }

        // Set up onOkClick listener
        bdAppsDialog.onOkClick = { mobileNumber ->
            viewModel.sendBdappsSubscription(mobileNumber)
            bdAppsDialog.dismiss()

            val dialog = CommonDialog(requireActivity(), getString(R.string.a_confirmation_will_come_to_your_mobile))
            dialog.show()
        }

        // Show the dialog
        bdAppsDialog.show()
    }

    private fun showBkashDialog(){
        val dialog = CommonDialog(requireActivity(), getString(R.string.you_can_buy_monthly_package_only_at_50BDT),
            object : CommonDialog.OnOkClickListener {
                override fun onOkClick() {
                    // Code to execute when "Ok" button is clicked
                    val user_id = sharedPreferences.getUserId().toString()
                    val intent = Intent(requireContext(), WebViewActivity::class.java).apply {
                        putExtra(WebViewActivity.EXTRA_URL,"https://xosstech.com/projects/Matrimony/Payment/bkash/payment.php?user_id=")
                        putExtra(WebViewActivity.SUCCESS_URL,"https://xosstech.com/projects/Matrimony/Payment/bkash/success.html")
                        putExtra(WebViewActivity.USER_ID, user_id)
                    }
                    startActivity(intent)
                }
            })
        dialog.show()
    }
}