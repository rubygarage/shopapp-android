package com.repository

import com.domain.entity.AccessData

interface SessionRepository {

    fun saveSession(accessData: AccessData)

    fun getSession(): AccessData?
}