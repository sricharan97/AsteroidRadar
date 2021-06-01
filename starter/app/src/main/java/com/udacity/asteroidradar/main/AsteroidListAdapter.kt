package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.databinding.ListViewItemBinding

/**
 * This class implements a [RecyclerView] [ListAdapter] which uses Data Binding to present [List]
 * data, including computing diffs between lists.
 * @param onClickListener a lambda
 */

class AsteroidListAdapter(private val onClickListener: OnclickListener) : ListAdapter<Asteroid,
        AsteroidListAdapter.AsteroidViewHolder>(DiffCallback) {

    /**
     * The AsteroidViewHolder constructor takes the binding variable from the associated
     * ListViewItem, which nicely gives it access to the full [Asteroid] information.
     */
    class AsteroidViewHolder(private var binding: ListViewItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(asteroid: Asteroid) {
            binding.asteroid = asteroid
            binding.executePendingBindings()
        }
    }


    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [Asteroid]
     * has been updated.
     */

    companion object DiffCallback : DiffUtil.ItemCallback<Asteroid>() {

        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

    }

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        return AsteroidViewHolder(ListViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val currentAsteroid = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(currentAsteroid)
        }
        holder.bind(currentAsteroid)

    }


    /**
     * Custom listener that handles clicks on [RecyclerView] items.  Passes the [Asteroid]
     * associated with the current item to the [onClick] function.
     * @param clickListener lambda that will be called with the current [Asteroid]
     */

    class OnclickListener(val clickListener: (asteroid: Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }

}