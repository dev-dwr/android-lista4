package com.pwr266521.lista4.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwr266521.lista4.model.FlickrResponse
import com.pwr266521.lista4.repository.FlickrRepository
import kotlinx.coroutines.launch

class FlickrViewModel(private val repository: FlickrRepository) : ViewModel() {
    var photos = mutableStateOf<FlickrResponse?>(null)
    var isLoading = mutableStateOf(false)

    init {
        loadPhotos()
    }

    private fun loadPhotos() {
        viewModelScope.launch {
            isLoading.value = true
            photos.value = repository.getPublicPhotos()
            isLoading.value = false
        }
    }
}