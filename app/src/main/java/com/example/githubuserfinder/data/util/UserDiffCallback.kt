package com.example.githubuserfinder.data.util

import androidx.recyclerview.widget.DiffUtil
import com.example.githubuserfinder.data.database.User

class UserDiffCallback(private val mOldList: List<User>, private val mNewList: List<User>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldList.size
    }

    override fun getNewListSize(): Int {
        return mNewList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldList[oldItemPosition].id == mNewList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUser = mOldList[oldItemPosition]
        val newUser = mNewList[newItemPosition]
        return oldUser.login == newUser.login && oldUser.imageUrl == newUser.imageUrl
    }
}