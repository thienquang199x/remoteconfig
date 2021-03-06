package co.ntq.remoteconfig

import co.ntq.remoteconfig.locals.StorageResourceLocalRepository
import co.ntq.remoteconfig.remotes.HttpGETResourceRemoteRepository
import me.giacoppo.remoteconfig.core.*

fun initRemoteConfig(init: RemoteConfig.() -> Unit) {
    RemoteConfig.initialize(init)
}

inline fun <reified T: Any> RemoteConfig.remoteResource(
        localRepository: ResourceLocalRepository,
        remoteRepository: ResourceRemoteRepository) {
    initRemoteResource(T::class.java) {
        resourceLocalRepository = localRepository
        resourceRemoteRepository = remoteRepository
    }
}

inline fun <reified T: Any> RemoteConfig.remoteResource(
        localRepository: ResourceLocalRepository,
        remoteRepository: ResourceRemoteRepository,
        noinline init: RemoteResource<T>.() -> Unit) {
    initRemoteResource(T::class.java) {
        resourceLocalRepository = localRepository
        resourceRemoteRepository = remoteRepository
        init(this)
    }
}

fun RemoteConfigContext.network(url: String): ResourceRemoteRepository =
        HttpGETResourceRemoteRepository(url)

fun RemoteConfigContext.storage(dir: String): ResourceLocalRepository =
    StorageResourceLocalRepository(dir)

inline fun <reified T: Any> remoteConfig(nameOfConfig: String) = RemoteConfig.of<T>(nameOfConfig)
inline fun <reified T: Any> remoteConfig() = RemoteConfig.of<T>(T::class.java)