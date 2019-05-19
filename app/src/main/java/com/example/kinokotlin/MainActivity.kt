package com.example.kinokotlin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.Toast
import com.example.kinokotlin.adapter.MoviesItemAdapter
import com.example.kinokotlin.api.JsonPlaceHolderApi
import com.example.kinokotlin.api.RetrofitClient
import com.example.kinokotlin.model.Genre
import com.example.kinokotlin.model.Movie
import com.example.kinokotlin.model.PreGenre
import com.example.kinokotlin.model.Result
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList
import android.widget.ArrayAdapter
import com.example.kinokotlin.R.string.genre


class MainActivity : AppCompatActivity() {

    private lateinit var mToggle: ActionBarDrawerToggle
    private var genres: List<Genre> = ArrayList()
    private var genreId: Int = 28
    private var movieType: String = "most_popular"

    val APIKey = "88e00a6ff7f2cd56c1f07c582cf84355"
    internal var service = RetrofitClient.retrofitInstance.create(JsonPlaceHolderApi::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val call = service.getPopularMovie(APIKey)

        requestGenreList()
        requestMovieList(call)
        initUI()

    }

    private fun initUI() {
        mToggle = ActionBarDrawerToggle(this, activity_main, R.string.open, R.string.close)
        activity_main.addDrawerListener(mToggle)
        mToggle.syncState()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        spinnerGenre?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var c: Call<Movie>? = null
                var ge: Genre = spinnerGenre.selectedItem as Genre
                genreId = ge.id
                if (movieType.equals("most_popular")) {
                    c = service.getPopularMovie(APIKey)
                    requestMovieList(c)
                } else if (movieType.equals("top_rated")) {
                    c = service.getTopRatedMovie(APIKey)
                    requestMovieList(c)
                } else if (movieType.equals("upcoming")) {
                    c = service.getUpcomingMovie(APIKey)
                    requestMovieList(c)
                } else if (movieType.equals("latest")) {
                    c = service.getNowPlayingMovie(APIKey)
                    requestMovieList(c)
                } else if (movieType.equals("now_playing")) {
                    c = service.getNowPlayingMovie(APIKey)
                    requestMovieList(c)
                }


            }

        }

        nav_view.setNavigationItemSelectedListener {
            genreId = 0
            var call: Call<Movie>? = null
            when (it.itemId) {
                R.id.most_popular -> {
                    call = service.getPopularMovie(APIKey)
                    requestMovieList(call)
                    movieType = "most_popular"
                    activity_main.closeDrawers()
                    true
                }
                R.id.top_rated -> {
                    call = service.getTopRatedMovie(APIKey)
                    requestMovieList(call)
                    movieType = "top_rated"
                    activity_main.closeDrawers()
                    true
                }
                R.id.upcoming -> {
                    call = service.getUpcomingMovie(APIKey)
                    requestMovieList(call)
                    movieType = "upcoming"
                    activity_main.closeDrawers()
                    true
                }
                R.id.latest -> {
                    call = service.getNowPlayingMovie(APIKey)
                    requestMovieList(call)
                    movieType = "latest"
                    activity_main.closeDrawers()
                    true
                }
                R.id.now_playing -> {
                    call = service.getNowPlayingMovie(APIKey)
                    requestMovieList(call)
                    movieType = "now_playing"
                    activity_main.closeDrawers()
                    true
                }
                R.id.login -> {
                    PreferenceUtils.saveLogged(this, false)
                    startActivity(Intent(this, LoginActivity::class.java))
                    true
                }

                else -> false
            }
        }

    }

    private fun requestMovieList(call: Call<Movie>) {
        call.enqueue(object : Callback<Movie> {
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                generateMovieList(response.body()?.results!!, genres)
            }

            override fun onFailure(call: Call<Movie>, t: Throwable) {
                Toast.makeText(
                    baseContext, "Unable to load movies",
                    Toast.LENGTH_SHORT
                ).show()
                t.printStackTrace()
            }
        })

    }

    private fun requestGenreList() {
        val call = service.getPreGenre(APIKey)
        call.enqueue(object : Callback<PreGenre> {
            override fun onResponse(call: Call<PreGenre>, response: Response<PreGenre>) {
                genres = response.body()?.genres!!
                populateGenreList(genres)
            }

            override fun onFailure(call: Call<PreGenre>, t: Throwable) {
                Toast.makeText(
                    baseContext, "Unable to load genres",
                    Toast.LENGTH_SHORT
                ).show()
                t.printStackTrace()
            }
        })
    }

    private fun generateMovieList(results: List<Result>, genreList: List<Genre>) {
        val filteredResults: MutableList<Result> =  mutableListOf<Result>()

        if (genreId != 0) {
            for (i in results.indices) {
                if (results[i].genreIds!!.contains(genreId)) {
                    filteredResults.add(results[i])
                }
            }

        } else {
            filteredResults.addAll(results)
        }
        println(genreId)
        println(filteredResults)
        val adapter = MoviesItemAdapter(this, filteredResults, genreList)
        movies.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        movies.adapter = adapter

    }

    private fun populateGenreList(genreList: List<Genre>) {
        val dataAdapter = ArrayAdapter<Genre>(this, android.R.layout.simple_spinner_item, genreList)

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGenre.adapter = dataAdapter

    }
}
