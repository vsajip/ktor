/*
* Copyright 2014-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
*/

package io.ktor.utils.io

/**
 * Print exception stacktrace.
 */
@Deprecated(
    level = DeprecationLevel.ERROR,
    message = "Use `printStackTrace` instead",
    replaceWith = ReplaceWith("printStackTrace")
)
public expect fun Throwable.printStack()
