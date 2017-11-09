package com.repository.impl

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.domain.entity.AccessData
import com.repository.SessionRepository

class SessionRepositoryImpl(context: Context) : SessionRepository {

    companion object {
        private const val EMAIL = "email"
        private const val ACCESS_TOKEN = "access_token"
        private const val EXPIRES_DATE = "expires_date"
    }

    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    override fun saveSession(accessData: AccessData) {
        preferences.edit()
                .putString(EMAIL, accessData.email)
                .putString(ACCESS_TOKEN, accessData.accessToken)
                .putLong(EXPIRES_DATE, accessData.expiresAt)
                .apply()
    }

    override fun getSession(): AccessData? {
        val email = preferences.getString(EMAIL, null)
        val accessToken = preferences.getString(ACCESS_TOKEN, null)
        val expiresDate = preferences.getLong(EXPIRES_DATE, 0)
        return if (email != null && accessToken != null) {
            AccessData(
                    email,
                    accessToken,
                    expiresDate
            )
        } else {
            null
        }
    }
}