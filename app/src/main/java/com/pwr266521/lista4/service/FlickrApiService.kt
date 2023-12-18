package com.pwr266521.lista4.service

import com.pwr266521.lista4.model.FlickrResponse
import retrofit2.http.GET

interface FlickrApiService {
    @GET("services/feeds/photos_public.gne?format=json&nojsoncallback=1")
    suspend fun getPublicPhotos(): FlickrResponse
}