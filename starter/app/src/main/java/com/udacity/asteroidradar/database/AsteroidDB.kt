package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

//The Data Access Object pattern - an interface to retrieve or insert data
@Dao
interface AsteroidDao {
    @Query ("select * from asteroidEntity where closeApproachDate >= CURRENT_TIMESTAMP")
    fun getAsteroidList() : List<AsteroidEntity>

    @Query("select * from asteroidentity where closeApproachDate " +
            "between :startDate and :endDate order by date(closeApproachDate) asc")
    fun getAsteroidListByFilter(startDate: String, endDate: String): List<AsteroidEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: AsteroidEntity)
}

@Dao
interface PicOfDayDao {
    @Query ("select * from pictureOfDayEntity")
    fun getPicOfDay() : PictureOfDayEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg pictureOfDayEntity: PictureOfDayEntity)
}

//A database to cache the asteroid information pulled using NASA API
@Database (entities = [AsteroidEntity::class], version = 1, exportSchema = false)
abstract class AsteroidDatabase: RoomDatabase() {
    abstract val asteroidDao:AsteroidDao
    abstract val picOfDayDao: PicOfDayDao


companion object {
    //@Volatile ensures that the INSTANCE value is never cached and it is pulled from main memory
    @Volatile
    //this will keep track of the database instance retrieved via getDatabase
    private lateinit var INSTANCE: AsteroidDatabase

    fun getDatabase(context: Context): AsteroidDatabase {
        synchronized(this) {
            if (!::INSTANCE.isInitialized) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AsteroidDatabase::class.java,
                    "asteroids"
                ).fallbackToDestructiveMigration()
                    .build()
            }
        }

        return INSTANCE
    }
}
}