package com.kuhu.xosstech.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.kuhu.xosstech.R
import com.kuhu.xosstech.data.MatchRequest
import com.kuhu.xosstech.databinding.ItemMatchRequestBinding
import com.squareup.picasso.Picasso

class MatchRequestAdapter(private val profiles: List<MatchRequest>): RecyclerView.Adapter<MatchRequestAdapter.ViewHolder>() {

    private lateinit var onButtonClickListener: OnButtonClickListener

    fun setOnButtonClickListener(listener: OnButtonClickListener) {
        this.onButtonClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemMatchRequestBinding = DataBindingUtil.inflate(inflater, R.layout.item_match_request, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val profile = profiles[position]
        holder.bind(profile)
    }

    override fun getItemCount(): Int {
        return profiles.size
    }

    inner class ViewHolder(private val binding: ItemMatchRequestBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnAcceptRequest.setOnClickListener {
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION)
                {
                    val profile = profiles[position]
                    onButtonClickListener.onButtonClick(profile.id)
                    profile.isButtonClicked = true
                    notifyItemChanged(position)
                }
            }
            binding.btnRejectRequest.setOnClickListener {
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION)
                {
                    val profile = profiles[position]
                    onButtonClickListener.onRejectButtonClick(profile.id)
                    profile.isButtonClicked = true
                    notifyItemChanged(position)
                }
            }
            binding.matchParentLayout.setOnClickListener {
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION)
                {
                    val profile = profiles[position]
                    onButtonClickListener.onProfileView(profile.id)
                    notifyItemChanged(position)
                }
            }
        }
        fun bind(profile: MatchRequest) {
            binding.matchProfile = profile

            Picasso.get()
                .load(profile.sender.profilePicture)
                .placeholder(R.drawable.dp_cover)
                .error(R.drawable.demo_dp)
                .into(binding.imvProfileMatchRequest)

            binding.executePendingBindings()
        }
    }

    interface OnButtonClickListener {
        fun onButtonClick(profileId: Int)
        fun onRejectButtonClick(profileId: Int)
        fun onProfileView(profileId: Int)
    }
}