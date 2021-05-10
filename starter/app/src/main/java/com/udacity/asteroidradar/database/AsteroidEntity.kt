package com.udacity.asteroidradar.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_asteroid_data")
data class AsteroidEntity(

      @PrimaryKey
      val id: Long,

      @ColumnInfo(name = "absolute_magnitude")
      val absMagnitude: Double,

      @ColumnInfo(name = "estimated_diameter_max")
      val estDiameterMax: Double,

      @ColumnInfo(name = "is_potentially_hazardous")
      val isPotentiallyHazardous: Boolean,

      @ColumnInfo(name = "relative_velocity")
      val relVelocity: Double,

      @ColumnInfo(name = "miss_distance")
      val missDistance: Double,

      @ColumnInfo(name = "epoch_approach_date")
      val epochDateApproach: Long

)
