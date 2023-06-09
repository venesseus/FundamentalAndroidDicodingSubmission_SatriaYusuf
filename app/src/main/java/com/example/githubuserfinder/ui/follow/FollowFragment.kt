package com.example.githubuserfinder.ui.follow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserfinder.data.model.UserResponse
import com.example.githubuserfinder.databinding.FollowFragmentBinding
import com.example.githubuserfinder.ui.detail.DetailActivity
import com.example.githubuserfinder.ui.main.MainActivity

class FollowFragment : Fragment() {

    companion object {
        const val TAB_TITLES = "tab_titles"
        const val GIT_FOLLOWER = "Followers"
        const val GIT_FOLLOWING = "Following"
    }

    private lateinit var binding: FollowFragmentBinding
    private lateinit var viewModel: FollowViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FollowFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[FollowViewModel::class.java]

        //DetailActivity Follow
        val userTab = requireActivity().intent.getStringExtra(MainActivity.EXTRA_DATA)

        binding.rvFollow.layoutManager = LinearLayoutManager(activity)
        val detailTab = arguments?.getString(TAB_TITLES)
        if (detailTab  == GIT_FOLLOWER) {
            userTab?.let { viewModel.getUserFollower(it) }
        } else if (detailTab  == GIT_FOLLOWING) {
            userTab?.let { viewModel.getUserFollowing(it) }
        }

        //DetailFavorite Follow
        val userFollow = requireActivity().intent.getStringExtra(DetailActivity.EXTRA_FAVORITE)

        binding.rvFollow.layoutManager = LinearLayoutManager(activity)
        val favTab = arguments?.getString(TAB_TITLES)
        if (favTab == GIT_FOLLOWER) {
            userFollow?.let { viewModel.getUserFollower(it) }
        } else if (favTab == GIT_FOLLOWING) {
            userFollow?.let { viewModel.getUserFollowing(it) }
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.listFollow.observe(viewLifecycleOwner) {
            val adapter = FollowAdapter(it)
            binding.apply {
                rvFollow.adapter = adapter
            }
        }

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(context, "Data Not Found", Toast.LENGTH_SHORT).show()
            viewModel.doneToastError()
        }
        return binding.root
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingFollow.visibility = View.VISIBLE
        } else {
            binding.loadingFollow.visibility = View.GONE
        }
    }
}