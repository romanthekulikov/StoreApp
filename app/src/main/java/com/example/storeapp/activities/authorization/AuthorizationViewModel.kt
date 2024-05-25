package com.example.storeapp.activities.authorization

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.usecases.InputDataCheckUseCase

class AuthorizationViewModel(
    private val inputDataCheckUseCase: InputDataCheckUseCase
) : ViewModel() {
    var isSignIn = MutableLiveData(false)
        private set
    var errorInputDataDetected = MutableLiveData(false)
        private set
    var email = MutableLiveData("")
        private set
    var password = MutableLiveData("")
        private set
    var confirmPassword = MutableLiveData("")
        private set

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