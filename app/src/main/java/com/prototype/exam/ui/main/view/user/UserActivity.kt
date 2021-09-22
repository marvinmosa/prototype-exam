package com.prototype.exam.ui.main.view.user

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.prototype.exam.App
import com.prototype.exam.R
import com.prototype.exam.databinding.ActivityMainBinding
import com.prototype.exam.ui.base.BaseActivity
import com.prototype.exam.ui.main.view.login.LoginActivity
import com.prototype.exam.utils.Constants

class UserActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_user_detail) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_user_detail)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
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
        sharedPref?.edit()?.putString(Constants.SHARED_PREFERENCE_LOGIN_KEY, null)?.apply()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
        return true
    }
}

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        App.appComponent.inject(this)
//
//        setupUi()
//    }
//    override fun setupUi() {
//        val toolbar = findViewById<Toolbar>(R.id.toolbar)
//        setSupportActionBar(toolbar)
//        toolbar.setNavigationOnClickListener {
//            onBackPressed()
//        }
//    }

//    override fun setupObservers() {
//        //Do nothing
//    }
//
//    fun setToolbarTitle(@StringRes titleString: Int) {
//       // supportActionBar?.title = resources.getString(titleString)
//    }
//
//    fun onShowBackButton(isDisplay: Boolean) {
////        supportActionBar?.setDisplayHomeAsUpEnabled(isDisplay)
////        supportActionBar?.setDisplayShowHomeEnabled(isDisplay)
//    }
//

//}