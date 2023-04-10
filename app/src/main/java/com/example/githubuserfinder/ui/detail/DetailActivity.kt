package com.example.githubuserfinder.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuserfinder.R
import com.example.githubuserfinder.data.database.User
import com.example.githubuserfinder.data.model.DetailUserResponse
import com.example.githubuserfinder.data.model.UserResponse
import com.example.githubuserfinder.databinding.ActivityDetailBinding
import com.example.githubuserfinder.ui.main.MainActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: DetailViewModel

    private var ivFavorite: Boolean = false
    private var favoriteUser: User? = null
    private var detailUser = UserResponse()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        viewModel = obtainViewModel(this@DetailActivity)
        val user = intent.getStringExtra(MainActivity.EXTRA_DATA)
        println("Print (${MainActivity.EXTRA_DATA}")

        if (user != null) {
            user.let { viewModel.getDetailUser(it) }
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
                            binding?.ivFavorite?.setImageResource(R.drawable.ic_draw_bookmarked)
                        }
                    }
                }
            }
            binding?.ivFavorite?.setOnClickListener{
                if(!ivFavorite){
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

            val sectionPagerAdapter = SectionPagerAdapter(this)
            val viewPager: ViewPager2 = findViewById(R.id.view_pager)
            viewPager.adapter = sectionPagerAdapter
            val tabs: TabLayout = findViewById(R.id.tabs)
            TabLayoutMediator(tabs, viewPager) { detailTabs, position ->
                detailTabs.text = resources.getString(GIT_TABS[position])
            }.attach()
        }

        viewModel.loading.observe(this) {
            showLoading(it)
        }

        viewModel.error.observe(this) {
            Toast.makeText(this, "Data Not Found", Toast.LENGTH_SHORT).show()
            viewModel.doneToastError()
        }
    }

    private fun insertToDatabase(detailList: UserResponse) {
        favoriteUser.let { favoriteUser ->
            favoriteUser?.id = detailList.id
            favoriteUser?.login = detailList.login
            favoriteUser?.imageUrl = detailList.avatarUrl
            viewModel.insert(favoriteUser as User)
            Toast.makeText(this, "Favorited", Toast.LENGTH_SHORT).show()
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = DetailViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory) [DetailViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean){
        binding?.detailProgressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_FAVORITE = "extra_favorite"
        @StringRes
        val GIT_TABS = intArrayOf(
            R.string.tabs_text_1,
            R.string.tabs_text_2
        )
    }
}
