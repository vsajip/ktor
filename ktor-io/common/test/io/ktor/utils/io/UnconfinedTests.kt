/*
* Copyright 2014-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
*/

package io.ktor.utils.io

import io.ktor.utils.io.core.*
import kotlinx.coroutines.*
import kotlin.coroutines.*
import kotlin.test.*

private class SingleStepContinuation : Continuation<Unit> {
    lateinit var result: Any
    override val context: CoroutineContext get() = Dispatchers.Unconfined

    override fun resumeWith(result: Result<Unit>) {
        this.result = result
    }

    fun assertSuccess(task: suspend () -> Unit) {
        task.startCoroutine(this)
        assertEquals(Result.success(Unit), result)
    }
}
