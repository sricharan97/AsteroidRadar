package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.launch
import org.json.JSONObject

enum class AsteroidStatus { LOADING, ERROR, DONE }
enum class PictureOfDayStatus { LOADING, ERROR, DONE }

class MainViewModel : ViewModel() {


    // The internal MutableLiveData that stores the status of the most recent asteroids request
    private val _asteroidStatus = MutableLiveData<AsteroidStatus>()

    // The external immutable LiveData for the request status
    val asteroidStatus: LiveData<AsteroidStatus>
        get() = _asteroidStatus

    // The internal MutableLiveData that stores the status of the most recent picture of the day request
    private val _pictureStatus = MutableLiveData<PictureOfDayStatus>()

    // The external immutable LiveData for the request status
    val pictureStatus: LiveData<PictureOfDayStatus>
        get() = _pictureStatus

    // The internal MutableLiveData String that stores the most recent response
    private val _asteroids = MutableLiveData<List<Asteroid>>()

    // The external immutable LiveData for the response String
    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids


    //The internal MutableLiveData for the pictureOfTheDay
    private val _pictureOfTheDay = MutableLiveData<PictureOfDay>()

    //The external immutable LiveDate for the pictureOfTheDay
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfTheDay


    /**
     * Call getAsteroidProperties() on init so we can display status immediately.
     */
    init {
        getAsteroidProperties()
    }


    /**
     * Sets the value of the asteroids LiveData to the list of asteroids retrieved
     */

    private fun getAsteroidProperties() {

        viewModelScope.launch {
            try {
                _asteroids.value = parseAsteroidsJsonResult(
                    JSONObject(
                        NasaApi.retrofitService
                            .getAsteroids(
                                "2021-01-01",
                                "2021-01-05",
                                API_KEY
                            )
                    )
                )
                _asteroidStatus.value = AsteroidStatus.DONE

            } catch (e: Exception) {
                _asteroidStatus.value = AsteroidStatus.ERROR
                _asteroids.value = ArrayList()
            }
        }


    }

    /**
     * Sets the value of the pictureOfDay LiveData to PictureOfDay object retrieved
     */


    private fun getPictureOfTheDay() {

        viewModelScope.launch {
            try {
                _pictureOfTheDay.value = NasaApi.retrofitService.getPictureOfTheDay(API_KEY)
                _pictureStatus.value = PictureOfDayStatus.DONE
            } catch (e: Exception) {
                _pictureStatus.value = PictureOfDayStatus.ERROR
            }
        }
    }


}