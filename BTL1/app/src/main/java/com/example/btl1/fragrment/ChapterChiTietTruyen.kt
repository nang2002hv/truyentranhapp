package com.example.btl1.fragrment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.btl1.Model.Chapter
import com.example.btl1.Model.ChapterDaDoc
import com.example.btl1.Model.ChapterModel
import com.example.btl1.R
import com.example.btl1.adapters.AdaptersChapterTruyen
import com.example.btl1.dal.SQLiteHelper

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException


class ChapterChiTietTruyen : Fragment() {

    private lateinit var recyclerChapter: RecyclerView
    private lateinit var adaptersChapterTruyen: AdaptersChapterTruyen
    private  var listChapter: ArrayList<ChapterModel> = ArrayList()
    private lateinit var linkTruyen: String
    private lateinit var iduser : String
    private var gridLayoutManager: GridLayoutManager? = null
    private var db : SQLiteHelper? = null
    private var chapterdbs : List<Chapter>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            linkTruyen = it.getString("linktruyen").toString()
            iduser = it.getString("iduser").toString()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(linktruyen: String, iduser : String) =
            ChapterChiTietTruyen().apply {
                arguments = Bundle().apply {
                    putString("linktruyen", linktruyen)
                    putString("iduser", iduser)

                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chapter_chi_tiet_truyen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerChapter = view.findViewById(R.id.recycler_view_chapter)
        gridLayoutManager = GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
        recyclerChapter?.layoutManager = gridLayoutManager
        recyclerChapter?.setHasFixedSize(true)
        adaptersChapterTruyen = AdaptersChapterTruyen(requireContext(), listChapter, iduser)
        recyclerChapter?.adapter = adaptersChapterTruyen
        getAllChapter(linkTruyen)

    }

    override fun onResume() {
        super.onResume()
        var linkTruyen1 = linkTruyen
        db = SQLiteHelper(requireContext())
        val url = linkTruyen1
        val segments = url.split("-")
        val id = segments.last()
        for(chapter in listChapter){
            if(iduser != "0"){
                var truyenDaDoc1 = db?.getTruyenDaDocByIDTruyenAndIDUser(id.toInt(), iduser.toInt())
                if (truyenDaDoc1 != null) {
                    val idChapter = db?.getIdChapter(chapter.tenchap, chapter.truyenId)
                    if (idChapter != null) {
                        var chapterDaDoc = ChapterDaDoc(0,idChapter,truyenDaDoc1.id, chapter.truyenId)
                        var chapterDaDoc1 = db?.getChappterDaDocByIDChapterAndIDTruyenAndIDUser(idChapter, chapter.truyenId, truyenDaDoc1.id)
                        if (chapterDaDoc1 != null) {
                            // If the chapter has been read, change the color of the item
                            chapter.id = -1
                        } else {
                            // If the chapter has not been read, keep the original color
                            chapter.id = 0
                        }
                    }
                }
            }
        }
        adaptersChapterTruyen?.notifyDataSetChanged()
    }



    private fun getAllChapter(linktruyen : String){
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                db = SQLiteHelper(requireContext())
                val url = linkTruyen
                val segments = url.split("-")
                val id = segments.last()
                chapterdbs = db?.getAllChapter(id.toInt())
//                if(chapterdbs != null){
//                    for (chapter in chapterdbs!!){
//                        var tenchapter = chapter.tenchapter
//                        var urlchapter = chapter.urlchapter
//                        var timechapter = chapter.timechapter
//                        listChapter.add(ChapterModel(0,chapter.truyen_id,tenchapter,urlchapter,timechapter))
//                    }
//                }
                if(chapterdbs != null){
                    for (chapter in chapterdbs!!){
                        if(iduser != "0"){
                            var truyenDaDoc1 = db?.getTruyenDaDocByIDTruyenAndIDUser(id.toInt(), iduser.toInt())
                            if (truyenDaDoc1 != null) {
                                val idChapter = db?.getIdChapter(chapter.tenchapter, chapter.truyen_id)
                                if (idChapter != null) {
                                    var chapterDaDoc1 = db?.getChappterDaDocByIDChapterAndIDTruyenAndIDUser(idChapter, chapter.truyen_id, truyenDaDoc1.id)
                                    if (chapterDaDoc1 != null) {
                                        listChapter.add(ChapterModel(-1,chapter.truyen_id,chapter.tenchapter,chapter.urlchapter,chapter.timechapter))
                                    } else {
                                        listChapter.add(ChapterModel(0,chapter.truyen_id,chapter.tenchapter,chapter.urlchapter,chapter.timechapter))
                                    }
                                }
                            } else {
                                listChapter.add(ChapterModel(0,chapter.truyen_id,chapter.tenchapter,chapter.urlchapter,chapter.timechapter))
                            }
                        }


                    }
                }
                println("hello chapter" + listChapter.size)

                launch(Dispatchers.Main) {
                    adaptersChapterTruyen?.notifyDataSetChanged()
                }
            } catch (e: IOException) {
                // Handle any exceptions here
                e.printStackTrace()
            }
        }

    }


}