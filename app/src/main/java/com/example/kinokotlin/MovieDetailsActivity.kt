package com.example.kinokotlin

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import android.widget.Toast
import com.example.kinokotlin.adapter.LogosItemAdapter
import com.example.kinokotlin.api.JsonPlaceHolderApi
import com.example.kinokotlin.api.RetrofitClient
import com.example.kinokotlin.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_movie_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class MovieDetailsActivity : AppCompatActivity() {
    companion object {
        private const val LIKED_COLLECTION = "liked"

        fun start(context: Context) {
            context.startActivity(Intent(context,
                MovieDetailsActivity::class.java))
        }
    }

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firebaseCloudstore by lazy { FirebaseFirestore.getInstance() }
    private val likedCollection by lazy { firebaseCloudstore.collection(LIKED_COLLECTION) }

    val APIKey = "88e00a6ff7f2cd56c1f07c582cf84355"
    val IMAGE_URL = "http://image.tmdb.org/t/p/w780"
    private var mURITrailer: String? = null

    internal var service = RetrofitClient.retrofitInstance.create(JsonPlaceHolderApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        initUI()
    }

    private fun initUI(){
        requestVideoDetails(
            service.getVideoDetail(
                intent
                    .getIntExtra("MOVIE_ID", 0), APIKey
            )
        )
        requestMovieCreditDetails(
            service.getCreditDetail(
                intent
                    .getIntExtra("MOVIE_ID", 0), APIKey
            )
        )
        requestMovieDetails(
            service.getMovieDetail(
                intent
                    .getIntExtra("MOVIE_ID", 0), APIKey
            )
        )

        if (PreferenceUtils.getLogged(this)) {

            val docRef = likedCollection.document(firebaseAuth.currentUser?.email
                                                + intent.getIntExtra("MOVIE_ID", 0).toString())
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document.data != null) {
                        println("--------------------------" + document.data)
                        add_movie.setImageResource(R.drawable.liked)
                    } else {
                        add_movie.setImageResource(R.drawable.like)
                    }
                }
                .addOnFailureListener { exception -> }
        }

        play_button.setOnClickListener { watchYoutubeVideo(this, mURITrailer) }
        add_movie.setOnClickListener {
            if (!PreferenceUtils.getLogged(this)) {
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                val docRef = likedCollection.document(firebaseAuth.currentUser?.email
                        + intent.getIntExtra("MOVIE_ID", 0).toString())
                docRef.get()
                    .addOnSuccessListener { document ->
                        if (document.data != null) {
                            val liked = Liked(
                                intent.getIntExtra("MOVIE_ID", 0).toString(),
                                firebaseAuth.currentUser?.email!!
                            )
                            deleteLiked(liked)
                            add_movie.setImageResource(R.drawable.like)
                        } else {
                            val liked = Liked(
                                intent.getIntExtra("MOVIE_ID", 0).toString(),
                                firebaseAuth.currentUser?.email!!
                            )
                            addLiked(liked)
                            add_movie.setImageResource(R.drawable.liked)
                        }
                    }
                    .addOnFailureListener { exception -> }
            }
        }
    }

    private fun addLiked(liked: Liked) {
        likedCollection.document(liked.email + liked.id).set(liked).addOnCompleteListener {
                task ->
            run {
                if (task.result == null) {
                    Toast.makeText(this, "added",
                        Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, task.exception?.message,
                        Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    private fun deleteLiked(liked: Liked) {

        likedCollection.document(liked.email + liked.id).delete().addOnCompleteListener {
                task ->
            run {
                if (task.result == null) {
                    Toast.makeText(this, "deleted",
                        Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, task.exception?.message,
                        Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    private fun requestVideoDetails(call: Call<PreVideo>) {
        call.enqueue(object : Callback<PreVideo> {
            override fun onResponse(call: Call<PreVideo>, response: Response<PreVideo>) {

                if (!response.body()!!.results?.isEmpty()!!) mURITrailer = response.body()!!.results!![0].key
            }

            override fun onFailure(call: Call<PreVideo>, t: Throwable) {
                Toast.makeText(baseContext, "Unable to load video details",
                    Toast.LENGTH_SHORT).show()
                t.printStackTrace()}

        })
    }

    private fun requestMovieCreditDetails(call: Call<PreCast>) {
        call.enqueue(object : Callback<PreCast> {
            override fun onFailure(call: Call<PreCast>, t: Throwable) {
                Toast.makeText(baseContext, "Unable to load video credits",
                    Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }

            override fun onResponse(call: Call<PreCast>, response: Response<PreCast>) {
                Picasso.get().load(IMAGE_URL + response.body()!!.cast!![0].profilePath).into(actor_1)
                Picasso.get().load(IMAGE_URL + response.body()!!.cast!![1].profilePath).into(actor_2)
                Picasso.get().load(IMAGE_URL + response.body()!!.cast!![2].profilePath).into(actor_3)
                Picasso.get().load(IMAGE_URL + response.body()!!.cast!![3].profilePath).into(actor_4)
                Picasso.get().load(IMAGE_URL + response.body()!!.cast!![4].profilePath).into(actor_5)
            }
        })
    }

    private fun requestMovieDetails(call: Call<MovieDetail>) {
        call.enqueue(object : Callback<MovieDetail> {
            override fun onFailure(call: Call<MovieDetail>, t: Throwable) {
                Toast.makeText(baseContext, "Unable to load movie details",
                    Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }

            override fun onResponse(call: Call<MovieDetail>, response: Response<MovieDetail>) {
                Picasso.get().load(IMAGE_URL + response.body()!!.backdropPath).into(backdrop)
                Picasso.get().load(IMAGE_URL + response.body()!!.posterPath).into(poster)
                Picasso.get().load(IMAGE_URL + response.body()!!.backdropPath).into(backdrop_trailer)
                movie_detail_title.text = response.body()!!.title
                description.text = response.body()!!.overview
                rating.text = response.body()!!.voteAverage.toString()

                generateLogoList(response.body()!!.productionCompanies!!)
            }
        })
    }

    private fun generateLogoList(productionCompanyList: List<ProductionCompany>) {
        val adapter = LogosItemAdapter(productionCompanyList)
        logo_list.layoutManager = LinearLayoutManager(this, LinearLayout.HORIZONTAL, false)
        logo_list.adapter = adapter
    }

    fun watchYoutubeVideo(context: Context, id: String?) {
        val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))
        val webIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://www.youtube.com/watch?v=$id")
        )
        try {
            context.startActivity(appIntent)
        } catch (ex: ActivityNotFoundException) {
            context.startActivity(webIntent)
        }

    }
}
