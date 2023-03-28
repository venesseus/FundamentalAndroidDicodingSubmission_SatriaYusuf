package com.example.githubuserfinder.ui.follow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserfinder.data.model.UserResponse
import com.example.githubuserfinder.data.api.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    private val _error = MutableLiveData<Boolean>()
    private val _listFollow = MutableLiveData<List<UserResponse>>()

    val loading : LiveData<Boolean> = _isLoading
    val error : LiveData<Boolean> = _error
    val listFollow : LiveData<List<UserResponse>> = _listFollow

    fun getUserFollower(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowers(username)
        client.enqueue(object : Callback<List<UserResponse>>{
            override fun onResponse(
                call: Call<List<UserResponse>>,
                response: Response<List<UserResponse>>,
            ) {
                _isLoading.value = false
                _listFollow.value = response.body()
            }

            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                _isLoading.value = false
                _error.value = true
            }

        })
    }

    fun getUserFollowing(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowing(username)
        client.enqueue(object : Callback<List<UserResponse>> {
            override fun onResponse(
                call: Call<List<UserResponse>>,
                response: Response<List<UserResponse>>,
            ) {
                _isLoading.value = false
                _listFollow.value = response.body()
            }
            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                _isLoading.value = false
                _error.value = true
            }
        })
    }

    fun doneToastError(){
        _error.value = false
    }
}