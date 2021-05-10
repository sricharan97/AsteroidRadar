package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao {

    //for testing the Room database
    @Insert
    fun insertAsteroid(asteroidEntity: AsteroidEntity)

    //for testing the room database
    @Query("select * from daily_asteroid_data where id= :key")
    fun getAsteroid(key: Long): AsteroidEntity?

    //Fetch all the asteroids from the database
    @Query("select * from daily_asteroid_data order by epoch_approach_date")
    fun getAsteroids(): LiveData<List<AsteroidEntity>>

    //implement cache
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroidEntities: AsteroidEntity)

}


@Database(entities = [AsteroidEntity::class], version = 1, exportSchema = false)
abstract class AsteroidsDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao

    companion object {

        @Volatile
        private var INSTANCE: AsteroidsDatabase? = null

        fun getInstance(context: Context): AsteroidsDatabase {

            synchronized(this) {

                var instance = INSTANCE

                if (instance == null) {

                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AsteroidsDatabase::class.java,
                        "asteroids"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }


        }
    }

}