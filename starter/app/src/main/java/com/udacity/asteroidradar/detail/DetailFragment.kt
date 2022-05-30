package com.udacity.asteroidradar.detail


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentDetailBinding
import com.udacity.asteroidradar.main.MainViewModel
import com.udacity.asteroidradar.main.MainViewModelFactory

class DetailFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val asteroid = DetailFragmentArgs.fromBundle(requireArguments()).selectedAsteroid
        val application = requireNotNull(this.activity).application
        val viewModelFactory = DetailViewModelFactory(asteroid, application)
        val detailViewModel = ViewModelProvider(this, viewModelFactory).get(DetailViewModel::class.java)

        binding.detailViewModel = detailViewModel

//        binding.helpButton.setOnClickListener {
//            displayAstronomicalUnitExplanationDialog()
//        }

        return binding.root
    }

//    private fun displayAstronomicalUnitExplanationDialog() {
//        val builder = AlertDialog.Builder(activity!!)
//            .setMessage(getString(R.string.astronomica_unit_explanation))
//            .setPositiveButton(android.R.string.ok, null)
//        builder.create().show()
//    }
}
