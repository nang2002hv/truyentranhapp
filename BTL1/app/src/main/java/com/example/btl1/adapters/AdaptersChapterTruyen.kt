package com.example.btl1.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.btl1.Model.ChapterDaDoc
import com.example.btl1.Model.ChapterModel
import com.example.btl1.Model.TruyenDaDoc
import com.example.btl1.R
import com.example.btl1.activity.DocTruyenActivity
import com.example.btl1.dal.SQLiteHelper

class AdaptersChapterTruyen(var context : Context, var listChapter : ArrayList<ChapterModel>, var iduser : String) : RecyclerView.Adapter<AdaptersChapterTruyen.ViewHolder>() {

    var db : SQLiteHelper? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(chapterModel: ChapterModel) {
            println("hello chappter")
            db = SQLiteHelper(context)
            var tenChapter = itemView.findViewById<TextView>(R.id.ten_chapter_chi_tiet)
            var timeChapter = itemView.findViewById<TextView>(R.id.time_chitiet_chapter)
            tenChapter.text = chapterModel.tenchap
            timeChapter.text = chapterModel.timechap
            if(chapterModel.id == -1){
                tenChapter.setTextColor(Color.GREEN)
                timeChapter.setTextColor(Color.GREEN)
            }
            if(chapterModel.id == 0){
                tenChapter.setTextColor(Color.WHITE)
                timeChapter.setTextColor(Color.WHITE)
            }

            itemView.setOnClickListener {
                var truyen = SQLiteHelper(context).getTruyenDBByID(chapterModel.truyenId)
                var luotxem = truyen.luotxem + 1

                db?.updateLuotXem(truyen.id, luotxem)
                var truyenDaDoc = TruyenDaDoc(0,1, iduser.toInt(),truyen.id)
                if(iduser != "0"){
                    val truyenDaDocFromDB = db?.getTruyenDaDocByIDTruyenAndIDUser(truyen.id, iduser.toInt())
                    if(truyenDaDocFromDB == null){
                        db?.insertTruyenDaDoc(truyenDaDoc)
                    }
                }
                var truyenDaDoc1 = db?.getTruyenDaDocByIDTruyenAndIDUser(truyen.id, iduser.toInt())
                if (truyenDaDoc1 != null) {
                    val idChapter = db?.getIdChapter(chapterModel.tenchap, truyen.id)
                    if (idChapter != null) {
                        var chapterDaDoc = ChapterDaDoc(0,idChapter,truyenDaDoc1.id, truyen.id)
                        var chapterDaDoc1 = db?.getChappterDaDocByIDChapterAndIDTruyenAndIDUser(idChapter, truyen.id, truyenDaDoc1.id)
                        if(chapterDaDoc1 == null){
                            db?.insertChapterDaDoc(chapterDaDoc)
                        }
                    }
                }
                val intent = Intent(context, DocTruyenActivity::class.java)
                intent.putExtra("linkchapter", chapterModel.linkchap)
                intent.putExtra("idtruyen", chapterModel.truyenId.toString())
                intent.putExtra("tenchapter", chapterModel.tenchap)
                intent.putExtra("iduser", iduser)
                context.startActivity(intent)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.item_chapter, parent, false)
        return ViewHolder(view);
    }

    override fun getItemCount(): Int {
        return listChapter.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindView(listChapter.get(position))



    }
}