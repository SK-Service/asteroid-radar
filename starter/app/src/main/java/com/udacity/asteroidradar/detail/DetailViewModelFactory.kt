package com.udacity.asteroidradar.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.database.AsteroidDao
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.main.MainViewModel
import java.lang.IllegalArgumentException

class DetailViewModelFactory (private val asteroid: Asteroid,
        private val application:Application) : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel (asteroid, application) as T
        }
        throw IllegalArgumentException ("Unknown ViewModel class")
    }

}

class MainViewModelFactory (
    private val datasource: AsteroidDao,
    private val application:Application) : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel (datasource, application) as T
        }
        throw IllegalArgumentException ("Unknown ViewModel class")
    }

}