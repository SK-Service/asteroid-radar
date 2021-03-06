package com.udacity.asteroidradar.repository

import android.util.Log
import com.udacity.asteroidradar.api.NasaService
import com.udacity.asteroidradar.api.PictureOfDayJson
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.api.toDomainModel
import com.udacity.asteroidradar.database.*
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDayDM
import com.udacity.asteroidradar.util.getEndOfDateRange
import com.udacity.asteroidradar.util.getTodayDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import java.lang.Exception

class AsteroidRepo (private val database: AsteroidDatabase){

    private lateinit var asteroidListDB: List<AsteroidEntity>
    private lateinit var picOfDayDB: PictureOfDayEntity

    suspend fun refreshAsteroidList (): List<Asteroid> {
            Log.i("AsteroidRepo", "inside refreshAsteroidList")
            withContext(Dispatchers.IO) {
                Log.i("AsteroidRepo", "inside  the refreshAsteroidList-Dispatcher.IO")
                val startTodayDate = getTodayDate()
                val dateAfterNext7Days = getEndOfDateRange()

                try {
                    Log.i("AsteroidRepo", "Before call to NASA API")
                    val asteroidListNasa = NasaService.nasaAsteroids.getNasaAsteroidList(startTodayDate,
                        dateAfterNext7Days)
                    Log.i("AsteroidRepo", "After call to NASA API: number of asteroids:" +
                            "                               <${asteroidListNasa.length}>")
                    Log.i("AsteroidRepo", "Before parsing the JSON String")
                    val asteroidListNasa_arraylist = parseAsteroidsJsonResult(JSONObject(asteroidListNasa))
                    Log.i("AsteroidRepo", "After parsing the JSON String-" +
                            "Number of Asteroids:<${asteroidListNasa_arraylist.size}>")

                    Log.i("AsteroidRepo", "Before call to insertAll DB")
                    database.asteroidDao.insertAll(*asteroidListNasa_arraylist.toDatabaseModel())
                    Log.i("AsteroidRepo", "After call to Database for Insert-All")
                } catch (e: Exception) {
                    //HTTP Exception are not reflected back to the caller
                        // Any other system error the caller should handle
                    e.printStackTrace()
                }

                Log.i("AsteroidRepo", "Before call to fetch from DB")
                asteroidListDB = database.asteroidDao.getAsteroidListByFilter(
                                                                startTodayDate,dateAfterNext7Days)
                Log.i("AsteroidRepo", "After fetch from DB:<${asteroidListDB.size}>")
            }
        return asteroidListDB.asDomainModel()
    }

    //function retrieve picture of day from NASA
    suspend fun refreshPictureOfDay (): PictureOfDayDM {
        Log.i("AsteroidRepo", "inside refreshPictureOfDay")
        withContext(Dispatchers.IO) {
            Log.i("AsteroidRepo", "inside  the refreshPictureOfDay-Dispatcher.IO")

            try {
                Log.i("AsteroidRepo", "Before call to NASA PictureOfDay API")
                val picOfDayNasa = NasaService.picOfDayService.getNasaPicOfDay()


                Log.i("AsteroidRepo", "Before call to insertAll DB with pic of day info")

                Log.i("AsteroidRepo", "Title:<${picOfDayNasa.title}>, " +
                        "URL<${picOfDayNasa.url}>")
                var picOfDayEntity = picOfDayNasa.toEntityModel()
                picOfDayEntity.date = getTodayDate()

                Log.i("AsteroidRepo", "Before call to insertAll DB with pic of day info" +
                        "title:<${picOfDayEntity.title}>, Date:<${picOfDayEntity.date}>")

                database.picOfDayDao.insertAll(picOfDayEntity)

                Log.i("AsteroidRepo", "After call to insert Database for pic of day info")

            } catch (e: Exception) {
                //HTTP Exception are not reflected back to the caller
                // Any other system error the caller should handle
                e.printStackTrace()
            }

            Log.i("AsteroidRepo", "Before call to fetch from DB")
            picOfDayDB = database.picOfDayDao.getPicOfDay()

            Log.i("AsteroidRepo", "After fetch picture from DB")
        }
        return picOfDayDB.toDomainModel()
    }

    suspend fun deleteOldPicOfDay() {
        withContext(Dispatchers.IO) {
            database.picOfDayDao.removeOldPicOfDay(getTodayDate())
        }

    }

    suspend fun deleteOldAsteroids() {
        withContext(Dispatchers.IO) {
            database.asteroidDao.removeOldAsteroids(getTodayDate())
        }

    }
}