package com.example.githubuserfinder.ui.detail

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserfinder.data.database.User
import com.example.githubuserfinder.data.model.UserResponse
import com.example.githubuserfinder.data.util.UserRepository
import com.example.githubuserfinder.data.api.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    private val _listUser = MutableLiveData<UserResponse>()
    private val _error = MutableLiveData<Boolean>()

    val loading : LiveData<Boolean> = _isLoading
    val listUser : LiveData<UserResponse> = _listUser
    val error : LiveData<Boolean> = _error

    private val mUserRepository: UserRepository =
        UserRepository(application)

    fun insert(user: User){
        mUserRepository.insert(user)
    }

    fun delete(id: Int){
        mUserRepository.delete(id)
    }

    fun getFavorite():LiveData<List<User>> =
        mUserRepository.getAllUsers()

    fun getDetailUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>,
            ) {
                if (response.isSuccessful){
                    _isLoading.value = false
                    _listUser.value = response.body()
                }
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                _error.value = true
            }
        })
    }

    fun doneToastError(){
        _error.value = false
    }
}