package com.example.kinokotlin.api

import com.example.kinokotlin.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface JsonPlaceHolderApi {
    @GET("movie/popular")
    fun getPopularMovie(@Query("api_key") key: String): Call<Movie>

    @GET("movie/top_rated")
    fun getTopRatedMovie(@Query("api_key") key: String): Call<Movie>

    @GET("movie/upcoming")
    fun getUpcomingMovie(@Query("api_key") key: String): Call<Movie>

    @GET("movie/latest")
    fun getLatestMovie(@Query("api_key") key: String): Call<Movie>

    @GET("movie/now_playing")
    fun getNowPlayingMovie(@Query("api_key") key: String): Call<Movie>

    @GET("genre/movie/list")
    fun getPreGenre(@Query("api_key") key: String): Call<PreGenre>

    @GET("movie/{movie_id}")
    fun getMovieDetail(@Path("movie_id") movieId: Int?, @Query("api_key") key: String): Call<MovieDetail>

    @GET("movie/{movie_id}/credits")
    fun getCreditDetail(@Path("movie_id") movieId: Int?, @Query("api_key") key: String): Call<PreCast>

    @GET("movie/{movie_id}/videos")
    fun getVideoDetail(@Path("movie_id") movieId: Int?, @Query("api_key") key: String): Call<PreVideo>


}
