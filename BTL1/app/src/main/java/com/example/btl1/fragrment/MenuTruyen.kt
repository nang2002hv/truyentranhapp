package com.example.btl1.fragrment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.btl1.Model.ChapterModel
import com.example.btl1.Model.TruyenModel
import com.example.btl1.R
import com.example.btl1.adapters.AdaptersTruyenHome
import com.example.btl1.dal.SQLiteHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import java.io.IOException

class MenuTruyen : Fragment() {
    private var adaptersTruyenHome: AdaptersTruyenHome? = null
    private val listTruyen = ArrayList<TruyenModel>()
    private var recyclerTruyen: RecyclerView? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var textView : TextView? = null
    private var button : Button? = null
    private lateinit var iduser : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            iduser = it.getString("iduser").toString()
        }
    }

    companion object{
        @JvmStatic
        fun newInstance(iduser: String) =
            MenuTruyen().apply {
                arguments = Bundle().apply {
                    putString("iduser", iduser)

                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu_truyen, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerTruyen = view.findViewById(R.id.recycler_view)
        gridLayoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        recyclerTruyen?.layoutManager = gridLayoutManager
        recyclerTruyen?.setHasFixedSize(true)
        adaptersTruyenHome = AdaptersTruyenHome(requireContext(), listTruyen,iduser)
        var textView = view.findViewById<TextView>(R.id.editTextSearch)
        recyclerTruyen?.adapter = adaptersTruyenHome
        button = view.findViewById(R.id.buttonSearch)
        button?.setOnClickListener {
            val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(textView?.windowToken, 0)

            val keyword = textView.text.toString()
            val arr = keyword?.split("\\s+")
            val url = "https://nettruyenfull.com/tim-truyen?keyword=" + arr?.joinToString("+")
            listTruyen.clear()
            adaptersTruyenHome?.notifyDataSetChanged()
            getAllTruyen(url)
        }

    }


    private fun getAllTruyen(url : String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                var i = 0
                i = i + 1

                val doc = Jsoup.connect(url).get()
                val element = doc.select("main.main").first()
                val element1 = element.select("div.container > div.row > div.center-side > div.Module > div.ModuleContent > div.items >div.row > div.item")

                for (value in element1) {
                    val element2 = value.select("figure.clearfix > div.image").first()
                    val element3 = value.select("figcaption > ul > li")
                    val tentruyen = element2.select("a").attr("title")
                    val linkhinhtruyen = element2.select("img").attr("data-original")
                    val linktruyen = element2.select("a").attr("href")

                    val chapterList = ArrayList<ChapterModel>() // Tạo một danh sách mới cho mỗi truyện
                    for (value1 in element3) {
                        val tenchap = value1.select("a").text()
                        val linkchap = value1.select("a").attr("href")
                        val timechap = value1.select("i").text()
                        chapterList.add(ChapterModel(0,0,tenchap, linkchap, timechap)) // Thêm ChapterModel vào danh sách
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
}
