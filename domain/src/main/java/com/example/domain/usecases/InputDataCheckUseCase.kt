package com.example.domain.usecases

import android.util.Patterns

class InputDataCheckUseCase {
    fun checkEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun checkPassword(password: String): Boolean {
        return password.length >= 8
    }
}