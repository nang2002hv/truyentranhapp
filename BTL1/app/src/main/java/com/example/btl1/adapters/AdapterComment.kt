package com.example.btl1.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.btl1.Model.Comment
import com.example.btl1.Model.User
import com.example.btl1.R
import com.example.btl1.dal.SQLiteHelper

class AdapterComment(var context : Context, var comments : List<Comment>) : RecyclerView.Adapter<AdapterComment.ViewHolder>() {

    private var db : SQLiteHelper? = null
    private var user : String = ""

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        fun bindView(commenttruyen: Comment){
            var username = itemView.findViewById<TextView>(R.id.username)
            var comment = itemView.findViewById<TextView>(R.id.comment)
            var star = itemView.findViewById<RatingBar>(R.id.user_rating)
            db = SQLiteHelper(context)
            var user = db!!.getUserByID(commenttruyen.user_id)
            username.text = user
            comment.text = commenttruyen.comment
            star.rating = commenttruyen.star.toFloat()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = View.inflate(context, R.layout.item_comment, null)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(comments[position])
    }
}
