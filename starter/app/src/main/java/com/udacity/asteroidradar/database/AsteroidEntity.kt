package com.udacity.asteroidradar.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.Asteroid

@Entity(tableName = "daily_asteroid_data")
data class AsteroidEntity(

        @PrimaryKey
        val id: Long,

        @ColumnInfo(name = "code_name")
        val codename: String,

        @ColumnInfo(name = "close_approach_date")
        val closeApproachDate: String,

        @ColumnInfo(name = "absolute_magnitude")
        val absMagnitude: Double,

        @ColumnInfo(name = "estimated_diameter")
        val estDiameter: Double,

        @ColumnInfo(name = "relative_velocity")
        val relVelocity: Double,

        @ColumnInfo(name = "miss_distance")
        val missDistance: Double,

        @ColumnInfo(name = "is_potentially_hazardous")
        val isPotentiallyHazardous: Boolean

)

fun List<AsteroidEntity>.asDomainModel(): List<Asteroid> {

    return map {
        Asteroid(id = it.id,
                codename = it.codename,
                closeApproachDate = it.closeApproachDate,
                absoluteMagnitude = it.absMagnitude,
                estimatedDiameter = it.estDiameter,
                relativeVelocity = it.relVelocity,
                distanceFromEarth = it.missDistance,
                isPotentiallyHazardous = it.isPotentiallyHazardous)
    }
}
