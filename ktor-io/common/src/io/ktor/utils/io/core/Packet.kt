package io.ktor.utils.io.core

import io.ktor.utils.io.errors.*

public expect class EOFException(message: String) : IOException
