package com.udacity.asteroidradar.main

import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.AsteroidViewItemBinding
import com.udacity.asteroidradar.domain.Asteroid

class AsteroidRecyclerAdapter (val onClickListener: AsteroidOnClickListener) :
    ListAdapter<Asteroid , AsteroidRecyclerAdapter.AsteroidViewHolder> (AsteroidDiffCallBack)
{
    /**The AsteroidViewHolder constructor takes the binding variable from the associated Asteroid
    List Item, which gives access to full Asteroid **/
    class AsteroidViewHolder(private val binding:AsteroidViewItemBinding) :
            RecyclerView.ViewHolder ( binding.root){

        fun bind(asteroid: Asteroid) {
            binding.asteroid = asteroid
            //to ensure that data binding executes immediately and the Recycler calculates the size
            binding.executePendingBindings()
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [Asteroid]
     * has been updated.
     */
companion object AsteroidDiffCallBack: DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        return AsteroidViewHolder (AsteroidViewItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val asteroid =getItem(position)
        Log.i("AsteroidRecyclerAdapter", "inside onBindViewHolder")
        holder.itemView.setOnClickListener {
            run {
                Log.i("AsteroidRecyclerAdapter", "inside onBindViewHolder - Set on click listener")
                onClickListener.onClick(asteroid)
            }

        }
        holder.bind(asteroid)
    }

    /**
     * Click Listener to handle click on the recycler view item
     */

    class AsteroidOnClickListener(val clickListener: (asteroid: Asteroid) -> Unit) {

        fun onClick(asteroid: Asteroid) = {
                    Log.i("AsteroidOnClickListener", "Inisde OnClick")
                    clickListener(asteroid) }
    }

}