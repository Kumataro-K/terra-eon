package com.lifesurvivor

import com.badlogic.gdx.Game
import com.lifesurvivor.screen.TitleScreen

/**
 * Life Survivor: 生命38億年の戦い
 *
 * メインゲームクラス
 * LibGDX Gameクラスを継承し、スクリーン管理を行う
 *
 * Vampire Survivorsスタイルの自動攻撃・大量敵処理型サバイバーゲーム
 * 宇宙誕生から現代までの生命史をテーマにした豆知識が随所に登場する
 */
class LifeSurvivorGame : Game() {

    override fun create() {
        // タイトル画面からスタート
        setScreen(TitleScreen(this))
    }
}
