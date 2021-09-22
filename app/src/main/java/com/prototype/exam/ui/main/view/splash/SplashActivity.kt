package com.prototype.exam.ui.main.view.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.prototype.exam.App
import com.prototype.exam.ui.main.view.login.LoginActivity
import com.prototype.exam.ui.main.view.user.UserActivity
import com.prototype.exam.ui.main.viewModel.SplashViewModel
import com.prototype.exam.utils.ViewModelFactory
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
        splashViewModel = ViewModelProvider(this, viewModelFactory).get(SplashViewModel::class.java)
        handleActivityNavigation()
    }

    private fun handleActivityNavigation() {
        val intent = if (splashViewModel.isLoggedIn()) {
            Intent(this, UserActivity::class.java)
        } else {
            Intent(this, LoginActivity::class.java)
        }

        startActivity(intent)
        finish()
    }
}