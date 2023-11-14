package com.example.opsctask1screens

import android.content.Context

object TokenManager {
    private const val PREF_NAME = "auth_pref"
    private const val AUTH_TOKEN = "auth_token"
    private const val DEFAULT_FRAGMENT = "default_fragment"
    private const val HAS_LOGGED_IN_BEFORE = "has_logged_in_before"

    fun saveAuthToken(context: Context, token: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(AUTH_TOKEN, token).apply()
    }

    fun getAuthToken(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(AUTH_TOKEN, null)
    }

    fun saveDefaultFragment(context: Context, defaultFragment: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(DEFAULT_FRAGMENT, defaultFragment).apply()
    }

    fun getDefaultFragment(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(DEFAULT_FRAGMENT, null)
    }

    fun setLoggedInBefore(context: Context, hasLoggedInBefore: Boolean) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(HAS_LOGGED_IN_BEFORE, hasLoggedInBefore).apply()
    }

    fun hasLoggedInBefore(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(HAS_LOGGED_IN_BEFORE, false)
    }

    fun clearAuthToken(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().remove(AUTH_TOKEN).apply()
    }
}
