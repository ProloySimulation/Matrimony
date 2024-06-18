package com.kuhu.xosstech.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.kuhu.xosstech.R
import com.kuhu.xosstech.data.UserProfile
import com.kuhu.xosstech.databinding.ItemSuggestedListBinding
import com.squareup.picasso.Picasso

class ProfileAdapter(private val profiles: List<UserProfile>): RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {

    private lateinit var onButtonClickListener: OnButtonClickListener

    fun setOnButtonClickListener(listener: OnButtonClickListener) {
        this.onButtonClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemSuggestedListBinding = DataBindingUtil.inflate(inflater, R.layout.item_suggested_list, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val profile = profiles[position]
        holder.bind(profile)
    }

    override fun getItemCount(): Int {
        return profiles.size
    }

    inner class ViewHolder(private val binding: ItemSuggestedListBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnSendRequest.setOnClickListener {
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION)
                {
                    val profile = profiles[position]
                    onButtonClickListener.onButtonClick(profile.id)

                    // Update the button click state
                    profile.isButtonClicked = true
                    notifyItemChanged(position)
                }
            }
            binding.parentLayoutSuggest.setOnClickListener{
                val profile = profiles[position]
                onButtonClickListener.onProfileView(profile.id)
                notifyItemChanged(position)
            }
            binding.parentProfileChat.setOnClickListener {
                val profile = profiles[position]
                onButtonClickListener.onButtonMessage(profile.id)
                notifyItemChanged(position)
            }
            /*binding.matchParentLayout.setOnClickListener {
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION)
                {
                    val profile = profiles[position]
                    onButtonClickListener.onProfileView(profile.id)
                    notifyItemChanged(position)
                }
            }*/
        }
        fun bind(profile: UserProfile) {
            binding.suggestedProfile = profile

            Picasso.get()
                .load(profile.profilePicture)
                .placeholder(R.drawable.dp_cover)
                .error(R.drawable.demo_dp)
                .into(binding.imvProfileSuggestList)

            binding.executePendingBindings()
        }
    }

    interface OnButtonClickListener {
        fun onButtonClick(profileId: Int)
        fun onButtonMessage(profileId: Int)
        fun onProfileView(profileId: Int)
    }
}
