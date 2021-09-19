package com.prototype.exam.ui.main.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.prototype.exam.App
import com.prototype.exam.R
import com.prototype.exam.ui.base.BaseActivity
import com.prototype.exam.ui.main.view.login.LoginActivity
import com.prototype.exam.utils.Constants

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                logout()
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout() : Boolean {
        val sharedPref = baseContext?.getSharedPreferences(Constants.SHARED_PREFERENCE_LOGIN, MODE_PRIVATE)
        sharedPref?.let { it.edit().putString(Constants.SHARED_PREFERENCE_LOGIN_KEY, null).apply()  }
        startActivity(Intent(this, LoginActivity::class.java))
       // overridePendingTransition(R.anim.slide_out, R.anim.slide_in)
        return true
    }
}