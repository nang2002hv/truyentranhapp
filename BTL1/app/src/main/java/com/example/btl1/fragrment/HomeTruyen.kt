package com.example.btl1.fragrment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.btl1.Model.ChapterModel
import com.example.btl1.Model.TruyenModel
import com.example.btl1.R
import com.example.btl1.adapters.AdaptersTruyenHome
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import java.io.IOException

class HomeTruyen : Fragment() {

    private  var i = 0
    private var adaptersTruyenHome: AdaptersTruyenHome? = null
    private val listTruyen = ArrayList<TruyenModel>()
    private var recyclerTruyen: RecyclerView? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private lateinit var iduser : String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home_truyen, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            iduser = it.getString("iduser").toString()
        }
    }

    companion object{
        @JvmStatic
        fun newInstance(iduser: String) =
            HomeTruyen().apply {
                arguments = Bundle().apply {
                    putString("iduser", iduser)

                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerTruyen = view.findViewById(R.id.home_truyen)
        gridLayoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        recyclerTruyen?.layoutManager = gridLayoutManager
        recyclerTruyen?.setHasFixedSize(true)
        adaptersTruyenHome = AdaptersTruyenHome(requireContext(), listTruyen,iduser)
        recyclerTruyen?.adapter = adaptersTruyenHome
        getAllTruyen()
        val recyclerView = recyclerTruyen
        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                // Kiểm tra nếu đã cuộn đến cuối danh sách
                if (lastVisibleItemPosition == totalItemCount - 1) {
                    getAllTruyen()
                }
            }
        })
    }

    private fun getAllTruyen() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                var j = 0;
                var url = "https://nettruyenfull.com/?page=" + i
                val doc = Jsoup.connect(url).get()
                val element = doc.select("main.main").first()
                val element1 = element.select("div.container > div.row > div.center-side > div.Module > div.ModuleContent > div.items >div.row > div.item")

                for (value in element1) {
                    val element2 = value.select("figure.clearfix > div.image").first()
                    val element3 = value.select("figcaption > ul > li")
                    val tentruyen = element2.select("a").attr("title")
                    val linkhinhtruyen = element2.select("img").attr("data-original")
                    val linktruyen = element2.select("a").attr("href")
                    var i = getIdFromUrl(linktruyen)?.toInt() ?: 0

                    val chapterList = ArrayList<ChapterModel>() // Tạo một danh sách mới cho mỗi truyện
                    for (value1 in element3) {
                        j = j+1
                        val tenchap = value1.select("a").text()
                        val linkchap = value1.select("a").attr("href")
                        val timechap = value1.select("i").text()
                        chapterList.add(ChapterModel(j,i,tenchap, linkchap, timechap)) // Thêm ChapterModel vào danh sách
                    }

                    listTruyen.add(TruyenModel(i,tentruyen, linktruyen, chapterList, linkhinhtruyen)) // Thêm TruyenModel vào danh sách
                }

                // After data is fetched, set up the RecyclerView and its adapter on the main thread
                launch(Dispatchers.Main) {
                    adaptersTruyenHome?.notifyDataSetChanged()
                }
            } catch (e: IOException) {
                // Handle any exceptions here
                e.printStackTrace()
            }
        }
    }

    fun getIdFromUrl(url: String): String? {
        val parts = url.split("-")
        val id = parts.lastOrNull()
        return id
    }
}
