package data

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE

@OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
class HttpClientFactory internal constructor(
    private val httpClientEngine: HttpClientEngine? = null,
) {

    fun createClient(isDebug: Boolean): HttpClient = createBaseClient().config {
        install(ContentEncoding)

        install(Logging) {
            logger = Logger.SIMPLE
            level = getLogLevel(isDebug)
        }
    }
    private fun createBaseClient() =
        if (httpClientEngine != null) HttpClient(httpClientEngine) else HttpClient()
}

private fun getLogLevel(debug: Boolean): LogLevel =
    if (debug) LogLevel.HEADERS else LogLevel.NONE

fun provideHttpClient(
    httpClientFactory: HttpClientFactory,
): HttpClient = httpClientFactory.createClient(isDebug = true)
