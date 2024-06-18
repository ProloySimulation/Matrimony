package com.kuhu.xosstech.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.kuhu.xosstech.R
import com.kuhu.xosstech.data.UserProfile
import com.kuhu.xosstech.databinding.ItemAcceptListBinding
import com.kuhu.xosstech.databinding.ItemMatchNewBinding
import com.squareup.picasso.Picasso

class AcceptedAdapter(private val profiles: List<UserProfile>, private val viewType: Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var onButtonClickListener: OnButtonClickListener

    fun setOnButtonClickListener(listener: OnButtonClickListener) {
        this.onButtonClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            VIEW_TYPE_PROFILE_MATCH -> {
                val inflater = LayoutInflater.from(parent.context)
                val binding: ItemAcceptListBinding = DataBindingUtil.inflate(inflater, R.layout.item_accept_list, parent, false)
                ViewHolder(binding)
            }
            VIEW_TYPE_PROFILE_MATCH_MATCH_FRAGMENT -> {
                // Inflate another layout for another view type
                val inflater = LayoutInflater.from(parent.context)
                val binding: ItemMatchNewBinding = DataBindingUtil.inflate(inflater, R.layout.item_match_new, parent, false)
                AnotherViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_PROFILE_MATCH -> (holder as ViewHolder).bind(profiles[position])
            VIEW_TYPE_PROFILE_MATCH_MATCH_FRAGMENT -> (holder as AnotherViewHolder).bind(profiles[position])
        }
    }

    override fun getItemCount(): Int {
        return profiles.size
    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    inner class ViewHolder(private val binding: ItemAcceptListBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.parentLayoutAccepted.setOnClickListener {
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION)
                {
                    val profile = profiles[position]
                    onButtonClickListener.onProfileView(profile.id)
                    notifyItemChanged(position)
                }
            }
        }
        fun bind(profile: UserProfile) {
            binding.matchProfile = profile

            Picasso.get()
                .load(profile.profilePicture)
                .placeholder(R.drawable.dp_cover)
                .error(R.drawable.demo_dp)
                .into(binding.imvProfileAccept)

            binding.executePendingBindings()
        }
    }

    inner class AnotherViewHolder(private val binding: ItemMatchNewBinding) : RecyclerView.ViewHolder(binding.root) {
        // Similar to ProfileViewHolder, implement binding logic for another view type
        fun bind(profile: UserProfile) {
            binding.matchProfile = profile
            binding.executePendingBindings()
        }
    }

    interface OnButtonClickListener {
        fun onProfileView(profileId: Int)
    }

    companion object {
        const val VIEW_TYPE_PROFILE_MATCH = 0
        const val VIEW_TYPE_PROFILE_MATCH_MATCH_FRAGMENT = 1
    }
}