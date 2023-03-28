package com.example.githubuserfinder.ui.follow

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.githubuserfinder.data.model.UserResponse
import com.example.githubuserfinder.databinding.ItemUsersBinding

class FollowAdapter(private val UserList: List<UserResponse>) : RecyclerView.Adapter<FollowAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = UserList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gitUser = UserList[position]
        Glide.with(holder.itemView.context)
            .load(gitUser.avatarUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .centerCrop()
            .circleCrop()
            .into(holder.binding.ciAvatar)
        holder.apply {
            binding.apply {
                tvUsername.text = gitUser.login
            }
        }
    }

    class ViewHolder(val binding: ItemUsersBinding): RecyclerView.ViewHolder(binding.root)
}