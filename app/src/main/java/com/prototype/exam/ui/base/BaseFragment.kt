package com.prototype.exam.ui.base

import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow


abstract class BaseFragment: Fragment(), PermissionCallback {

    abstract fun setupUi()
    abstract fun setupObservers()

    sealed class SingleEvent {
        data class ErrorEvent(val message: String) : SingleEvent()
    }

    protected val eventChannel = Channel<SingleEvent>()
    val eventFlow = eventChannel.receiveAsFlow()

    abstract fun triggerErrorEvent(message: String): Job

    internal val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                onUserGranted()
            } else {
                onUserDenied()
            }
        }
}

interface PermissionCallback {
    fun onUserGranted() {}
    fun onUserDenied() {}
}