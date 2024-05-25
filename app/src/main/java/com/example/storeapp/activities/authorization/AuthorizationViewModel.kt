package com.example.storeapp.activities.authorization

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.usecases.InputDataCheckUseCase

class AuthorizationViewModel(
    private val inputDataCheckUseCase: InputDataCheckUseCase
) : ViewModel() {
    val isSignIn = MutableLiveData(false)
    val errorInputDataDetected = MutableLiveData(false)
    val email = MutableLiveData("")
    val password = MutableLiveData("")
    val confirmPassword = MutableLiveData("")

    fun switchAuthorizationMode() {
        isSignIn.postValue(!isSignIn.value!!)
    }

    fun setEmail(typedEmail: String) {
        email.postValue(typedEmail)
    }

    fun setPassword(typedPassword: String) {
        password.postValue(typedPassword)
    }

    fun setPasswordConfirm(typedPasswordConfirm: String) {
        confirmPassword.postValue(typedPasswordConfirm)
    }

    fun checkData() {
        errorInputDataDetected.postValue(
            inputDataCheckUseCase.checkEmail(email.value!!) &&
                    inputDataCheckUseCase.checkPassword(password.value!!) && password.value == confirmPassword.value
        )
    }
}