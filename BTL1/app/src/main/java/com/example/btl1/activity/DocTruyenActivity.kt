package com.example.btl1.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.btl1.Model.Chapter
import com.example.btl1.Model.ChapterDaDoc
import com.example.btl1.Model.PageModel
import com.example.btl1.R
import com.example.btl1.adapters.AdaptersPageTruyen
import com.example.btl1.dal.SQLiteHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import java.io.IOException

class DocTruyenActivity : AppCompatActivity() {

    private lateinit var linkchapter : String
    private lateinit var idUser : String
    private lateinit var tenchapter1 : String
    private lateinit var listPage : ArrayList<PageModel>
    private lateinit var recyclerView: RecyclerView
    private var gridLayoutManager: GridLayoutManager? = null
    private var adaptersPageTruyen : AdaptersPageTruyen? = null
    private lateinit var listchapter : ArrayList<Chapter>
    private var textView : TextView? = null
    private lateinit var idTruyen : String
    private var img : ImageView? = null
    private var img1 : ImageView? = null
    private var img2 : ImageView? = null
    private var db : SQLiteHelper ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_doc_truyen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        recyclerView = findViewById(R.id.recycler_view)
        gridLayoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = gridLayoutManager
        listPage = ArrayList()
        textView = findViewById(R.id.thongtintruyen)
        adaptersPageTruyen = AdaptersPageTruyen(this,listPage)
        recyclerView?.adapter = adaptersPageTruyen
        linkchapter = intent.getStringExtra("linkchapter").toString()
        idTruyen = intent.getStringExtra("idtruyen").toString()
        tenchapter1 = intent.getStringExtra("tenchapter").toString()
        idUser = intent.getStringExtra("iduser").toString()
        getAllChapter(linkchapter)
        img = findViewById(R.id.arrow_back)
        img1 = findViewById(R.id.pre)
        img2 = findViewById(R.id.next)
        img?.setOnClickListener {
            onBackPressed()
        }
        db = SQLiteHelper(this)
        listchapter = db!!.getAllChapter(idTruyen.toInt())

        img1?.setOnClickListener {
            var vitri = 0
            var i = 0
            var idchapter = -1
            for(value in listchapter){
                if (value.tenchapter == tenchapter1.trim()){
                    vitri = i
                    idchapter = value.id
                    break;
                }
                i = i + 1
            }
            if (vitri < listchapter.size - 1) {
                var truyenDaDoc1 = db?.getTruyenDaDocByIDTruyenAndIDUser(idTruyen.toInt(), idUser.toInt())
                tenchapter1 = listchapter[vitri + 1].tenchapter
                if(idchapter != -1){
                    var chapterDaDoc = ChapterDaDoc(0,idchapter,truyenDaDoc1!!.id, idTruyen.toInt())
                    var chapterDaDoc1 = db?.getChappterDaDocByIDChapterAndIDTruyenAndIDUser(idchapter+1, idTruyen.toInt(), truyenDaDoc1.id)
                    if(chapterDaDoc1 == null){
                        db?.insertChapterDaDoc(chapterDaDoc)
                    }
                }

                getAllChapter(listchapter[vitri + 1].urlchapter)
            }
        }

        img2?.setOnClickListener {
            var vitri = 0
            var i = 0
            var idchapter = -1
            println(tenchapter1)
            for(value in listchapter){
                if (value.tenchapter == tenchapter1.trim()){
                    vitri = i
                    idchapter = value.id
                    break;
                }
                i = i + 1
            }
            if (vitri > 0) {
                tenchapter1 = listchapter[vitri - 1].tenchapter
                var truyenDaDoc1 = db?.getTruyenDaDocByIDTruyenAndIDUser(idTruyen.toInt(), idUser.toInt())
                tenchapter1 = listchapter[vitri + 1].tenchapter
                if(idchapter != -1){
                    var chapterDaDoc = ChapterDaDoc(0,idchapter,truyenDaDoc1!!.id, idTruyen.toInt())
                    var chapterDaDoc1 = db?.getChappterDaDocByIDChapterAndIDTruyenAndIDUser(idchapter-1, idTruyen.toInt(), truyenDaDoc1.id)
                    if(chapterDaDoc1 == null){
                        db?.insertChapterDaDoc(chapterDaDoc)
                    }
                }
                getAllChapter(listchapter[vitri - 1].urlchapter)
            }
        }
    }

    private fun getAllChapter(linkchapter : String){
        println("link chapter" + linkchapter)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                listPage.clear()
                var i = 0
                val doc = Jsoup.connect(linkchapter).get()
                val element = doc.select("main.main")
                val element1 = element.select("div.container > div.row > div.full-width > div.reading ")
                var title = element1.select("div.container > div.top > h1.txt-primary > span").text()
                title = title.replace("-", "")
                var element2 = element1.select("div.reading-detail > div.page-chapter")
                for(value in element2){
                    i = i + 1
                    var link = value.select("img").attr("data-original")
                    var tenpage = "Page " + i
                    listPage.add(PageModel(tenpage,link))
                }
                tenchapter1 = title
                listPage.removeAt(listPage.size - 1)
                launch(Dispatchers.Main){
                    textView?.text = title
                    adaptersPageTruyen?.notifyDataSetChanged()
                }
            }catch (e : IOException){
                e.printStackTrace()
            }
        }
    }


}