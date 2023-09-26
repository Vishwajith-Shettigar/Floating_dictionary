package com.example.lyka_findmeaning.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lyka_findmeaning.data.word
import com.example.lyka_findmeaning.network.Apiservice
import com.example.lyka_findmeaning.network.Retrofitinstance
import com.example.lyka_findmeaning.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Getmeaningviewmodel : ViewModel() {



    fun fetchMeaning(word: String) {
        viewModelScope.launch {

        }
        viewModelScope.launch {

        }
    }

}