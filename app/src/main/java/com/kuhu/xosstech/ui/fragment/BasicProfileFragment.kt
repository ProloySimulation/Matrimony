package com.kuhu.xosstech.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.kuhu.xosstech.R
import com.kuhu.xosstech.databinding.FragmentBasicProfileBinding
import com.kuhu.xosstech.network.ApiConfig
import com.kuhu.xosstech.util.SharedPreferencesManager
import com.kuhu.xosstech.viewmodel.ProfileUpdateViewModel


class BasicProfileFragment : Fragment() {

    private val viewModel: ProfileUpdateViewModel by lazy {
        ProfileUpdateViewModel(ApiConfig.BASE_URL)
    }
    private lateinit var sharedPreferences: SharedPreferencesManager
    private lateinit var binding:FragmentBasicProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBasicProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        sharedPreferences = SharedPreferencesManager(requireActivity())
        val token = sharedPreferences.getLoginToken()

        if (token != null) {
            Log.e("logintoken",token)
        }

        binding.btnBasicProfileContinue.setOnClickListener {
            val fullName = binding.autoCompleteTextViewFullName.text.toString()
            val division = binding.autoCompleteTextViewDivsion.text.toString()
            val district = binding.autoCompleteTextViewDistrict.text.toString()
            val city = binding.autoCompleteTextViewCity.text.toString()
            val gender = binding.autoCompleteTextViewGender.text.toString()
            val maritalStatus = binding.autoCompleteTextViewMarital.text.toString()
            val religion = binding.autoCompleteTextViewReligion.text.toString()

            val updatedFields = mutableMapOf<String, String>().apply {
                fullName.takeIf { it.isNotEmpty() }?.let { put("name", it) }
                division.takeIf { it.isNotEmpty() }?.let { put("division", it) }
                district.takeIf { it.isNotEmpty() }?.let { put("district", it) }
                city.takeIf { it.isNotEmpty() }?.let { put("city", it) }
                gender.takeIf { it.isNotEmpty() }?.let { put("gender", it) }
                maritalStatus.takeIf { it.isNotEmpty() }?.let { put("marital_status", it) }
                religion.takeIf { it.isNotEmpty() }?.let { put("religion", it) }
            }

            if (token != null) {
                if (updatedFields.isNotEmpty()) {
                    viewModel.updateProfile(updatedFields, "Bearer " + token)
                } else {
                    Toast.makeText(requireActivity(), "All fields are required", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.responseMessage.observe(requireActivity()){message ->

            Toast.makeText(requireActivity(),message,Toast.LENGTH_SHORT).show()
            Log.e("errorMessage",message)
            if(message.equals("Info Update Successfully"))
            {
                val viewPager = activity?.findViewById<ViewPager>(R.id.viewPager)
                viewPager?.setCurrentItem(1, true)
            }
        }

        // Dropdown menu declare

        setupAutoCompleteTextViewAdapter(binding.autoCompleteTextViewDivsion,R.array.spinner_division)
        setupAutoCompleteTextViewAdapter(binding.autoCompleteTextViewGender,R.array.spinner_gender)
        setupAutoCompleteTextViewAdapter(binding.autoCompleteTextViewReligion,R.array.spinner_relgion)
        setupAutoCompleteTextViewAdapter(binding.autoCompleteTextViewMarital,R.array.spinner_marital_status)
        setupAutoCompleteTextViewAdapter(binding.autoCompleteTextViewDistrict,R.array.spinner_district)

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