package com.android.vacationsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.vacationsapp.fragments.VacationFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.main_layout, VacationFragment()).commit()
    }
}