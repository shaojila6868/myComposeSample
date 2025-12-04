package com.kir.mycomposesample.labratory.hiltlib

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class HiltViewModel @Inject constructor(
    private val repository: UserRepository,
    private val logger: Logger
) : ViewModel() {

    fun loadUserName(): String {
        return repository.getUserName()
    }

    fun log(message: String) {
        logger.log(message)
    }
}