package com.example.newscmp.core.network

import io.ktor.client.call.body
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException

sealed interface Result<out D, out E> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Failure<out E: Error>(val error: E) : Result<Nothing, E>
}

sealed interface Error {
    enum class NetworkError: Error {
        INVALID_REQUEST,
        UNAUTHORIZED,
        CORS_ERROR,
        TOO_MANY_REQUESTS,
        TIMEOUT,
        NO_INTERNET,
        UNKNOWN
    }

    enum class LocalError: Error {
        UNKNOWN
    }
}

suspend inline fun <reified D> safeCall(
    execute : () -> HttpResponse
) : Result<D, Error.NetworkError> {
    val response = try {
        execute()
    } catch (_: SocketTimeoutException) {
        return Result.Failure(error = Error.NetworkError.TIMEOUT)
    }catch (_: UnresolvedAddressException) {
        return Result.Failure(error = Error.NetworkError.NO_INTERNET)
    }catch (_: Exception) {
        return Result.Failure(error = Error.NetworkError.UNKNOWN)
    }

    return responseToResult(response)
}

suspend inline fun <reified T> responseToResult(
    httpResponse: HttpResponse
) : Result<T, Error.NetworkError> {
    return when(httpResponse.status.value) {
        in 200..299 -> {
            Result.Success(httpResponse.body<T>())
        }

        400, 404 -> Result.Failure(error = Error.NetworkError.INVALID_REQUEST)

        401 -> Result.Failure(error = Error.NetworkError.UNAUTHORIZED)

        403 -> Result.Failure(error = Error.NetworkError.CORS_ERROR)

        429 -> Result.Failure(error = Error.NetworkError.TOO_MANY_REQUESTS)

        else -> {
            Result.Failure(error = Error.NetworkError.UNKNOWN)
        }
    }
}

inline fun <D, E: Error, R> Result<D, E>.map(map : (D) -> R) : Result<R, E> {
    return when(this) {
        is Result.Failure -> Result.Failure(this.error)
        is Result.Success -> Result.Success(map(this.data))
    }
}