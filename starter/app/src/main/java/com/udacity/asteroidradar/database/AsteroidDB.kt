package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

interface AsteroidDao {
    @Query ("select * from asteroidEntity where closeApproachDate >= CURRENT_TIMESTAMP")
    fun getAsteroidList() : LiveData<List<AsteroidEntity>>

    @Query("select * from asteroidentity where closeApproachDate " +
            "between :startDate and :endDate order by date(closeApproachDate) asc")
    fun getAsteroidListByFilter(startDate: String, endDate: String): LiveData<List<AsteroidEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: AsteroidEntity)
}

@Database (entities = [AsteroidEntity::class], version = 1)
abstract class AsteroidDatabase: RoomDatabase() {
    abstract val asteroidDao:AsteroidDao
}

private lateinit var INSTANCE: AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE =Room.databaseBuilder(context.applicationContext,
                                AsteroidDatabase::class.java,
                                "asteroids").build()
        }
    }

    return INSTANCE
}