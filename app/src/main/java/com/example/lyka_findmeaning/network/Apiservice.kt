package com.example.lyka_findmeaning.network

import com.example.lyka_findmeaning.data.word
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Apiservice {

    @GET("{word}")
     fun getMeaning(@Path("word") word:String): Call<List<word>>

}