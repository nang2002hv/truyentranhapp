package com.example.btl1.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.btl1.fragrment.ChapterChiTietTruyen
import com.example.btl1.fragrment.GioiThieuChiTietTruyen


class ViewPager2Adapter(fragment : FragmentActivity,private val linktruyen : String, private var iduser : String) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {
                ChapterChiTietTruyen.newInstance(linktruyen, iduser)
            }
            1 -> {
                GioiThieuChiTietTruyen.newInstance(linktruyen)
            }

            else -> ChapterChiTietTruyen.newInstance(linktruyen, iduser)
        }
    }
}