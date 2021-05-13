package com.rpaysolution.aaybyay.activity

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.rpaysolution.aaybyay.R
import com.rpaysolution.aaybyay.fragment.About
import com.rpaysolution.aaybyay.fragment.History
import com.rpaysolution.aaybyay.fragment.Home
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var navBar:BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(R.id.frame, Home()).commit()

        navBar = findViewById(R.id.navBar)

        navBar.setOnNavigationItemSelectedListener {

            when(it.itemId){

                R.id.home -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame, Home()).commit()
                }

                R.id.history -> {
                    supportActionBar?.title = "Expense History"
                    supportFragmentManager.beginTransaction().replace(R.id.frame, History()).commit()
                }

                R.id.about -> {
                    supportActionBar?.title = "About us"
                    supportFragmentManager.beginTransaction().replace(R.id.frame, About()).commit()
                }

                else ->{
                    supportFragmentManager.beginTransaction().replace(R.id.frame, Home()).commit()

                }
            }
            return@setOnNavigationItemSelectedListener true
        }


    }

    override fun onBackPressed() {
        val frag=supportFragmentManager.findFragmentById(R.id.frame)

        if(frag !is Home)
        {
            supportFragmentManager.beginTransaction().replace(R.id.frame, Home()).commit()
            navBar.selectedItemId = R.id.home
            supportActionBar?.title = "AayByay"
        }else
            super.onBackPressed()
    }
}