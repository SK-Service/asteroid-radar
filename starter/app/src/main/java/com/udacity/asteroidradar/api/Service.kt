package com.udacity.asteroidradar.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface AsteroidService{
    @GET("neo/rest/v1/feed")
    suspend fun getNasaAsteroidList(@Query("start_date") startDate: String,
                            @Query("end_date") endDate:String,
                            @Query("API_KEY") apiKey: String = "SitZzqlkTck1infdlYNYPFtgbReI51ddib6Hedyu") : String

}

interface PicOfDayService{
    @GET("nplanetary/apod")
    suspend fun getNasaPicOfDay(
                @Query("API_KEY") apiKey: String =
                    "SitZzqlkTck1infdlYNYPFtgbReI51ddib6Hedyu") : PictureOfDayJson

}

//build Moshi object that Retrofit is gong to use
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

//Service to invoke
object NasaService {
        //Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl(Constants.BASE_URL)
            .build()

    val nasaAsteroids = retrofit.create(AsteroidService::class.java)
    val picOfDayService = retrofit.create(PicOfDayService::class.java)
}

