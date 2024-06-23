package com.example.lyka_findmeaning.repository

import android.util.Log
import com.example.lyka_findmeaning.Getmeaningcallback
import com.example.lyka_findmeaning.data.word
import com.example.lyka_findmeaning.network.Apiservice
import com.example.lyka_findmeaning.network.Retrofitinstance
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Response
import java.lang.reflect.Type


class Getmeaningrepository {
    val apiservice = Retrofitinstance.retrofit.create(Apiservice::class.java)

    fun getMeaning(word: String, callbackl: Getmeaningcallback): word? {
        var result: word? = null
        val call: Call<List<word>> = apiservice.getMeaning(word)
        call.enqueue(object : retrofit2.Callback<List<word>>  {
            override fun onResponse(call: Call<List<word>> , response: Response<List<word>> ) {
                if (response.isSuccessful) {
                    result =response.body()?.get(0)
                    callbackl.onSuccess(result)
                }else{
                    callbackl.onError("error")
                }
            }

            override fun onFailure(call: Call<List<word>> , t: Throwable) {
                Log.e("#", t.message.toString())
                callbackl.onError("error")
            }
        })
        return result
    }
}
