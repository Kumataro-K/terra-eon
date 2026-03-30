package com.lifesurvivor.desktop

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.lifesurvivor.LifeSurvivorGame

/**
 * Desktop版ランチャー
 * LWJGL3バックエンドでLibGDXゲームを起動する
 */
fun main() {
    val config = Lwjgl3ApplicationConfiguration().apply {
        setTitle("Life Survivor: 生命38億年の戦い")
        setWindowedMode(800, 480)
        setResizable(true)
        useVsync(true)
        setForegroundFPS(60)
    }

    Lwjgl3Application(LifeSurvivorGame(), config)
}
