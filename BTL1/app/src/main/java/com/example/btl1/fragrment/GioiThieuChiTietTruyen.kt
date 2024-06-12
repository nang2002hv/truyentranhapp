package com.example.btl1.fragrment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.btl1.Model.ChapterModel
import com.example.btl1.R
import com.example.btl1.adapters.AdaptersChapterTruyen
import com.example.btl1.dal.SQLiteHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import java.io.IOException


class GioiThieuChiTietTruyen : Fragment() {
    private lateinit var linkTruyen: String
    private lateinit var noidung : TextView
    private lateinit var db : SQLiteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            linkTruyen = it.getString("linktruyen").toString()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(linktruyen: String) =
            GioiThieuChiTietTruyen().apply {
                arguments = Bundle().apply {
                    putString("linktruyen", linktruyen)

                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noidung = view.findViewById(R.id.noidung)
        getAllChapter(linkTruyen)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gioi_thieu_chi_tiet_truyen, container, false)
    }

    private fun getAllChapter(linktruyen : String){
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val url = linkTruyen
                val segments = url.split("-")
                val id = segments.last()
                db = SQLiteHelper(requireContext())
                val truyen = db.getTruyenDBByID(id.toInt())
                launch(Dispatchers.Main) {
                    noidung.text = truyen.mo_ta
                }
            } catch (e: IOException) {
                // Handle any exceptions here
                e.printStackTrace()
            }
        }

    }


}