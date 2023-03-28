package com.example.githubuserfinder.ui.favorite

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
import com.example.githubuserfinder.ui.follow.FollowAdapter
import com.example.githubuserfinder.ui.follow.FollowFragment
import com.example.githubuserfinder.ui.follow.FollowViewModel
import com.example.githubuserfinder.ui.main.MainActivity

class FavoriteFollowFragment : Fragment() {

    companion object {
        const val GIT_TABS = "GIT_TABS"
        const val GIT_FOLLOWER = "Followers"
        const val GIT_FOLLOWING = "Following"
    }

    private lateinit var binding: FollowFragmentBinding
    private lateinit var viewModel: FollowViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FollowFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[FollowViewModel::class.java]

        val favUser : UserResponse = requireActivity().intent.getParcelableExtra(DetailActivity.EXTRA_FAVORITE)!!
        binding.rvFollow.layoutManager = LinearLayoutManager(activity)
        val gitTab = arguments?. getString(GIT_TABS)
        if (gitTab == GIT_FOLLOWER) {
            favUser.login?.let { viewModel.getUserFollower(it)}
        } else if (gitTab == GIT_FOLLOWING){
            favUser.login?.let { viewModel.getUserFollowing(it) }
        }

        viewModel.loading.observe(viewLifecycleOwner){
            showLoading(it)
        }

        viewModel.listFollow.observe(viewLifecycleOwner){
            val adapter = FollowAdapter(it)
            binding.apply {
                rvFollow.adapter = adapter
            }
        }

        viewModel.error.observe(viewLifecycleOwner){
            Toast.makeText(context, "Data Not Found", Toast.LENGTH_SHORT).show()
            viewModel.doneToastError()
        }
        return binding.root
    }

    private fun showLoading(isLoading : Boolean) {
        if (isLoading) {
            binding.loadingFollow.visibility = View.VISIBLE
        } else {
            binding.loadingFollow.visibility = View.GONE
        }
    }
}