package com.example.kinokotlin.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kinokotlin.MovieDetailsActivity
import com.example.kinokotlin.R
import com.example.kinokotlin.model.Genre
import com.example.kinokotlin.model.Result
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_item_movie.view.*
import java.util.*

class MoviesItemAdapter(private val context: Context,
                        private val mMovieList: List<Result>,
                        private val mGenreList: List<Genre>)
: RecyclerView.Adapter<MoviesItemAdapter.MoviesItemViewHolder>() {
    val IMAGE_URL = "http://image.tmdb.org/t/p/w780"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = MoviesItemViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item_movie, parent, false))

    override fun onBindViewHolder(moviesItemViewHolder: MoviesItemViewHolder, position: Int) {
        moviesItemViewHolder.bindMovieItem(mMovieList[position])
    }

    override fun getItemCount() = mMovieList.size

    inner class MoviesItemViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        fun bindMovieItem(movie: Result) {

            var movieGenre = ""
            val genreMap = HashMap<Int, String>()
            for (i in mGenreList.indices) {
                genreMap[mGenreList[i].id] = mGenreList[i].name
            }

            Picasso.get().load(IMAGE_URL + movie.posterPath).into(view.movie_poster_view)
            view.read_more_button.tag = movie.id
            view.movie_title.text = movie.title
            view.movie_vote_average.text = movie.voteAverage.toString()
            view.movie_release_date.text = movie.releaseDate

            for (id in movie.genreIds!!) {
                if (!movieGenre.isEmpty()) {
                    movieGenre = movieGenre + "," + genreMap[id]
                } else {
                    movieGenre += genreMap[id]
                }
            }

            movieGenre = movieGenre.replace(",".toRegex(), "\n")
            view.movie_genre.text = movieGenre

            view.read_more_button.setOnClickListener {
                val intent = Intent(context, MovieDetailsActivity::class.java)
                intent.putExtra("MOVIE_ID", view.read_more_button.tag as Int)
                context.startActivity(intent)
            }
        }
    }
}