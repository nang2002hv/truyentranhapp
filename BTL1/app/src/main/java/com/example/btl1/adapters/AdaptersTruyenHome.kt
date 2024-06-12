package com.example.btl1.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.btl1.Model.TruyenModel
import com.example.btl1.R
import com.example.btl1.activity.ChiTietTruyen

import com.squareup.picasso.Picasso

class AdaptersTruyenHome(var context : Context, var listTruyen : ArrayList<TruyenModel>,var iduser : String) : RecyclerView.Adapter<AdaptersTruyenHome.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(truyen: TruyenModel) {
            val tenTruyen = itemView.findViewById<TextView>(R.id.tentruyen)
            val chapTruyen = itemView.findViewById<TextView>(R.id.chappter)
            val chapTruyen1 = itemView.findViewById<TextView>(R.id.chappter1)
            val chapTruyen2 = itemView.findViewById<TextView>(R.id.chappter2)
            val ngay = itemView.findViewById<TextView>(R.id.ngay)
            val ngay1 = itemView.findViewById<TextView>(R.id.ngay1)
            val ngay2 = itemView.findViewById<TextView>(R.id.ngay2)
            var hinhtruyen = itemView.findViewById<ImageView>(R.id.img_truyen)
            tenTruyen.text = truyen.tentruyen
            chapTruyen.text = truyen.chaptruyen.getOrNull(0)?.tenchap
            chapTruyen1.text = truyen.chaptruyen.getOrNull(1)?.tenchap
            chapTruyen2.text = truyen.chaptruyen.getOrNull(2)?.tenchap
            ngay.text = truyen.chaptruyen.getOrNull(0)?.timechap
            ngay1.text = truyen.chaptruyen.getOrNull(1)?.timechap
            ngay2.text = truyen.chaptruyen.getOrNull(2)?.timechap
            Picasso.get().load(truyen.linkhinhtruyen).into(hinhtruyen)
            itemView.setOnClickListener {
                val intent = Intent(context, ChiTietTruyen::class.java)
                intent.putExtra("linktruyen", truyen.linktruyen)
                intent.putExtra("tentruyen", truyen.tentruyen)
                intent.putExtra("linkhinhtruyen", truyen.linkhinhtruyen)
                intent.putExtra("chapTruyen", truyen.chaptruyen.getOrNull(0)?.linkchap)
                intent.putExtra("chapTruyen1", truyen.chaptruyen.getOrNull(1)?.linkchap)
                intent.putExtra("chapTruyen2", truyen.chaptruyen.getOrNull(2)?.linkchap)
                intent.putExtra("id", truyen.id)
                intent.putExtra("ngay", truyen.chaptruyen.getOrNull(0)?.timechap)
                intent.putExtra("ngay1", truyen.chaptruyen.getOrNull(1)?.timechap)
                intent.putExtra("ngay2", truyen.chaptruyen.getOrNull(2)?.timechap)
                intent.putExtra("tenchap", truyen.chaptruyen.getOrNull(0)?.tenchap)
                intent.putExtra("tenchap1", truyen.chaptruyen.getOrNull(1)?.tenchap)
                intent.putExtra("tenchap2", truyen.chaptruyen.getOrNull(2)?.tenchap)
                intent.putExtra("iduser", iduser)
                context.startActivity(intent)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.itemtruyen, parent, false)
        return ViewHolder(view);
    }

    override fun getItemCount(): Int {
        return listTruyen.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindView(listTruyen.get(position))



    }
}