package com.example.opsctask1screens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.widget.Filter
import android.widget.Filterable


class ObservationAdapter(
    private val dataset: List<ObservationModel>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<ObservationAdapter.ObservationViewHolder>(), Filterable {

    private var filteredList: List<ObservationModel> = dataset.toList()

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredResults = if (constraint.isNullOrEmpty()) {
                    dataset.toList()
                } else {
                    dataset.filter {
                        it.comName.contains(constraint, ignoreCase = true) ||
                                it.sciName.contains(constraint, ignoreCase = true) ||
                                it.locName.contains(constraint, ignoreCase = true)
                        // Add more fields as needed for filtering
                    }
                }

                return FilterResults().apply {
                    values = filteredResults
                    count = filteredResults.size
                }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as? List<ObservationModel> ?: emptyList()
                notifyDataSetChanged()
            }
        }
    }
    interface OnItemClickListener {
        fun onItemClick(observation: ObservationModel)
    }
    class ObservationViewHolder(view: View, private val clickListener: OnItemClickListener) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val speciesCodeTextView: TextView = view.findViewById(R.id.speciesCodeTextView)
        val comNameTextView: TextView = view.findViewById(R.id.comNameTextView)
        val locNameTextView: TextView = view.findViewById(R.id.locNameTextView)
        val image: ImageView = view.findViewById(R.id.image)

        lateinit var observation: ObservationModel

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            observation?.let {
                // Call the onItemClick method of the clickListener with the observation
                clickListener.onItemClick(it)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObservationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_observation, parent, false)
        return ObservationViewHolder(view, itemClickListener)
    }


    override fun onBindViewHolder(holder: ObservationViewHolder, position: Int) {
        val observation = dataset[position]
        holder.speciesCodeTextView.text = observation.comName
        holder.comNameTextView.text = observation.sciName
        holder.locNameTextView.text = observation.locName

        // Set the observation for the item
        holder.observation = observation

        // Set the click listener for the item
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(observation)
        }
    }


    override fun getItemCount():Int {
       return dataset.size
    }



}
