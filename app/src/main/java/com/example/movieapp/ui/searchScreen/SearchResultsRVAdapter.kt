package com.example.movieapp.ui.searchScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.model.SearchResult

class SearchResultsRVAdapter(
    private val searchResult: List<SearchResult>
) : RecyclerView.Adapter<SearchResultsRVAdapter.SearchViewHolder>() {

    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val searchResultTextView: TextView = itemView.findViewById(R.id.searchResultTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_list, parent, false)
        return SearchViewHolder(view)
    }

    override fun getItemCount(): Int {
        return searchResult.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        if (!searchResult.isNullOrEmpty()) {
            holder.searchResultTextView.text = searchResult[position].original_title
        } else {
            holder.searchResultTextView.text = "No movies found!"
        }
    }
}