package com.example.btl1.activity

import android.content.Intent
import kotlin.math.roundToInt
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.btl1.Model.Chapter
import com.example.btl1.Model.ChapterDaDoc
import com.example.btl1.Model.ChapterModel
import com.example.btl1.Model.TheLoai
import com.example.btl1.Model.Truyen
import com.example.btl1.Model.TruyenModel
import com.example.btl1.Model.TruyenYeuThich
import com.example.btl1.R
import com.example.btl1.adapters.CategoryAdapter
import com.example.btl1.adapters.ViewPager2Adapter
import com.example.btl1.dal.SQLiteHelper
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import java.io.IOException

class ChiTietTruyen : AppCompatActivity() {

    private lateinit var linkTruyen : String
    private lateinit var tentruyen : String
    private lateinit var timeView : TextView
    private lateinit var tentruyenView : TextView
    private lateinit var yeuthich : TextView
    private lateinit var view : TextView
    private lateinit var tinhtrangtruyenView : TextView
    private lateinit var theloais : ArrayList<TheLoai>
    private lateinit var linkanhView : ImageView
    private lateinit var recyclerView: RecyclerView
    private var gridLayoutManager: GridLayoutManager? = null
    private var categoryAdapter : CategoryAdapter? = null
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var viewPager : ViewPager2
    private var img : ImageView? = null
    private var imgfavorite : ImageView ? = null
    private var db : SQLiteHelper? = null
    private var iduser : String = ""
    private var truyenModel : ArrayList<TruyenModel>? = null
    private var chappters : ArrayList<Chapter>? = null
    private var imgChat : ImageView? = null
    private var textchat : TextView? = null
    private var textStar : TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chitiettruyen)
        linkTruyen = intent.getStringExtra("linktruyen").toString()
        window.decorView.setBackgroundColor(ContextCompat.getColor(this, R.color.black))
        var chapterModels = ArrayList<ChapterModel>()
        val id = intent.getIntExtra("id", -1)
        iduser = intent.getStringExtra("iduser").toString()
        val tentruyen = intent.getStringExtra("tentruyen").toString()
//        val linkhinhtruyen = intent.getStringExtra("linkhinhtruyen").toString()
//        val tenchap = intent.getStringExtra("tenchap").toString()
//        val chapTruyen = intent.getStringExtra("chapTruyen").toString()
//        val ngay = intent.getStringExtra("ngay").toString()
//        chapterModels.add( ChapterModel(0, id, tenchap, chapTruyen, ngay))
//
//        val tenchap1 = intent.getStringExtra("tenchap1").toString()
//        val chapTruyen1 = intent.getStringExtra("chapTruyen1").toString()
//        val ngay1 = intent.getStringExtra("ngay1").toString()
//        chapterModels.add(ChapterModel(0, id,tenchap1,chapTruyen1,ngay1))
//
//        val tenchap2 = intent.getStringExtra("tenchap2").toString()
//        val chapTruyen2 = intent.getStringExtra("chapTruyen2").toString()
//        val ngay2 = intent.getStringExtra("ngay2").toString()
//        val chapterModel2 = ChapterModel(0,id,tenchap2,chapTruyen2,ngay2)
//        chapterModels.add(chapterModel2)
//
//        var truyenModel = TruyenModel(id,tentruyen,linkhinhtruyen,chapterModels,linkTruyen)
        db = SQLiteHelper(this)
        textStar = findViewById(R.id.star)
        textchat = findViewById(R.id.textchat)
        view = findViewById(R.id.view)
        yeuthich = findViewById(R.id.textfavorite)
        timeView = findViewById(R.id.time_chitiet)
        tentruyenView = findViewById(R.id.tentruyen_chitiet)
        tinhtrangtruyenView = findViewById(R.id.tinhtrang)
        linkanhView = findViewById(R.id.img_truyen_chi_tiet)
        recyclerView = findViewById(R.id.the_loai_truyen)
        gridLayoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = gridLayoutManager
        theloais = ArrayList()
        categoryAdapter = CategoryAdapter(this, theloais)
        viewPager = findViewById(R.id.view_pager_2)
        recyclerView?.adapter = categoryAdapter
        getchitietturyen(linkTruyen)
        bottomNavigationView = findViewById(R.id.bottom)
        setUpdateViewPager()
        img = findViewById(R.id.arrow_back)
        img?.setOnClickListener {
            onBackPressed()
        }
        imgfavorite = findViewById(R.id.favorite)
        setUpdatefavorite(linkTruyen)
        imgChat = findViewById(R.id.chat)
        imgChat?.setOnClickListener(){
            var intent = Intent(this, BinhLuanTruyen::class.java)
            intent.putExtra("linktruyen",linkTruyen )
            intent.putExtra("iduser", iduser)
            println("iduser: $iduser")
            intent.putExtra("tentruyen", tentruyen)
            startActivity(intent)
        }

    }

    private fun setUpdatefavorite(linkTruyen: String){
        val url = linkTruyen
        val segments = url.split("-")
        val id = segments.last()
        var truyenyeuthich = db?.getTruyenYeuThichByIDTruyenAndIDUser(id.toInt(), iduser.toInt())
        if(truyenyeuthich != null){
            if(truyenyeuthich?.yeuthich == 1) {
                imgfavorite?.setImageResource(R.drawable.favorite)
            } else {
                imgfavorite?.setImageResource(R.drawable.favorite_white)
            }
        } else {
            imgfavorite?.setImageResource(R.drawable.favorite_white)
        }
        var truyen = db?.getTruyenDBByID(id.toInt())
        yeuthich.text = truyen?.luotyeuthich.toString()

        imgfavorite?.setOnClickListener {
            if (truyenyeuthich == null){
                db?.insertTruyenYeuThich(TruyenYeuThich(0,0,iduser.toInt(),id.toInt()))
                truyenyeuthich = db?.getTruyenYeuThichByIDTruyenAndIDUser(id.toInt(), iduser.toInt())
            }
            if(truyenyeuthich?.yeuthich == 1){
                db?.updateTruyenYeuThich(id.toInt(),iduser.toInt(),0)
                truyenyeuthich?.yeuthich = 0
                imgfavorite?.setImageResource(R.drawable.favorite_white)
                var truyen = db?.getTruyenDBByID(id.toInt())
                yeuthich.text = truyen?.luotyeuthich.toString()
                yeuthich.text = (truyen?.luotyeuthich!! - 1).toString()
                if(truyen?.luotyeuthich!! - 1 < 0){
                    yeuthich.text = "0"
                    db?.updateLuotYeuThich(id.toInt(), 0)

                } else {
                    db?.updateLuotYeuThich(id.toInt(), truyen.luotyeuthich - 1)
                }

            } else {
                truyenyeuthich?.yeuthich = 1
                db?.updateTruyenYeuThich(id.toInt(),iduser.toInt(),1)
                imgfavorite?.setImageResource(R.drawable.favorite)
                var truyen = db?.getTruyenDBByID(id.toInt())
                yeuthich.text = truyen?.luotyeuthich.toString()
                yeuthich.text = (truyen?.luotyeuthich!! + 1).toString()
                db?.updateLuotYeuThich(id.toInt(), truyen.luotyeuthich + 1)
            }
        }


    }

    private fun setUpdateViewPager(){
        val viewPagerAdapter = ViewPager2Adapter(this,linkTruyen,iduser)
        viewPager.adapter = viewPagerAdapter
        viewPager.isUserInputEnabled = false
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                bottomNavigationView.menu.getItem(position).isChecked = true
            }
        })
        bottomNavigationView.setOnItemSelectedListener{
            when(it.itemId){
                R.id.chapter_chi_tiet_truyen -> {
                    viewPager.currentItem = 0
                }
                R.id.gioithieu_chi_tiet_chuyen -> {
                    viewPager.currentItem = 1
                }
            }
            true
        }
    }

    override fun onResume() {
        super.onResume()
        var linkTruyen1 = intent.getStringExtra("linktruyen").toString()
        db = SQLiteHelper(application)
        val url = linkTruyen1
        val segments = url.split("-")
        val id = segments.last()
        var truyen = db?.getTruyenDBByID(id.toInt())
        view.text = truyen?.luotxem.toString()
        textchat?.text = db?.countCommentInTruyenDB(id.toInt()).toString()
        var count = db?.countCommentInTruyenDB(id.toInt())
        textchat?.text = count.toString()
        var sum = db?.sumStarCommentInTruyenDB(id.toInt())
        if(sum == null){
            textStar?.text = "0"
        } else {
            if(count != null && count != 0){
                val average = sum.toDouble() / count
                val roundedAverage = String.format("%.1f", average)
                textStar?.text = roundedAverage
            } else {
                textStar?.text = "0"
            }
        }
    }

    private fun getchitietturyen(linkTruyen : String){
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                db = SQLiteHelper(application)
                val url = linkTruyen
                val segments = url.split("-")
                val id = segments.last()
                val doc = Jsoup.connect(linkTruyen).get()
                val element = doc.select("main.main")
                val element1 = element.select("div.container > div.row > div.center-side > article#item-detail ")
                val mota = element1.select("div.detail-content > p").text()
                var time = element1.select("time.small").text()
                var linkanh = element1.select("div.detail-info > div.row > div.col-image > img").attr("src")
                var tentruyen = element1.select("div.detail-info > div.row > div.col-image > img").attr("alt")
                var element2 = element1.select("div.detail-info > div.row > div.col-info > ul")
                var tinhtrangtruyen = element2.select("li.status > p.col-xs-8").text()
                var tacgia = element2.select("li.author > p.col-xs-8").text()
                var element3 = element2.select("li.kind > p.col-xs-8")
                var element4 = element1.select("div.list-chapter > nav > ul > li ")

                var xau = ""
                for(value in element3){
                    var linktheloai = element3.select("a").attr("href")
                    var tentheloai = element3.select("a").text()
                    xau += tentheloai + ","
                    var theloai = TheLoai(linktheloai, tentheloai)
                    theloais.add(theloai)
                }


                xau = xau.removeSuffix(",")
                var check = db?.findTruyenById(id.toInt())

                if(check == false){
                    db?.insertTruyen(Truyen(id.toInt(),0,tentruyen,tacgia,time,mota,linkanh,0,0,linkTruyen,tinhtrangtruyen))

                }
                for (element in element4){
                    val tenchap = element.select("div.chapter > a").text()
                    if(db?.findChapterByTenChapter(tenchap,id.toInt()) != null){
                        break
                    }
                    val linkchap = element.select("div.chapter > a").attr("href")
                    val timechap = element.select("div.small").text()
                    db?.insertChapter(Chapter(0,tenchap,timechap,linkchap,id.toInt()))

                }
                launch(Dispatchers.Main) {
                    var truyen = db?.getTruyenDBByID(id.toInt())
                    yeuthich.text = truyen?.luotyeuthich.toString()
                    timeView.text = time
                    var count = db?.countCommentInTruyenDB(id.toInt())
                    textchat?.text = count.toString()
                    var sum = db?.sumStarCommentInTruyenDB(id.toInt())
                    if(sum == null){
                        textStar?.text = "0"
                    } else {
                        if(count != null && count != 0){
                            val average = sum.toDouble() / count
                            val roundedAverage = String.format("%.1f", average)
                            textStar?.text = roundedAverage
                        } else {
                            textStar?.text = "0"
                        }
                    }
                    tentruyenView.text = tentruyen
                    view.text = truyen?.luotxem.toString()
                    tinhtrangtruyenView.text = tinhtrangtruyen
                    Picasso.get().load(linkanh).into(linkanhView)
                    categoryAdapter?.notifyDataSetChanged()
                }
            } catch (e: IOException) {
                // Handle any exceptions here
                e.printStackTrace()
            }
        }
    }
}