package com.example.lyka_findmeaning.network

import com.example.lyka_findmeaning.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Retrofitinstance {
    private val baseurl:String=Constants.retrofit_base_url
    val retrofit:Retrofit by lazy{
        Retrofit.Builder()
            .baseUrl(baseurl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}