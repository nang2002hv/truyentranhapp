package com.example.btl1.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.btl1.Model.TheLoai
import com.example.btl1.R

class CategoryAdapter(var context: Context, val categories: List<TheLoai>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun finbView(category: TheLoai) {
            val name = itemView.findViewById<TextView>(R.id.the_loai)
            name.text = category.tentheloai
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_the_loai, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.finbView(categories[position])
    }

    override fun getItemCount() = categories.size
}