package com.example.githubuserfinder.ui.main

import android.util.Log
import androidx.lifecycle.*
import com.example.githubuserfinder.data.model.SearchResponse
import com.example.githubuserfinder.data.model.UserResponse
import com.example.githubuserfinder.data.api.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    private val _listUser = MutableLiveData<List<UserResponse>>()
    private val _error = MutableLiveData<Boolean>()

    val loading: LiveData<Boolean> = _isLoading
    val listUser: LiveData<List<UserResponse>> = _listUser
    val error: LiveData<Boolean> = _error

    init {
        detailUser()
    }

    fun searchUser(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchUser(query)
        client.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>,
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _listUser.value = response.body()?.items
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("ERROR", "Error On : ${t.message.toString()}111")
            }
        })
    }

    fun detailUser() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsersList()
        client.enqueue(object : Callback<List<UserResponse>> {
            override fun onResponse(
                call: Call<List<UserResponse>>,
                response: Response<List<UserResponse>>,
            ) {
                if (response.isSuccessful){
                    _isLoading.value = false
                    _listUser.value = response.body()
                }
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