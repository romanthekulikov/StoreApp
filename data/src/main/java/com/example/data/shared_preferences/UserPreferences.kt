package com.example.data.shared_preferences

import android.content.Context

const val PREFERENCES_NAME = "user_pref"
const val KEY_LOGGED_IN = "logged_in"
class UserPreferences(context: Context) {
    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    private val editor = preferences.edit()

    fun saveLogin(loggedIn: Boolean) {
        editor.putBoolean(KEY_LOGGED_IN, loggedIn)
        editor.apply()
    }

    fun loggedIn(): Boolean {
        return preferences.getBoolean(KEY_LOGGED_IN, false)
    }
}