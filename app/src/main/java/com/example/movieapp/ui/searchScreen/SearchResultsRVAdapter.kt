package com.example.movieapp.ui.searchScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.model.SearchResult
import com.example.movieapp.utils.APIConstants

class SearchResultsRVAdapter(
    var searchResult: List<SearchResult>,
    private val searchResultItemOnClickListener: SearchResultItemOnClickListener
) : RecyclerView.Adapter<SearchResultsRVAdapter.SearchViewHolder>() {

    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val searchResultImageView: ImageView = itemView.findViewById(R.id.searchResultImageView)
        val searchListLayout: ConstraintLayout = itemView.findViewById(R.id.searchListLayout)
    }

    interface SearchResultItemOnClickListener{
        fun showSearchItemMovieDetails(movieId: Int)
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
            val imageUrl = APIConstants.IMAGE_PATH + searchResult[position].poster_path
            Glide.with(holder.searchResultImageView)
                .load(imageUrl)
                .into(holder.searchResultImageView)

        }
        holder.searchListLayout.setOnClickListener {
            searchResultItemOnClickListener.showSearchItemMovieDetails(searchResult[position].id)
        }
    }
}