package com.ntq.remoteconfig

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import co.ntq.remoteconfig.*

class MainActivity : AppCompatActivity() {

    private val remoteAppConfig by lazy { remoteConfig<AppConfig>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRemoteConfig {
            remoteResource<AppConfig>(
                storage(filesDir.absolutePath),
                network("http://192.168.1.5:3000/remoteConfigs")
            )
        }
        remoteAppConfig.setDefaultConfig(AppConfig())
        remoteAppConfig.fetch({
            remoteAppConfig.activateFetched()
        }, {
        })
    }
}