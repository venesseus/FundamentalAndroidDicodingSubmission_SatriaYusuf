package com.example.githubuserfinder.ui.favorite

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.githubuserfinder.data.database.User
import com.example.githubuserfinder.data.util.DiffCallback
import com.example.githubuserfinder.databinding.ItemUsersBinding
import com.example.githubuserfinder.ui.detail.DetailActivity

class FavoriteAdapter : ListAdapter<User, FavoriteAdapter.FavoriteViewHolder>(DIFF_CALLBACK) {

    private val userFavorite = ArrayList<User>()

    fun setFavorite(userFav : List<User>){
        val diffCallback = DiffCallback(this.userFavorite, userFav)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.userFavorite.clear()
        this.userFavorite.addAll(userFav)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int, ): FavoriteViewHolder {
        val itemUserBinding = ItemUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(itemUserBinding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val userHolder = userFavorite[position]
        holder.bind(userHolder)
    }

    class FavoriteViewHolder(val binding : ItemUsersBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(userFav : User){
            with(binding){
                tvUsername.text = userFav.login
                itemView.setOnClickListener{
                    val intent = Intent(itemView.context, FavoriteDetail::class.java)
                    intent.putExtra(DetailActivity.EXTRA_FAVORITE, userFav.login)
                    itemView.context.startActivity(intent)
                    println(userFav.login)
                }
            }
            Glide.with(itemView.context)
                .load(userFav.imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .circleCrop()
                .into(binding.ciAvatar) //Coba cek apa butuh layout item_row tambahan atau engga
        }
    }
    override fun getItemCount(): Int = userFavorite.size

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<User> =
            object : DiffUtil.ItemCallback<User>() {
                override fun areItemsTheSame(oldUser: User, newUser: User): Boolean {
                    return oldUser.login == newUser.login
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldUser: User, newUser: User): Boolean {
                    return oldUser == newUser
                }
            }
    }
}