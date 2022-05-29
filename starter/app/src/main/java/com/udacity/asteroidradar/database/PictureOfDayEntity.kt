package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.api.PictureOfDayJson
import com.udacity.asteroidradar.domain.PictureOfDayDM

@Entity
data class PictureOfDayEntity  (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val mediaType: String,
    val title: String,
    val url: String
        )

fun PictureOfDayEntity.toDomainModel() : PictureOfDayDM {
    return PictureOfDayDM (
        mediaType = this.mediaType,
        title = this.title,
        url = this.url)
}

fun PictureOfDayJson.toEntityModel() : PictureOfDayEntity {
     return PictureOfDayEntity (
         mediaType = this.mediaType,
         title = this.title,
         url = this.url)
}


