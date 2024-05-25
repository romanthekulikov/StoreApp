package com.example.storeapp.activities.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domain.usecases.InputDataCheckUseCase

@Suppress("UNCHECKED_CAST")
class AuthorizationViewModelFactory : ViewModelProvider.Factory {
    private val inputDataCheckUseCase = InputDataCheckUseCase()
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthorizationViewModel(inputDataCheckUseCase) as T
    }
}