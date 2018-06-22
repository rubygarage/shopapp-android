package com.shopapp

import android.content.Context
import com.shopapp.gateway.Api

interface ApiInitializer {

    fun initialize(context: Context): Api?
}