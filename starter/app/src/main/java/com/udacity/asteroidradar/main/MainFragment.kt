package com.udacity.asteroidradar.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Log.i("MainFragment", "inside onCreateView")
        val binding: FragmentMainBinding = DataBindingUtil.inflate(inflater,
                        R.layout.fragment_main, container, false)
        //Get hold of the application context
        val application = requireNotNull(this.activity).application
        //Get hold of the viewmodel with the application context
        val datasource = AsteroidDatabase.getDatabase(application).asteroidDao
        val viewModelFactory = MainViewModelFactory(datasource, application)
        val mainViewModel =ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        Log.i("MainFragment", "inside onCreateView: mainViewmodel:<$mainViewModel>")
        binding.viewModel = mainViewModel

        //Set this activty as the lifecycleowner
        binding.setLifecycleOwner(this)

        setHasOptionsMenu(true)

        Log.i("MainFragment", "inside onCreateView: Before Return")

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
