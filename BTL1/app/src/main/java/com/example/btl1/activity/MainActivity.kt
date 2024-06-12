package com.example.btl1.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.btl1.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.viewpager2.widget.ViewPager2
import com.example.btl1.adapters.ViewPagerAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager : ViewPager2
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var iduser : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        iduser = intent.getStringExtra("iduser").toString()
        viewPager = findViewById(R.id.view_pager)
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        setUpdateViewPager()
    }

    private fun setUpdateViewPager(){

        val viewPagerAdapter = ViewPagerAdapter(this,iduser)
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
                R.id.home -> {
                    viewPager.currentItem = 0
                }
                R.id.history -> {
                    viewPager.currentItem = 1
                }
                R.id.menu -> {
                    viewPager.currentItem = 2
                }
                R.id.account -> {
                    viewPager.currentItem = 3
                }
            }
            true
        }
    }


}
