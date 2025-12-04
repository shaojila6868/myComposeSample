package com.kir.mycomposesample.labratory.hiltlib

import jakarta.inject.Inject

class UserRepository @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
) {
    fun getUserName(): String {
        return userRemoteDataSource.fetchUserData()
    }
}

class UserRemoteDataSource @Inject constructor() {
    fun fetchUserData(): String {
        return "행복한 애아빠"
    }
}