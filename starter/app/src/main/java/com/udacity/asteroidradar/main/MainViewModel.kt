package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.PictureApi
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch

enum class AsteroidStatus { LOADING, ERROR, DONE }
enum class PictureOfDayStatus { LOADING, ERROR, DONE }

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AsteroidsDatabase.getInstance(application)
    private val asteroidsRepository = AsteroidsRepository(database)

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

    //The internal MutableLiveData for the pictureOfTheDay
    private val _pictureOfTheDay = MutableLiveData<PictureOfDay>()

    //The external immutable LiveDate for the pictureOfTheDay
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfTheDay

    // Internally, we use a MutableLiveData to handle navigation to the selected property
    private val _navigateToSelectedProperty = MutableLiveData<Asteroid>()

    // The external immutable LiveData for the navigation property
    val navigateToSelectedProperty: LiveData<Asteroid>
        get() = _navigateToSelectedProperty


    /**
     * Call getAsteroidProperties() on init so we can display status immediately.
     */
    init {
        getPictureOfTheDay()
        getAsteroids()
    }

    //list of asteroids
    val asteroids = asteroidsRepository.asteroids

    private fun getAsteroids() {
        viewModelScope.launch {
            try {
                asteroidsRepository.refreshAsteroids()
                _asteroidStatus.value = AsteroidStatus.DONE
                Log.d("MainViewModel", "retrieved the list of asteroids")

            } catch (e: Exception) {
                _asteroidStatus.value = AsteroidStatus.ERROR
                Log.d("MainViewModel", "Failed to retrieve the list of asteroids")
            }
        }
    }



    /**
     * Sets the value of the pictureOfDay LiveData to PictureOfDay object retrieved
     */


    private fun getPictureOfTheDay() {

        viewModelScope.launch {
            try {
                _pictureOfTheDay.value = PictureApi.retrofitService.getPictureOfTheDay(API_KEY)
                _pictureStatus.value = PictureOfDayStatus.DONE
                Log.d("MainviewModel", _pictureOfTheDay.value!!.mediaType)

            } catch (e: Exception) {
                _pictureStatus.value = PictureOfDayStatus.ERROR
            }

        }
    }


    /**
     * When the property is clicked, set the [_navigateToSelectedProperty] [MutableLiveData]
     * @param asteroid The [Asteroid] that was clicked on.
     */
    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedProperty.value = asteroid
    }

    /**
     * After the navigation has taken place, make sure navigateToSelectedProperty is set to null
     */
    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedProperty.value = null
    }


    /**
     * Factory for constructing MainViewModel with parameter
     */

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }

    }


}

