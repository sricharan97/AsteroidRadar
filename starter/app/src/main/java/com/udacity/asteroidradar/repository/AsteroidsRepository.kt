package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.main.MenuItemFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidsRepository(private val database: AsteroidsDatabase) {

    /**
     * return the appropriate LiveData of asteroids based on the menu item filter
     */
    fun getMenuItemSelection(filter: MenuItemFilter): LiveData<List<Asteroid>> {
        return when (filter) {

            (MenuItemFilter.SAVED) -> {
                Transformations.map(database.asteroidDao.getSavedAsteroids()) {
                    it.asDomainModel()
                }
            }

            (MenuItemFilter.TODAY) -> {
                Transformations.map(database.asteroidDao.getTodayAsteroids()) {
                    it.asDomainModel()
                }
            }

            else -> {
                Transformations.map(database.asteroidDao.getWeekAsteroids()) {
                    it.asDomainModel()
                }
            }
        }
    }


    /**
     * Refresh the asteroids stored in the offline cache.
     *
     * This function uses the IO dispatcher to ensure the database insert operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
     * function is now safe to call from any thread including the Main thread.
     *
     * To actually load the asteroids for use
     */

    suspend fun refreshAsteroids() {

        withContext(Dispatchers.IO) {

            var jsonString = ""

            try {
                jsonString = NasaApi.retrofitService.getAsteroids(
                        Constants.API_KEY)

            } catch (e: Exception) {

                Log.d("Repository", "Failed in network call to API ")

            }
            try {

                val networkaAsteroidList = parseAsteroidsJsonResult(JSONObject(jsonString))
                val asteroidList = networkaAsteroidList.asDatabaseModel()
                database.asteroidDao.insertAll(*asteroidList)

            } catch (e: Exception) {
                Log.d("Repository", "Failed in parsing the JSONResult")

            }

        }

    }
}