package com.kuhu.xosstech.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.kuhu.xosstech.R
import com.kuhu.xosstech.data.UserProfile
import com.kuhu.xosstech.databinding.ItemSearchBinding
import com.squareup.picasso.Picasso

class SearchAdapter(private val profiles: List<UserProfile>): RecyclerView.Adapter<SearchAdapter.ViewHolder>(){

    private lateinit var onButtonClickListener: OnButtonClickListener

    fun setOnButtonClickListener(listener: OnButtonClickListener) {
        this.onButtonClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemSearchBinding = DataBindingUtil.inflate(inflater, R.layout.item_search, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val profile = profiles[position]
        holder.bind(profile)
    }

    override fun getItemCount(): Int {
        return profiles.size
    }

    inner class ViewHolder(private val binding: ItemSearchBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.parentLayoutSearch.setOnClickListener{
                val profile = profiles[position]
                onButtonClickListener.onProfileView(profile.id)
                notifyItemChanged(position)
            }
        }

        fun bind(profile: UserProfile) {
            binding.suggestedProfile = profile

            Picasso.get()
                .load(profile.profilePicture)
                .placeholder(R.drawable.dp_cover)
                .error(R.drawable.demo_dp)
                .into(binding.imvSearchProfile)

            binding.executePendingBindings()
        }
    }

    interface OnButtonClickListener {
        fun onProfileView(profileId: Int)
    }
}