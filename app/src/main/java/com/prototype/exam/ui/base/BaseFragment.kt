package com.prototype.exam.ui.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


abstract class BaseFragment(contentLayoutId: Int) : Fragment(contentLayoutId) {

    abstract fun setupUi()
    abstract fun setupObservers()

    sealed class SingleEvent {
        data class  ErrorEvent(val message: String): SingleEvent()
    }

    protected val eventChannel = Channel<SingleEvent>()
    val eventFlow = eventChannel.receiveAsFlow()

    abstract fun triggerErrorEvent(message:String): Job
}
