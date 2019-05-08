package com.example.kinokotlin.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kinokotlin.R
import com.example.kinokotlin.model.ProductionCompany
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_item_logos.view.*


class LogosItemAdapter(private val productionCompanyList: List<ProductionCompany>) : RecyclerView.Adapter<LogosItemAdapter.LogosItemViewHolder>() {
    val IMAGE_URL = "http://image.tmdb.org/t/p/w780"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = LogosItemViewHolder(LayoutInflater.from(parent.context)
        .inflate(R.layout.layout_item_logos, parent, false))

    override fun onBindViewHolder(logosItemViewHolder: LogosItemViewHolder, position: Int) {
        logosItemViewHolder.bindMovieItem(productionCompanyList[position])
    }

    override fun getItemCount() = productionCompanyList.size

    inner class LogosItemViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        fun bindMovieItem(productionCompany: ProductionCompany) {

            Picasso.get().load(IMAGE_URL + productionCompany.logoPath).into(view.logo)
        }
    }
}