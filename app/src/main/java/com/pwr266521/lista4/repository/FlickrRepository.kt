package com.pwr266521.lista4.repository

import com.pwr266521.lista4.service.FlickrApiService

class FlickrRepository(private val flickrApiService: FlickrApiService) {
    suspend fun getPublicPhotos() = flickrApiService.getPublicPhotos()
}