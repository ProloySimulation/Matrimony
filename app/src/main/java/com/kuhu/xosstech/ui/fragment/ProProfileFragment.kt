package com.kuhu.xosstech.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.kuhu.xosstech.MainActivity
import com.kuhu.xosstech.R
import com.kuhu.xosstech.databinding.FragmentProProfileBinding
import com.kuhu.xosstech.network.ApiConfig
import com.kuhu.xosstech.util.SharedPreferencesManager
import com.kuhu.xosstech.viewmodel.ProfileUpdateViewModel

class ProProfileFragment : Fragment() {

    private val viewModel: ProfileUpdateViewModel by lazy {
        ProfileUpdateViewModel(ApiConfig.BASE_URL)
    }
    private lateinit var sharedPreferences: SharedPreferencesManager
    private lateinit var binding:FragmentProProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        sharedPreferences = SharedPreferencesManager(requireActivity())
        val token = sharedPreferences.getLoginToken()

        binding.btnProProfileContinue.setOnClickListener {

            val higherQualification = binding.autoCompleteTextViewHigherQualification.text.toString()
            val jobType = binding.autoCompleteTextViewJobType.text.toString()
            val designation = binding.autoCompleteTextViewMaritalDesignation.text.toString()
            val annualIncome = binding.autoCompleteTextViewAnnualIncome.text.toString()

            val updatedFields = mutableMapOf<String, String>().apply {
                higherQualification.takeIf { it.isNotEmpty() }?.let { put("education_level", it) }
                jobType.takeIf { it.isNotEmpty() }?.let { put("profession", it) }
                designation.takeIf { it.isNotEmpty() }?.let { put("designation", it) }
                annualIncome.takeIf { it.isNotEmpty() }?.let { put("income", it) }
            }

            if (token != null && updatedFields.isNotEmpty()) {
                viewModel.updateProfile(updatedFields, "Bearer " + token)
            } else {
                // Display error message here, for example:
                Toast.makeText(requireActivity(), "All fields are required", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.responseMessage.observe(requireActivity()){message ->

            Toast.makeText(requireActivity(),message, Toast.LENGTH_SHORT).show()
            Log.e("errorMessage",message)
            if(message.equals("Info Update Successfully"))
            {
                startActivity(Intent(requireContext(), MainActivity::class.java))
            }
        }

        setupAutoCompleteTextViewAdapter(binding.autoCompleteTextViewJobType,R.array.spinner_job)

        return view
    }

    fun setupAutoCompleteTextViewAdapter(autoCompleteTextView: AutoCompleteTextView, itemsArrayId: Int) {
        val adapter = ArrayAdapter.createFromResource(
            autoCompleteTextView.context,
            itemsArrayId,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        autoCompleteTextView.setAdapter(adapter)
    }
}