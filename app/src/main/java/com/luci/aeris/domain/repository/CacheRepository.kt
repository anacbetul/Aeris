package com.yourapp.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPrefRepository private constructor(context: Context) {

    companion object {
        private const val PREF_NAME = "airis"
        private const val KEY_DARK_MODE = "key_dark_mode"
        private var instance: SharedPrefRepository? = null

        fun getInstance(context: Context): SharedPrefRepository {
            if (instance == null) {
                instance = SharedPrefRepository(context.applicationContext)
            }
            return instance!!
        }
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    // --- Save Methods ---
    fun saveString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun saveInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    fun saveBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    fun saveFloat(key: String, value: Float) {
        prefs.edit().putFloat(key, value).apply()
    }

    fun saveLong(key: String, value: Long) {
        prefs.edit().putLong(key, value).apply()
    }

    // --- Get Methods ---
    fun getString(key: String, default: String = ""): String {
        return prefs.getString(key, default) ?: default
    }

    fun getInt(key: String, default: Int = 0): Int {
        return prefs.getInt(key, default)
    }

    fun getBoolean(key: String, default: Boolean = false): Boolean {
        return prefs.getBoolean(key, default)
    }

    fun getFloat(key: String, default: Float = 0f): Float {
        return prefs.getFloat(key, default)
    }

    fun getLong(key: String, default: Long = 0L): Long {
        return prefs.getLong(key, default)
    }

    // --- Remove / Clear ---
    fun remove(key: String) {
        prefs.edit().remove(key).apply()
    }

    fun clearAll() {
        prefs.edit().clear().apply()
    }

    // Tema kaydetme
    fun setDarkModeEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_DARK_MODE, enabled).apply()
    }

    // Tema okuma
    fun isDarkModeEnabled(): Boolean {
        return prefs.getBoolean(KEY_DARK_MODE, false)
    }
}
