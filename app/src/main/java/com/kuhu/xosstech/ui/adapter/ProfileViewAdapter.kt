package com.kuhu.xosstech.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.kuhu.xosstech.R
import com.kuhu.xosstech.data.ProfileView
import com.kuhu.xosstech.databinding.ItemMatchViewBinding
import com.kuhu.xosstech.databinding.ItemProfileViewBinding
import com.squareup.picasso.Picasso

class ProfileViewAdapter(private val profiles: List<ProfileView>, private val viewType: Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var onButtonClickListener: OnButtonClickListener

    fun setOnButtonClickListener(listener: OnButtonClickListener) {
        this.onButtonClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_PROFILE_VIEW -> {
                val inflater = LayoutInflater.from(parent.context)
                val binding: ItemProfileViewBinding = DataBindingUtil.inflate(inflater, R.layout.item_profile_view, parent, false)
                ProfileViewHolder(binding)
            }
            VIEW_TYPE_ANOTHER_VIEW -> {
                // Inflate another layout for another view type
                val inflater = LayoutInflater.from(parent.context)
                val binding: ItemMatchViewBinding = DataBindingUtil.inflate(inflater, R.layout.item_match_view, parent, false)
                AnotherViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_PROFILE_VIEW -> (holder as ProfileViewHolder).bind(profiles[position])
            VIEW_TYPE_ANOTHER_VIEW -> (holder as AnotherViewHolder).bind(profiles[position])
        }
    }

    override fun getItemCount(): Int {
        return profiles.size
    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    inner class ProfileViewHolder(private val binding: ItemProfileViewBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.parentLayoutAccepted.setOnClickListener {
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION) {
                    val profile = profiles[position]
                    onButtonClickListener.onProfileView(profile.id)
                    notifyItemChanged(position)
                }
            }
        }

        fun bind(profile: ProfileView) {
            binding.profileView = profile

            Picasso.get()
                .load(profile.viewers.profilePicture)
                .placeholder(R.drawable.dp_cover)
                .error(R.drawable.demo_dp)
                .into(binding.imvProfileViewSecond)

            binding.executePendingBindings()
        }
    }

    inner class AnotherViewHolder(private val binding: ItemMatchViewBinding) : RecyclerView.ViewHolder(binding.root) {
        // Similar to ProfileViewHolder, implement binding logic for another view type
        fun bind(profile: ProfileView) {
            binding.profileView = profile

            Picasso.get()
                .load(profile.viewers.profilePicture)
                .placeholder(R.drawable.dp_cover)
                .error(R.drawable.demo_dp)
                .into(binding.imvProfileView)

            binding.executePendingBindings()
        }
    }

    interface OnButtonClickListener {
        fun onProfileView(profileId: Int)
    }

    companion object {
        const val VIEW_TYPE_PROFILE_VIEW = 0
        const val VIEW_TYPE_ANOTHER_VIEW = 1
    }
}