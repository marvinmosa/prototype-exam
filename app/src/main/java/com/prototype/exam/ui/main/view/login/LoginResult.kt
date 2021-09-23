package com.prototype.exam.ui.main.view.login

import androidx.annotation.StringRes

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: Boolean? = null,
    val error: String? = null
)