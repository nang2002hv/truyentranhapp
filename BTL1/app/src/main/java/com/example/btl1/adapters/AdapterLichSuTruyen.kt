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

class AdapterLichSuTruyen(var context : Context,
                          var listTruyen : ArrayList<TruyenModel>,
                          var iduser : String,
                          var onItemDeleteClick: (Int) -> Unit = {}
    ) : RecyclerView.Adapter<AdapterLichSuTruyen.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(truyen: TruyenModel) {
            var img = itemView.findViewById<ImageView>(R.id.img_avatar)
            var tentruyen = itemView.findViewById<TextView>(R.id.tentruyen)
            Picasso.get().load(truyen.linkhinhtruyen).into(img)
            var img_delete = itemView.findViewById<ImageView>(R.id.img_delete)
            tentruyen.text = truyen.tentruyen

            itemView.setOnClickListener {
                val intent = Intent(context, ChiTietTruyen::class.java)
                intent.putExtra("linktruyen",truyen.linktruyen)
                intent.putExtra("tentruyen", truyen.tentruyen)
                intent.putExtra("iduser", iduser)
                context.startActivity(intent)
            }
            img_delete.setOnClickListener() {
                if (truyen.chaptruyen.isNotEmpty()) {
                    onItemDeleteClick(truyen.chaptruyen[0].id)
                }
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.item_lstruyen, parent, false)
        return ViewHolder(view);
    }

    override fun getItemCount(): Int {
        return listTruyen.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindView(listTruyen.get(position))



    }
}