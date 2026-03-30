package com.lifesurvivor.entity

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2

/**
 * プレイヤーステータス
 * 全ての基本パラメータを管理
 */
data class PlayerStats(
    var maxHp: Float = 100f,
    var hp: Float = 100f,
    var moveSpeed: Float = 120f,
    var armor: Float = 0f,
    var xpMultiplier: Float = 1f,
    var attackSpeedMultiplier: Float = 1f,
    var damageMultiplier: Float = 1f,
    var areaMultiplier: Float = 1f,
    var level: Int = 1,
    var xp: Float = 0f,
    var xpToNextLevel: Float = 10f,
    var xpPickupRange: Float = 50f,  // XP吸収範囲
    var hpRegenPerSec: Float = 0f,   // HP回復/秒
    var contactDamage: Float = 0f,   // 接触ダメージ反射
    var kills: Int = 0               // キル数
)

/**
 * プレイヤーエンティティ
 * 移動・描画・ステータス管理を担当
 */
class Player(x: Float, y: Float) {
    val position = Vector2(x, y)
    val velocity = Vector2(0f, 0f)
    val stats = PlayerStats()
    val size = 12f // 描画半径

    // プレイヤーが所持する武器IDとレベルのマップ
    val weaponLevels = mutableMapOf<String, Int>()

    // 無敵時間（被ダメージ後のiフレーム）
    var invincibleTimer = 0f
    private val invincibleDuration = 0.5f

    // 最後に移動した方向（武器の発射方向に使用）
    val facingDirection = Vector2(1f, 0f)

    /** ステータスを更新 */
    fun update(delta: Float) {
        // 移動
        position.x += velocity.x * stats.moveSpeed * delta
        position.y += velocity.y * stats.moveSpeed * delta

        // 向きを更新（移動中のみ）
        if (velocity.len2() > 0.01f) {
            facingDirection.set(velocity).nor()
        }

        // 無敵タイマー減少
        if (invincibleTimer > 0f) {
            invincibleTimer -= delta
        }

        // HP自然回復
        if (stats.hpRegenPerSec > 0f) {
            stats.hp = (stats.hp + stats.hpRegenPerSec * delta).coerceAtMost(stats.maxHp)
        }
    }

    /** ダメージを受ける */
    fun takeDamage(damage: Float): Boolean {
        if (invincibleTimer > 0f) return false

        val actualDamage = (damage - stats.armor).coerceAtLeast(1f)
        stats.hp -= actualDamage
        invincibleTimer = invincibleDuration

        return stats.hp <= 0f
    }

    /** XPを獲得 */
    fun gainXp(amount: Float): Boolean {
        stats.xp += amount * stats.xpMultiplier
        if (stats.xp >= stats.xpToNextLevel) {
            stats.xp -= stats.xpToNextLevel
            stats.level++
            // 次のレベルに必要なXP: 10 * level^1.3
            stats.xpToNextLevel = 10f * Math.pow(stats.level.toDouble(), 1.3).toFloat()
            return true // レベルアップした
        }
        return false
    }

    /** 武器を追加または強化 */
    fun addOrUpgradeWeapon(weaponId: String): Boolean {
        val currentLevel = weaponLevels[weaponId]
        return if (currentLevel == null) {
            weaponLevels[weaponId] = 1
            true // 新規武器獲得
        } else {
            weaponLevels[weaponId] = currentLevel + 1
            false // 既存武器強化
        }
    }

    /** ShapeRendererで描画 */
    fun render(renderer: ShapeRenderer) {
        // 無敵中は点滅
        if (invincibleTimer > 0f && (invincibleTimer * 10).toInt() % 2 == 0) {
            return
        }

        renderer.color = Color.WHITE
        renderer.circle(position.x, position.y, size)

        // 向いている方向を示す小さな三角形（目印）
        val dirX = facingDirection.x * size * 1.5f
        val dirY = facingDirection.y * size * 1.5f
        renderer.color = Color(0.8f, 0.9f, 1f, 1f)
        renderer.circle(position.x + dirX, position.y + dirY, 4f)
    }

    /** 生きているか */
    fun isAlive(): Boolean = stats.hp > 0f
}
