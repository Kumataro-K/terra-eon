package com.lifesurvivor.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import com.lifesurvivor.entity.Player

/**
 * 移動システム
 * キーボード入力からプレイヤーの移動を処理する
 * Android版ではタッチ入力をジョイスティックとして解釈する
 */
class MovementSystem {
    // タッチ入力用ジョイスティック
    private var touchOrigin: Vector2? = null
    private val joystickDeadZone = 20f

    /** プレイヤーの移動入力を更新 */
    fun update(player: Player) {
        val velocity = Vector2(0f, 0f)

        // キーボード入力（Desktop用）
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            velocity.y += 1f
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            velocity.y -= 1f
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            velocity.x -= 1f
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            velocity.x += 1f
        }

        // タッチ入力（Android用ジョイスティック）
        if (Gdx.input.isTouched) {
            val touchX = Gdx.input.x.toFloat()
            val touchY = Gdx.input.y.toFloat()

            if (touchOrigin == null) {
                touchOrigin = Vector2(touchX, touchY)
            }

            val origin = touchOrigin!!
            val dx = touchX - origin.x
            val dy = touchY - origin.y // Y軸は画面座標系なので反転不要（後で反転）

            if (dx * dx + dy * dy > joystickDeadZone * joystickDeadZone) {
                val dir = Vector2(dx, -dy).nor() // 画面Y軸を反転
                velocity.set(dir)
            }
        } else {
            touchOrigin = null
        }

        // 正規化して設定
        if (velocity.len2() > 0f) {
            velocity.nor()
        }
        player.velocity.set(velocity)
    }
}
