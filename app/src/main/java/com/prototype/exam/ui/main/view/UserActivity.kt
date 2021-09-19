package com.prototype.exam.ui.main.view

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.prototype.exam.App
import com.prototype.exam.R
import com.prototype.exam.ui.base.BaseActivity

class UserActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.appComponent.inject(this)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    fun onShowBackButton(isDisplay: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(isDisplay)
        supportActionBar?.setDisplayShowHomeEnabled(isDisplay)
    }
}