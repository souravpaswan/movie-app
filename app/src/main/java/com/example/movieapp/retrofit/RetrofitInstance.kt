package com.example.movieapp.retrofit

import com.example.movieapp.utils.APIConstants
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object {
//        val BASE_URL = "https://api.themoviedb.org/3/"
//        val API_KEY = "ce99ea84f89451260059c832125c6353"

        fun getRetrofitInstance(): Retrofit {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl(APIConstants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
        }

        val authService: RetrofitService by lazy {
            getRetrofitInstance().create(RetrofitService::class.java)
        }
    }
}