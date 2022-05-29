package com.udacity.asteroidradar.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PictureOfDayDM(val mediaType:String,
                     val title: String,
                     val url:String ) : Parcelable
