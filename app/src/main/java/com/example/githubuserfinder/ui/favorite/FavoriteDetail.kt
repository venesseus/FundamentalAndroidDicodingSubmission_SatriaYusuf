package com.example.githubuserfinder.ui.favorite

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.githubuserfinder.R
import com.example.githubuserfinder.data.database.User
import com.example.githubuserfinder.data.model.UserResponse
import com.example.githubuserfinder.databinding.ActivityDetailBinding
import com.example.githubuserfinder.ui.detail.DetailActivity
import com.example.githubuserfinder.ui.detail.DetailViewModel
import com.example.githubuserfinder.ui.detail.DetailViewModelFactory

class FavoriteDetail : AppCompatActivity() {

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: DetailViewModel
    private var detailUser = UserResponse()

    private var ivFavorite: Boolean = false
    private var favoriteUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        viewModel = obtainViewModel(this@FavoriteDetail)
        val user = intent.getStringExtra(DetailActivity.EXTRA_FAVORITE)

        if (user != null) {
            viewModel.getDetailUser(user)
        }

        viewModel.listUser.observe(this) { detailList ->
            detailUser = detailList

            if (detailList != null) {
                binding?.let {
                    Glide.with(this)
                        .load(detailList.avatarUrl)
                        .circleCrop()
                        .into(it.ivDetailImage)
                }
            }
            binding?.apply {
                tvDetailName.text = detailList.name
                tvDetailUsername.text = detailList.login
                tvDetailFollowers.text = detailList.followers.toString()
                tvDetailFollowing.text = detailList.following.toString()
                tvDetailRepository.text = detailList.publicRepos.toString()
            }

            favoriteUser = User(detailList.id, detailList.login, detailList.avatarUrl)
            viewModel.getFavorite().observe(this) { userFavorite ->
                if (userFavorite != null) {
                    for (data in userFavorite) {
                        if (detailList.id == data.id) {
                            ivFavorite = true
                            binding?.btnFavorite?.visibility = View.VISIBLE // Kalo layoutnya ilang atau ancur coba ubah ke variabel yang lain
                        }
                    }
                }
            }

            binding?.btnFavorite?.setOnClickListener {
                if (ivFavorite) {
                    ivFavorite = true
                    binding!!.btnFavorite.visibility = View.VISIBLE // Ini juga
                    insertToDatabase(detailUser)
                } else {
                    ivFavorite = false
                    binding!!.btnFavorite.visibility = View.VISIBLE // Ini pun juga
                    viewModel.delete(detailUser.id)
                    Toast.makeText(this, "Delete Favorite", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.loading.observe(this){
            showLoading(it)
        }

        viewModel.error.observe(this){
            Toast.makeText(this, "Data Not Found", Toast.LENGTH_SHORT).show()
            viewModel.doneToastError()
        }
    }

        private fun insertToDatabase(gitDetailList: UserResponse) {
            favoriteUser.let { favoriteUser ->
                favoriteUser?.id = gitDetailList.id
                favoriteUser?.login = gitDetailList.login
                favoriteUser?.imageUrl = gitDetailList.avatarUrl
                viewModel.insert(favoriteUser as User)
                Toast.makeText(this, "Favorited", Toast.LENGTH_SHORT).show()
            }
        }

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = DetailViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DetailViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding?.detailProgressBar?.visibility = View.VISIBLE
        } else {
            binding?.detailProgressBar?.visibility = View.GONE
        }
    }
}