package com.udacity.asteroidradar.api

import com.squareup.moshi.Json
import com.udacity.asteroidradar.domain.PictureOfDayDM

data class PictureOfDayJson(
            @Json(name = "media_type")
            val mediaType: String,
            val title: String,
            val url: String)

fun PictureOfDayJson.toDomainModel() : PictureOfDayDM {
    return PictureOfDayDM(
        mediaType = this.mediaType,
        title = this.title,
        url = this.url
    )
}