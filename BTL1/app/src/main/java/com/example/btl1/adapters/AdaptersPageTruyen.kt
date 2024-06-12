package com.example.btl1.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.btl1.Model.PageModel
import com.example.btl1.R
import com.squareup.picasso.Picasso



class AdaptersPageTruyen(var context : Context, var listPage : ArrayList<PageModel>) : RecyclerView.Adapter<AdaptersPageTruyen.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(page: PageModel) {
            var linkpagetruyen = itemView.findViewById<ImageView>(R.id.imageView)

            Picasso.get().load(page.linkpage).into(linkpagetruyen)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.itemchapter, parent, false)
        return ViewHolder(view);
    }

    override fun getItemCount(): Int {
        return listPage.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindView(listPage.get(position))



    }
}