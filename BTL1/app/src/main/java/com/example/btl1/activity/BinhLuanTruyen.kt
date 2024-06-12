package com.example.btl1.activity

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.btl1.Model.Comment
import com.example.btl1.R
import com.example.btl1.adapters.AdapterComment
import com.example.btl1.dal.SQLiteHelper

class BinhLuanTruyen : AppCompatActivity() {

    private lateinit var linkTruyen : String
    private lateinit var iduser : String
    private var imgBack : ImageView? = null
    private var ratingbar : RatingBar? = null
    private var inputComment : EditText? = null
    private var btnSend : ImageView? = null
    private var db : SQLiteHelper? = null
    private var recyclerView : RecyclerView? = null
    private var gridLayoutManager : GridLayoutManager? = null
    private var adapterComment : AdapterComment? = null
    private var listComment : List<Comment>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_binh_luan_truyen)
        linkTruyen = intent.getStringExtra("linktruyen").toString()
        val url = linkTruyen
        val segments = url.split("-")
        val id = segments.last()
        ratingbar = findViewById(R.id.ratingBar)
        inputComment = findViewById(R.id.comment_input)
        iduser = intent.getStringExtra("iduser").toString()
        imgBack = findViewById(R.id.back)
        imgBack?.setOnClickListener {
            onBackPressed()
        }
        db = SQLiteHelper(this)

        recyclerView = findViewById(R.id.recycler_view)
        gridLayoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = gridLayoutManager
        listComment = db?.getAllComment(id.toInt())
        adapterComment = AdapterComment(this, listComment!!)
        recyclerView?.adapter = adapterComment
        btnSend = findViewById(R.id.send_button)
        setComment(iduser.toInt(), id.toInt())
    }


    fun setComment(id : Int, idtruyen : Int){
        btnSend?.setOnClickListener {
            var start = ratingbar?.rating
            var comment = inputComment?.text.toString()
            var user_id = id
            var cmt = Comment(0, user_id, comment, start!!.toInt(), idtruyen)
            db?.insertComment(cmt)
            inputComment?.setText("")
            ratingbar?.setRating(0f)
            listComment = db?.getAllComment(idtruyen)
            adapterComment = AdapterComment(this, listComment!!)
            recyclerView?.adapter = adapterComment
            adapterComment?.notifyDataSetChanged()

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(inputComment?.windowToken, 0)
        }
    }
}