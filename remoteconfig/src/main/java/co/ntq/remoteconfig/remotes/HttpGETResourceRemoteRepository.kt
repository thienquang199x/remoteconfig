package co.ntq.remoteconfig.remotes

import me.giacoppo.remoteconfig.core.ResourceRemoteRepository
import co.ntq.remoteconfig.exceptions.HttpException
import com.squareup.okhttp.*
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.Executors

class HttpGETResourceRemoteRepository(
    private val url: String
) : ResourceRemoteRepository {
    private val executorService = Executors.newSingleThreadExecutor()
    private val dispatcher = Dispatcher(executorService)

    private val client: OkHttpClient = OkHttpClient().setDispatcher(dispatcher)

    override fun fetch(
        success: (InputStream) -> Unit,
        fail: (Exception) -> Unit
    ) {
        val req = Request.Builder().url(url).build()
        val call = client.newCall(req)
        invokeInternal(call, success, fail)
    }

    private fun invokeInternal(
        call: Call,
        success: (InputStream) -> Unit,
        fail: (Exception) -> Unit
    ) {
        call.enqueue(object : Callback {
            override fun onFailure(request: Request?, e: IOException) {
                fail.invoke(e)
            }

            override fun onResponse(response: Response) {
                if (response.isSuccessful) {
                    success.invoke(response.body().byteStream())
                } else {
                    fail.invoke(HttpException(response.code(), response.message()))
                }
                executorService.shutdown()
            }

        })
    }

    companion object {
        fun create(url: String): ResourceRemoteRepository = HttpGETResourceRemoteRepository(url)
    }
}
