package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.database.AsteroidEntity


data class NetworkAsteroid(val id: Long,
                           val codename: String,
                           val closeApproachDate: String,
                           val absoluteMagnitude: Double,
                           val estimatedDiameter: Double,
                           val relativeVelocity: Double,
                           val distanceFromEarth: Double,
                           val isPotentiallyHazardous: Boolean)


fun List<NetworkAsteroid>.asDatabaseModel(): Array<AsteroidEntity> {
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