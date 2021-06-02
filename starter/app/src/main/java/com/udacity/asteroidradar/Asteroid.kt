package com.udacity.asteroidradar

import android.os.Parcelable
import com.udacity.asteroidradar.database.AsteroidEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Asteroid(val id: Long,
                    val codename: String,
                    val closeApproachDate: String,
                    val absoluteMagnitude: Double,
                    val estimatedDiameter: Double,
                    val relativeVelocity: Double,
                    val distanceFromEarth: Double,
                    val isPotentiallyHazardous: Boolean) : Parcelable


fun List<Asteroid>.asDatabaseModel(): Array<AsteroidEntity> {
    return map {
        AsteroidEntity(id = it.id,
                codename = it.codename,
                closeApproachDate = it.closeApproachDate,
                absMagnitude = it.absoluteMagnitude,
                estDiameter = it.estimatedDiameter,
                relVelocity = it.relativeVelocity,
                missDistance = it.distanceFromEarth,
                isPotentiallyHazardous = it.isPotentiallyHazardous)
    }.toTypedArray()
}