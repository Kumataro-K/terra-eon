package com.lifesurvivor.android

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.lifesurvivor.LifeSurvivorGame

/**
 * Android版ランチャー
 * AndroidApplicationを継承してLibGDXゲームを起動する
 */
class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val config = AndroidApplicationConfiguration().apply {
            // パフォーマンス設定
            useAccelerometer = false
            useCompass = false
            useGyroscope = false
            // アンチエイリアス
            numSamples = 2
        }

        initialize(LifeSurvivorGame(), config)
    }
}
