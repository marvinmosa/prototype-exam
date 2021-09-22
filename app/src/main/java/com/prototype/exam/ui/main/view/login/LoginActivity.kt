package com.prototype.exam.ui.main.view.login

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Button
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.prototype.exam.App
import com.prototype.exam.databinding.ActivityLoginBinding
import com.prototype.exam.ui.base.BaseActivity
import com.prototype.exam.ui.base.afterTextChanged
import com.prototype.exam.ui.main.view.user.UserActivity
import com.prototype.exam.ui.main.viewModel.LoginViewModel
import com.prototype.exam.utils.ViewModelFactory
import javax.inject.Inject

class LoginActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var username: TextInputLayout
    private lateinit var password: TextInputLayout
    private lateinit var login: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loginViewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
        setupUi()
        setupObservers()
    }

    override fun setupUi() {
        username = binding.usernameLayout!!
        password = binding.passwordLayout!!
        login = binding.login

        username.editText?.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.editText?.text.toString(),
                password.editText?.text.toString()
            )
        }

        password.editText?.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.editText?.text.toString(),
                    password.editText?.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            username.editText?.text.toString(),
                            password.editText?.text.toString()
                        )
                }
                false
            }
        }

        login.setOnClickListener {
            loginViewModel.login(
                username.editText?.text.toString(),
                password.editText?.text.toString()
            )
        }
    }

    override fun setupObservers() {
        loginViewModel.loginFormState.observe(this, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            } else {
                username.error = null
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            } else {
                password.error = null
            }
        })

        loginViewModel.loginResult.observe(this, Observer {
            val loginResult = it ?: return@Observer
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                startActivity(Intent(this, UserActivity::class.java))
                finish()
            }
        })
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Snackbar.make(binding.root, errorString, Snackbar.LENGTH_LONG).show()
    }
}