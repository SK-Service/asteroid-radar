package com.udacity.asteroidradar.main

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.repository.AsteroidRepo
import kotlinx.coroutines.launch
import java.lang.Exception
import java.time.LocalDate

enum class AsteroidApiStatus {LOADING, ERROR, DONE }

class MainViewModel (application: Application): AndroidViewModel(application) {

    //get hold of the database instance - by passing the application context
    private val database = getDatabase(application)
    private val asteroidRepo = AsteroidRepo(database)

//    //Internal lifecycle aware List of Asteroids
//    private val _listOfAsteroids = MutableLiveData<List<Asteroid>>()
    @RequiresApi(Build.VERSION_CODES.O)
    private val startDate = LocalDate.now()

    @RequiresApi(Build.VERSION_CODES.O)
    private val endDate = startDate.plusDays(7)

    //To provide external access to the list of asteroids
//    val listOfAsteroid: LiveData<List<Asteroid>> {Transformations.map(
//        database.asteroidDao.getAsteroidListByFilter(startDate.toString(), endDate.toString())) {
//        it.asDomainModel()
//    }}
//        get() = _listOfAsteroids

//    val listOfAsteroid: LiveData<List<Asteroid>> = database.asteroidDao.getAsteroidListByFilter(
//                    startDate.toString(),endDate.toString())

//    var _listOfAsteroid = MutableLiveData<List<Asteroid>>()
//    _listOfAsteroid.value = _listOfAsteroid.value?.plus(item) ?: listOf(item)
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    _listOfAsteroid.value = Transformations.map (database.asteroidDao.getAsteroidListByFilter(
//        startDate.toString(),endDate.toString())) {it.asDomainModel()}

    @RequiresApi(Build.VERSION_CODES.O)
    var listOfAsteroid: LiveData<List<Asteroid>> = Transformations.map (database.asteroidDao.getAsteroidListByFilter(
                      startDate.toString(),endDate.toString())) {it.asDomainModel()}
//        set(value) {
//            field = value
//            // For an extra challenge, update this to use the paging library.
//
//            // Notify any registered observers that the data set has changed. This will cause every
//            // element in our RecyclerView to be invalidated.
////            notifyDataSetChanged()
//        }

    //Internal track of navigation to asteroid detail page
    private val _navigateToDetail = MutableLiveData<Asteroid>()

    //To provide external access to the navigation to detail flag
    val navigateToDetail : LiveData<Asteroid>
        get() = _navigateToDetail

    //Internal track of NASA API Call
    private val _nasaApiCallStatus = MutableLiveData<AsteroidApiStatus>()

    //To provide external access to the NASA API call status

    val nasaApiCallStatus : LiveData<AsteroidApiStatus>
        get() = _nasaApiCallStatus

    // init is called to populate the database or cache with the list of asteroids
    init {
        viewModelScope.launch {
            asteroidRepo.refreshAsteroidList()
        }
    }

    private fun getListOfAsteroidsFromNasaApi() {
        viewModelScope.launch {
            _nasaApiCallStatus.value =AsteroidApiStatus.LOADING

             try {
                _nasaApiCallStatus.value = AsteroidApiStatus.DONE
             }catch (e: Exception) {
                _nasaApiCallStatus.value = AsteroidApiStatus.ERROR
             }
        }
    }

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToDetail.value = asteroid
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToDetail.value = null
    }

}