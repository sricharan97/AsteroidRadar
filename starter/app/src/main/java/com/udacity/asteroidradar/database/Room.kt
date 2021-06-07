package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao {


    //Fetch this week's asteroids from the database
    @Query("select * from daily_asteroid_data where date(close_approach_date) >= date('now')" +
            " order by date(close_approach_date)")
    fun getWeekAsteroids(): LiveData<List<AsteroidEntity>>


    //Fetch all the asteroids saved to the database
    @Query("select * from daily_asteroid_data order by date(close_approach_date)")
    fun getSavedAsteroids(): LiveData<List<AsteroidEntity>>

    //Fetch only today's asteroids
    @Query("select * from daily_asteroid_data where date(close_approach_date) = date('now') ")
    fun getTodayAsteroids(): LiveData<List<AsteroidEntity>>


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