package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this, MainViewModel.Factory(activity.application)).get(MainViewModel::class.java)
    }

    private var recyclerAdapter: AsteroidListAdapter? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        // set the adapter for the asteroids recyclerview
        recyclerAdapter = AsteroidListAdapter(AsteroidListAdapter.OnclickListener {
            viewModel.displayAsteroidDetails(it)
        })

        binding.asteroidRecycler.adapter = recyclerAdapter

        // Observe the navigateToSelectedProperty LiveData and Navigate when it isn't null
        // After navigating, call displayAsteroidDetailsComplete() so that the ViewModel is ready
        // for another navigation event.

        viewModel.navigateToSelectedProperty.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                // Must find the NavController from the Fragment
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                // Tell the ViewModel we've made the navigate call to prevent multiple navigation
                viewModel.displayAsteroidDetailsComplete()

            }
        })

        viewModel.asteroidsSavedChange.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                setRecyclerViewList(viewModel.asteroidsSaved)
                viewModel.setSavedAsteroidsChangeCompleted()
            }
        })

        viewModel.asteroidsWeekChange.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                setRecyclerViewList(viewModel.asteroids)
                viewModel.setWeekAsteroidsChangeCompleted()
            }
        })

        viewModel.asteroidsTodayChange.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                setRecyclerViewList(viewModel.asteroidsToday)
                viewModel.setTodayAsteroidsChangeCompleted()
            }
        })

        /**
         * Any LiveData value will always be null unless you observe it
         * so creating an observers on asteroidsToday and asteroidsSaved
         * to return non-null list
         */

        viewModel.asteroidsToday.observe(viewLifecycleOwner, Observer {
            setRecyclerViewList(viewModel.asteroidsToday)

        })

        viewModel.asteroidsSaved.observe(viewLifecycleOwner, Observer {
            setRecyclerViewList(viewModel.asteroidsSaved)
        })


        setHasOptionsMenu(true)

        return binding.root
    }

    private fun setRecyclerViewList(asteroids: LiveData<List<Asteroid>>) {
        recyclerAdapter!!.submitList(asteroids.value)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.show_week_menu -> {
                viewModel.getMenuItemAsteroids(MenuItemFilter.WEEK)
                true
            }

            R.id.show_saved_menu -> {
                viewModel.getMenuItemAsteroids(MenuItemFilter.SAVED)
                true
            }

            R.id.show_today_menu -> {
                viewModel.getMenuItemAsteroids(MenuItemFilter.TODAY)
                true
            }

            else -> super.onOptionsItemSelected(item)

        }
    }
}
