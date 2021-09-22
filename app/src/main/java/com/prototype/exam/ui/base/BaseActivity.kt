package com.prototype.exam.ui.base

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.prototype.exam.utils.Constants
import java.util.regex.Pattern


abstract class BaseActivity : AppCompatActivity() {
    abstract fun setupUi()
    abstract fun setupObservers()
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            val text = editable.toString()
            if(text.isNotEmpty() && !Pattern.matches(Constants.INPUT_VALIDATION_LOGIN_REGEX, text)) {
                editable?.delete(text.length - 1, text.length)
            }
            afterTextChanged.invoke(text)
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
