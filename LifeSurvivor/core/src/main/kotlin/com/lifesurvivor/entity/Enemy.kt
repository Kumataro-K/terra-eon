package com.lifesurvivor.entity

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.lifesurvivor.data.EnemyData

/**
 * 敵エンティティ
 * 敵データに基づいて動作する個体を表す
 */
class Enemy(
    val data: EnemyData,
    x: Float,
    y: Float
) {
    val position = Vector2(x, y)
    var hp: Float = data.hp
    var knockbackVelocity = Vector2(0f, 0f)
    var knockbackTimer = 0f

    // ダメージを受けた時のフラッシュ
    var hitFlashTimer = 0f

    /** プレイヤーに向かって移動する */
    fun update(delta: Float, playerPos: Vector2) {
        // ノックバック中は通常移動しない
        if (knockbackTimer > 0f) {
            position.x += knockbackVelocity.x * delta
            position.y += knockbackVelocity.y * delta
            knockbackTimer -= delta
            return
        }

        // プレイヤーに向かって移動
        val dir = Vector2(playerPos.x - position.x, playerPos.y - position.y)
        if (dir.len2() > 1f) {
            dir.nor()
            position.x += dir.x * data.speed * delta
            position.y += dir.y * data.speed * delta
        }

        // ヒットフラッシュ減少
        if (hitFlashTimer > 0f) {
            hitFlashTimer -= delta
        }
    }

    /** ダメージを受ける */
    fun takeDamage(damage: Float) {
        hp -= damage
        hitFlashTimer = 0.1f
    }

    /** ノックバックを適用 */
    fun applyKnockback(direction: Vector2, force: Float) {
        knockbackVelocity.set(direction).nor().scl(force)
        knockbackTimer = 0.2f
    }

    /** 生きているか */
    fun isAlive(): Boolean = hp > 0f

    /** ShapeRendererで描画 */
    fun render(renderer: ShapeRenderer) {
        // ヒットフラッシュ中は白く光る
        if (hitFlashTimer > 0f) {
            renderer.color = com.badlogic.gdx.graphics.Color.WHITE
        } else {
            renderer.color = data.color
        }

        renderer.circle(position.x, position.y, data.size)

        // ボスは外側にリングを描画
        if (data.isBoss) {
            renderer.color = com.badlogic.gdx.graphics.Color(1f, 0.8f, 0f, 0.5f)
            renderer.circle(position.x, position.y, data.size + 3f)
        }
    }

    /** プレイヤーとの距離の2乗を計算（パフォーマンス用） */
    fun distanceSq(playerPos: Vector2): Float {
        val dx = position.x - playerPos.x
        val dy = position.y - playerPos.y
        return dx * dx + dy * dy
    }
}
