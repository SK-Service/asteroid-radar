package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.AsteroidDao
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDayDM
import com.udacity.asteroidradar.repository.AsteroidRepo
import kotlinx.coroutines.launch
import kotlin.Exception


enum class AsteroidApiStatus {LOADING, ERROR, DONE }

class MainViewModel (datasource: AsteroidDao, application: Application): AndroidViewModel(application) {

    private var _listOfAsteroid = MutableLiveData<MutableList<Asteroid>>(mutableListOf())
    val listOfAsteroid: LiveData<MutableList<Asteroid>>
        get() = _listOfAsteroid

    //Internal track of navigation to asteroid detail page
    private val _navigateToDetail = MutableLiveData<Asteroid?>()

    //To provide external access to the navigation to detail flag
    val navigateToDetail : MutableLiveData<Asteroid?>
        get() = _navigateToDetail

    //Internal track of NASA API Call
    private val _nasaApiCallStatus = MutableLiveData<AsteroidApiStatus>()
    //To provide external access to the NASA API call status
    val nasaApiCallStatus : LiveData<AsteroidApiStatus>
        get() = _nasaApiCallStatus

    //picture of day
    private var _picOfDayProperty = MutableLiveData<PictureOfDayDM>()
    val picOfDayProperty: LiveData<PictureOfDayDM>
        get() = _picOfDayProperty


    // init is called to populate the database or cache at the start, with the list of asteroids
    init {
        Log.i("MainViewModel", "inside init")
        viewModelScope.launch {

            _nasaApiCallStatus.value =AsteroidApiStatus.LOADING
            try {
                _nasaApiCallStatus.value = AsteroidApiStatus.DONE
                _listOfAsteroid.value = AsteroidRepo(AsteroidDatabase.getDatabase(application))
                    .refreshAsteroidList()
                    .toMutableList()
            } catch (e: Exception){
                Log.i("MainViewModel", "After AsteroidRepo call and inside catch exception")
                e.printStackTrace()
                _nasaApiCallStatus.value = AsteroidApiStatus.ERROR
            }

        }
        //Loading picture of the day
        viewModelScope.launch {
            try {
                Log.i("MainViewModel", "Before retrieving pic of day from DB")
                _picOfDayProperty.value = AsteroidRepo(AsteroidDatabase.getDatabase(application))
                    .refreshPictureOfDay()
            } catch (e: Exception){
                Log.i("MainViewModel", "After retrieving picOfDay from DB " +
                                        "and inside catch exception")
                e.printStackTrace()
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