package com.example.movieapp.ui.movieDetailsScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.model.Cast
import com.example.movieapp.utils.APIConstants

class MovieCastRVAdapter(
    private val cast: List<Cast>
) : RecyclerView.Adapter<MovieCastRVAdapter.CastViewHolder>(){

    inner class CastViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val castMemberNameTextView: TextView = itemView.findViewById(R.id.castMemberNameTextView)
        val castMemberImageView: ImageView = itemView.findViewById(R.id.castMemberImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cast_list, parent, false)
        return CastViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cast.size
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        if(!cast.isNullOrEmpty()) {
            holder.castMemberNameTextView.text = cast[position].name
            if(cast[position].profile_path != null) {
                val imageUrl = APIConstants.IMAGE_PATH + cast[position].profile_path
                Glide.with(holder.castMemberImageView)
                    .load(imageUrl)
                    .into(holder.castMemberImageView)
            } else{
                Glide.with(holder.castMemberImageView)
                    .load(R.drawable.no_image_placeholder)
                    .into(holder.castMemberImageView)
            }
        } else{
            Toast.makeText(holder.itemView.context, "Unable to fetch cast details!", Toast.LENGTH_SHORT).show()
        }
    }
}