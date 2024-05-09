package com.example.movieapp.ui.movieDetailsScreen

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.ImageView
import android.widget.Toast
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.utils.APIConstants

class TrailersRVAdapter(
    private val trailers : List<String>
): RecyclerView.Adapter<TrailersRVAdapter.TrailerViewHolder>(){

    inner class TrailerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val trailerWebView: WebView = itemView.findViewById(R.id.trailerWebView)
//        val trailerPlayButton: ImageView = itemView.findViewById(R.id.trailerPlayButton)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrailersRVAdapter.TrailerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.trailers_list, parent, false)
        return TrailerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return trailers.size
    }

    override fun onBindViewHolder(holder: TrailersRVAdapter.TrailerViewHolder, position: Int) {
        if(!trailers.isNullOrEmpty()) {
            holder.trailerWebView.apply {
                clearHistory()
                loadData("", "text/html", "utf-8")
                settings.javaScriptEnabled = true
                setBackgroundColor(Color.TRANSPARENT) // for removing white background
                val html =
                    "<iframe width=\"100%\" height=\"100%\" src=\"${APIConstants.YOUTUBE_URL}${trailers[position]}\" frameborder=\"0\" allowfullscreen></iframe>"
                loadData(html, "text/html", "utf-8")
            }
        } else{
            Toast.makeText(holder.itemView.context, "Unable to fetch trailers!", Toast.LENGTH_SHORT).show()
        }
    }
}