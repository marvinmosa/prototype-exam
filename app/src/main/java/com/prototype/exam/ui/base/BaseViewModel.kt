package com.prototype.exam.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    sealed class SingleEvent {
        data class  ErrorEvent(val message: String): SingleEvent()
    }

    private val eventChannel = Channel<SingleEvent>()
    val eventFlow = eventChannel.receiveAsFlow()



    fun triggerErrorEvent(message: String) = viewModelScope.launch {
        eventChannel.send(SingleEvent.ErrorEvent(message))
    }
}