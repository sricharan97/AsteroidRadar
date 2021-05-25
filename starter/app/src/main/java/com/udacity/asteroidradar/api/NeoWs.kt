package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Build the Moshi object that Retrofit will be using to parse the picture of
 * the day, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


/**Create a retrofit object
 * Retrofit needs at least two things available to it to build a web services API:
 * the base URI for the web service, and a converter factory
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constants.BASE_URL)
    .build()


interface NasaApiService {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
            @Query("start_date") startDate: String,
            @Query("end_date") endDate: String,
            @Query("api_key") apiKey: String
    )
            : String

    @GET("/planetary/apod")
    suspend fun getPictureOfTheDay(@Query("api_key") apiKey: String)
            : PictureOfDay
}

object NasaApi {
    val retrofitService: NasaApiService by lazy {
        retrofit.create(NasaApiService::class.java)
    }
}