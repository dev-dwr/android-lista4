package com.pwr266521.lista4.repository

import com.pwr266521.lista4.model.FlickrResponse
import com.pwr266521.lista4.service.FlickrApiService
import java.lang.RuntimeException

class FlickrRepository(private val flickrApiService: FlickrApiService) {
    suspend fun getPublicPhotos(): FlickrResponse {
        var response: FlickrResponse? = null
        try {
            response = flickrApiService.getPublicPhotos()
        } catch (e: Exception) {
            throw RuntimeException(e.message)
        }
        return response
    }
}