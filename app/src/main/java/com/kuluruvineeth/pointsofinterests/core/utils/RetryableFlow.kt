package com.kuluruvineeth.pointsofinterests.core.utils

import kotlinx.coroutines.flow.*


fun <T> retryableFlow(retryTrigger: RetryTrigger, flowProvider: () -> Flow<T>) =
    retryTrigger.retryEvent.flatMapConcat { flowProvider() }


class RetryTrigger{

    internal val retryEvent = MutableStateFlow(true)

    fun retry(){
        retryEvent.value = retryEvent.value.not()
    }
}