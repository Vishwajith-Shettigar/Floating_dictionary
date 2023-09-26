package com.example.lyka_findmeaning

import com.example.lyka_findmeaning.data.word

interface Getmeaningcallback {

    fun onSuccess(word: word?)
    fun onError(errorMessage: String)
}