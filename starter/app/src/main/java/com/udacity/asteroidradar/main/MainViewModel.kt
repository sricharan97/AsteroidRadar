package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch

enum class MenuItemFilter { SAVED, WEEK, TODAY }

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AsteroidsDatabase.getInstance(application)
    private val asteroidsRepository = AsteroidsRepository(database)

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

    //This list will be observed in the RecyclerView
    private val _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids


    //Observer logic
    private val asteroidListObserver = Observer<List<Asteroid>> {
        //Update new list to RecyclerView
        _asteroids.value = it
    }

    private var asteroidListLiveData: LiveData<List<Asteroid>>



    init {
        asteroidListLiveData = asteroidsRepository.getMenuItemSelection(MenuItemFilter.WEEK)
        asteroidListLiveData.observeForever(asteroidListObserver)
        getPictureOfTheDay()
        getAsteroids()
    }


    private fun getAsteroids() {
        viewModelScope.launch {
            try {
                asteroidsRepository.refreshAsteroids()
            } catch (e: Exception) {
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
                _pictureOfTheDay.value = NasaApi.retrofitService.getPictureOfTheDay(API_KEY)

            } catch (e: Exception) {
                Log.d("MainViewModel", "Failed to retrieve the picture of day")
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
     * function to modify the [asteroids] liveDate to reflect the menu
     * item selection
     */

    fun updateFilter(filter: MenuItemFilter) {
        //Observe the new filtered LiveData
        asteroidListLiveData =
                asteroidsRepository.getMenuItemSelection(filter)
        asteroidListLiveData.observeForever(asteroidListObserver)
    }

    override fun onCleared() {
        super.onCleared()
        //Clear observers
        asteroidListLiveData.removeObserver(asteroidListObserver)
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

