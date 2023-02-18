package com.kuluruvineeth.data.core

import com.google.gson.Gson
import com.google.gson.GsonBuilder

object GsonFactory {

    fun create(): Gson = GsonBuilder()
        .setLenient()
        .create()
}