package com.kuluruvineeth.pointsofinterests.core.utils

import kotlinx.coroutines.flow.*


fun <T> retryableFlow(retryTrigger: RetryTrigger, flowProvider: () -> Flow<T>) =
    retryTrigger.retryEvent.filter { it == RetryTrigger.State.RETRYING }
        .flatMapConcat { flowProvider() }
        .onEach { retryTrigger.retryEvent.value = RetryTrigger.State.IDLE }


class RetryTrigger{
    enum class State {RETRYING, IDLE}

    internal val retryEvent = MutableStateFlow(State.RETRYING)

    fun retry(){
        retryEvent.value = State.RETRYING
    }
}