package com.example.btl1.fragrment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.btl1.Model.ChapterModel
import com.example.btl1.Model.TruyenDaDoc
import com.example.btl1.Model.TruyenModel
import com.example.btl1.R
import com.example.btl1.adapters.AdapterLichSuTruyen
import com.example.btl1.adapters.AdaptersTruyenHome
import com.example.btl1.dal.SQLiteHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LichSuTruyen : Fragment() {

    private var adapterLichSuTruyen: AdapterLichSuTruyen ? = null
    private var recyclerTruyen: RecyclerView? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var db: SQLiteHelper? = null
    private var img : ImageView? = null
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
            LichSuTruyen().apply {
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
        return inflater.inflate(R.layout.fragment_lichsu_truyen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerTruyen = view.findViewById(R.id.recyclerView)
        gridLayoutManager = GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
        recyclerTruyen?.layoutManager = gridLayoutManager
        img = view.findViewById(R.id.imageView)
        db = SQLiteHelper(requireContext())
        var listTruyen = ArrayList<TruyenModel>()
        var truyenDaDocs = db?.getAllTruyenDaDocByIDUser(iduser.toInt())

        for (truyenDaDoc in truyenDaDocs!!){
            var listChapterModel = ArrayList<ChapterModel>()
            var truyen = db?.getTruyenDBByID(truyenDaDoc.Truyen_id)
            var chaptermodel = ChapterModel(truyenDaDoc.id,0,"","","")
            listChapterModel.add(chaptermodel)
            listTruyen.add(TruyenModel(truyen!!.id,truyen.tentruyen,truyen.theloaitruyen,listChapterModel,truyen.linkhinh))
        }
        db?.close()
        adapterLichSuTruyen = AdapterLichSuTruyen(requireContext(), listTruyen,iduser, this ::onTruyencDeleteClick)
        recyclerTruyen?.adapter = adapterLichSuTruyen
        adapterLichSuTruyen?.notifyDataSetChanged()


    }
    override fun onResume() {
        super.onResume()
        db = SQLiteHelper(requireContext())
        var listTruyen = ArrayList<TruyenModel>()
        var truyenDaDocs = db?.getAllTruyenDaDocByIDUser(iduser.toInt())
        var listChapterModel = ArrayList<ChapterModel>()
        for (truyenDaDoc in truyenDaDocs!!){
            var listChapterModel = ArrayList<ChapterModel>()
            var truyen = db?.getTruyenDBByID(truyenDaDoc.Truyen_id)
            var chaptermodel = ChapterModel(truyenDaDoc.id,0,"","","")
            listChapterModel.add(chaptermodel)
            listTruyen.add(TruyenModel(truyen!!.id,truyen.tentruyen,truyen.theloaitruyen,listChapterModel,truyen.linkhinh))
        }
        adapterLichSuTruyen = AdapterLichSuTruyen(requireContext(), listTruyen,iduser, this ::onTruyencDeleteClick)
        recyclerTruyen?.adapter = adapterLichSuTruyen
        adapterLichSuTruyen?.notifyDataSetChanged()
    }

    private fun onTruyencDeleteClick(id: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("Xóa Truyện")
            .setMessage("Bạn có muốn xóa truyện này ra khỏi lịch sử không")
            .setPositiveButton("Yes") { _, _ ->
                lifecycleScope.launch(Dispatchers.IO) {
                    db = SQLiteHelper(requireContext())
                    db?.removeLichSuTruyenByID(id)
                    var listTruyen = ArrayList<TruyenModel>()
                    var truyenDaDocs = db?.getAllTruyenDaDocByIDUser(iduser.toInt())
                    var listChapterModel = ArrayList<ChapterModel>()
                    for (truyenDaDoc in truyenDaDocs!!){
                        var listChapterModel = ArrayList<ChapterModel>()
                        var truyen = db?.getTruyenDBByID(truyenDaDoc.Truyen_id)
                        var chaptermodel = ChapterModel(truyenDaDoc.id,0,"","","")
                        listChapterModel.add(chaptermodel)
                        listTruyen.add(TruyenModel(truyen!!.id,truyen.tentruyen,truyen.theloaitruyen,listChapterModel,truyen.linkhinh))
                    }
                    adapterLichSuTruyen = AdapterLichSuTruyen(requireContext(), listTruyen,iduser)
                    withContext(Dispatchers.Main) {
                        recyclerTruyen?.adapter = adapterLichSuTruyen
                        adapterLichSuTruyen?.notifyDataSetChanged()
                    }
                }
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

}
