package com.example.githubuserfinder.ui.favorite

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuserfinder.R
import com.example.githubuserfinder.data.database.User
import com.example.githubuserfinder.data.model.UserResponse
import com.example.githubuserfinder.databinding.ActivityDetailBinding
import com.example.githubuserfinder.ui.detail.DetailActivity
import com.example.githubuserfinder.ui.detail.DetailViewModel
import com.example.githubuserfinder.ui.detail.DetailViewModelFactory
import com.example.githubuserfinder.ui.detail.SectionPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class FavoriteDetail : AppCompatActivity() {

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding

    private var detailUser = UserResponse()
    private var ivFavorite: Boolean = false
    private var favUser: User? = null

    private lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        viewModel = obtainViewModel(this@FavoriteDetail)
        val user = intent.getStringExtra(DetailActivity.EXTRA_FAVORITE)

        if (user != null) {
            viewModel.getDetailUser(user)
        }

        //Binding Items (Check this if there's an error on favorite
        viewModel.listUser.observe(this) { detailList ->
            detailUser = detailList

            favUser = User(detailUser.id, detailUser.login, detailUser.avatarUrl)
            binding?.let {
                Glide.with(this)
                    .load(detailUser.avatarUrl)
                    .circleCrop()
                    .into(it.ivDetailImage)
            }
            binding?.apply {
                tvDetailName.text = detailUser.name
                tvDetailUsername.text = detailUser.login
                tvDetailFollowers.text = detailUser.followers.toString()
                tvDetailFollowing.text = detailUser.following.toString()
                tvDetailRepository.text = detailUser.publicRepos.toString()
            }

            viewModel.getFavorite().observe(this) { userFavorite ->
                if (userFavorite != null) {
                    for (data in userFavorite) {
                        if (detailUser.id == data.id) {
                            ivFavorite = true
                            binding?.ivFavorite?.setImageResource(R.drawable.ic_draw_bookmarked)
                        }
                    }
                }
            }

            binding?.ivFavorite?.setOnClickListener {
                if (!ivFavorite) {
                    ivFavorite = true
                    binding!!.ivFavorite.setImageResource(R.drawable.ic_draw_bookmarked)
                    insertToDatabase(detailUser)
                } else {
                    ivFavorite = false
                    binding!!.ivFavorite.setImageResource(R.drawable.ic_draw_bookmark)
                    viewModel.delete(detailUser.id)
                    Toast.makeText(this, "Bookmark Deleted", Toast.LENGTH_SHORT).show()
                }
            }
        }

        //Tab untuk favorite
        val sectionPagerAdapter = SectionPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { detailTabs, position ->
            detailTabs.text = resources.getString(DetailActivity.GIT_TABS[position])
        }.attach()

        viewModel.loading.observe(this){
            showLoading(it)
        }

        viewModel.error.observe(this){
            Toast.makeText(this, "Data Not Found", Toast.LENGTH_SHORT).show()
            viewModel.doneToastError()
        }
    }

        private fun insertToDatabase(gitDetailList: UserResponse) {
            favUser.let { favoriteUser ->
                favoriteUser?.id = gitDetailList.id
                favoriteUser?.login = gitDetailList.login
                favoriteUser?.imageUrl = gitDetailList.avatarUrl
                viewModel.insert(favoriteUser as User)
                Toast.makeText(this, "Bookmarked", Toast.LENGTH_SHORT).show()
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