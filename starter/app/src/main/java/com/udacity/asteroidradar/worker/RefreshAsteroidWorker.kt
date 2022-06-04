package com.udacity.asteroidradar.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.repository.AsteroidRepo
import retrofit2.HttpException
import java.util.*

class RefreshAsteroidWorker(appContext: Context, params: WorkerParameters) :
        CoroutineWorker(appContext, params)  {

    companion object {
        const val WORK_NAME = "RefereshAsteroidDaily"
    }

    override suspend fun doWork(): Result {
        Log.i("RefreshAsteroidWorker", "Inside do Work")
        val database = AsteroidDatabase.getDatabase(applicationContext)
        //If the app is not restarted - the refresh can still update the cache
        Log.i("RefreshAsteroidWorker", "About to refresh Asteroids")
        AsteroidRepo(database).refreshAsteroidList()
        Log.i("RefreshAsteroidWorker", "After refresh of Asteroids")

        //If the app is not restarted - the refresh can still update the cache
        Log.i("RefreshAsteroidWorker", "About to refresh PicOfDay")
        AsteroidRepo(database).refreshPictureOfDay()
        Log.i("RefreshAsteroidWorker", "After refresh of PicOfDay")

        //Clean up the cache with no longer valid asteroids and picture of day
        AsteroidRepo(database).deleteOldAsteroids()
        Log.i("RefreshAsteroidWorker", "After delete of Old Asteroids")
        AsteroidRepo(database).deleteOldPicOfDay()
        Log.i("RefreshAsteroidWorker", "After delete of Old PicOfDay")

        Log.i("RefreshAsteroidWorker", "After Refresh: TimeStamp:<${Calendar.DATE}>")
        return try {
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }


}