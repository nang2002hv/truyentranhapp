package com.example.btl1.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.btl1.fragrment.GD_User
import com.example.btl1.fragrment.HomeTruyen
import com.example.btl1.fragrment.LichSuTruyen
import com.example.btl1.fragrment.MenuTruyen

class ViewPagerAdapter(fragment : FragmentActivity,private val iduser : String) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {
                HomeTruyen.newInstance(iduser)
            }
            1 -> {
                LichSuTruyen.newInstance(iduser)
            }
            2 -> {
                MenuTruyen.newInstance(iduser)
            }
            3 -> {
                GD_User.newInstance(iduser)
            }
            else -> HomeTruyen()
        }
    }
}