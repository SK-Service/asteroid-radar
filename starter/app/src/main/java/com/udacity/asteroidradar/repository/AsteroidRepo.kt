package com.udacity.asteroidradar.repository

import android.util.Log
import com.udacity.asteroidradar.api.NasaService
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.util.getEndOfDateRange
import com.udacity.asteroidradar.util.getTodayDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher

class AsteroidRepo (private val database: AsteroidDatabase){

    suspend fun refreshAsteroidList () {
            withContext(Dispatchers.IO) {
                Log.i("AsteroidRepo", "inside  refreshAsteroidList")
                val startDate = getTodayDate()
                val endDate = getEndOfDateRange()
                val asteroidList = NasaService.nasaAsteroids.getNasaAsteroidList(startDate,endDate).await()
                Log.i("AsteroidRepo", "After call to NASA API")

                database.asteroidDao.insertAll(*asteroidList.asDatabaseModel())
                Log.i("AsteroidRepo", "After call to Database")

            }
    }

}