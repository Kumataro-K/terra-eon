package com.lifesurvivor.system

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.lifesurvivor.data.WeaponDefinitions
import com.lifesurvivor.data.WeaponType
import com.lifesurvivor.entity.Player
import com.lifesurvivor.entity.Projectile
import com.lifesurvivor.entity.ProjectileType

/**
 * 攻撃システム
 * プレイヤーが所持する武器の自動攻撃を制御する
 */
class AttackSystem {
    // 各武器のクールダウンタイマー
    private val cooldownTimers = mutableMapOf<String, Float>()

    /**
     * 武器の自動攻撃を更新
     * @return 新しく生成された弾のリスト
     */
    fun update(delta: Float, player: Player): List<Projectile> {
        val newProjectiles = mutableListOf<Projectile>()

        for ((weaponId, level) in player.weaponLevels) {
            val weaponData = WeaponDefinitions.getById(weaponId) ?: continue

            // パッシブ武器は弾を生成しない
            if (weaponData.type == WeaponType.PASSIVE) continue

            // クールダウン管理
            val timer = cooldownTimers.getOrPut(weaponId) { 0f }
            val cooldown = weaponData.baseCooldown / player.stats.attackSpeedMultiplier
            cooldownTimers[weaponId] = timer + delta

            if (cooldownTimers[weaponId]!! < cooldown) continue
            cooldownTimers[weaponId] = 0f

            // レベルに応じたダメージ計算
            val damage = weaponData.baseDamage * (1f + (level - 1) * 0.3f) * player.stats.damageMultiplier
            val area = weaponData.baseArea * player.stats.areaMultiplier

            // 武器ごとの弾生成
            when (weaponId) {
                "photosynthesis_whip" -> {
                    newProjectiles.addAll(createWhipProjectiles(player, damage, area, level))
                }
                "oxygen_burst" -> {
                    newProjectiles.addAll(createOxygenBurstProjectiles(player, damage, area, level, weaponData.projectileSpeed))
                }
                "extinction_meteor" -> {
                    newProjectiles.addAll(createMeteorProjectiles(player, damage, area, level, weaponData.projectileSpeed))
                }
                "carboniferous_spore" -> {
                    newProjectiles.addAll(createSporeProjectiles(player, damage, area, level))
                }
                "dinosaur_roar" -> {
                    newProjectiles.addAll(createRoarProjectiles(player, damage, area, level))
                }
                "pt_boundary_volcano" -> {
                    newProjectiles.addAll(createVolcanoProjectiles(player, damage, area, level))
                }
            }
        }

        return newProjectiles
    }

    /** 光合成ウィップ: 近距離円弧攻撃、360度ランダム方向 */
    private fun createWhipProjectiles(player: Player, damage: Float, area: Float, level: Int): List<Projectile> {
        val count = 1 + level / 3 // レベルに応じてムチの数増加
        val projectiles = mutableListOf<Projectile>()

        for (i in 0 until count) {
            val startAngle = MathUtils.random(0f, MathUtils.PI2)
            val proj = Projectile(
                x = player.position.x,
                y = player.position.y,
                direction = Vector2(1f, 0f),
                speed = 0f,
                damage = damage,
                size = area,
                type = ProjectileType.WHIP,
                color = Color(0.3f, 1f, 0.3f, 0.8f),
                lifetime = 0.4f,
                weaponId = "photosynthesis_whip",
                piercing = true
            )
            proj.arcAngle = startAngle
            proj.arcSpeed = MathUtils.PI2 * 2f // 2回転/秒
            proj.arcOrigin = Vector2(player.position.x, player.position.y)
            projectiles.add(proj)
        }

        return projectiles
    }

    /** 酸素バースト: 全方向8方向に弾を発射 */
    private fun createOxygenBurstProjectiles(player: Player, damage: Float, area: Float, level: Int, speed: Float): List<Projectile> {
        val directions = 8 + level // レベルに応じて弾数増加
        val projectiles = mutableListOf<Projectile>()

        for (i in 0 until directions) {
            val angle = (MathUtils.PI2 / directions) * i
            val dir = Vector2(MathUtils.cos(angle), MathUtils.sin(angle))

            projectiles.add(Projectile(
                x = player.position.x,
                y = player.position.y,
                direction = dir,
                speed = speed,
                damage = damage,
                size = area,
                type = ProjectileType.BULLET,
                color = Color(0.5f, 0.8f, 1f, 1f),
                lifetime = 2f,
                weaponId = "oxygen_burst"
            ))
        }

        return projectiles
    }

    /** 絶滅隕石: 画面上部からランダム位置に落下、爆発範囲攻撃 */
    private fun createMeteorProjectiles(player: Player, damage: Float, area: Float, level: Int, speed: Float): List<Projectile> {
        val count = 1 + level / 2 // レベルに応じて隕石数増加
        val projectiles = mutableListOf<Projectile>()

        for (i in 0 until count) {
            // 着弾位置: プレイヤー周辺のランダム位置
            val targetX = player.position.x + MathUtils.random(-200f, 200f)
            val targetY = player.position.y + MathUtils.random(-200f, 200f)

            // 開始位置: 着弾位置の上方
            val startY = targetY + 400f

            val proj = Projectile(
                x = targetX + MathUtils.random(-50f, 50f),
                y = startY,
                direction = Vector2(0f, -1f),
                speed = speed,
                damage = damage,
                size = area,
                type = ProjectileType.METEOR,
                color = Color(1f, 0.4f, 0f, 1f),
                lifetime = 5f,
                weaponId = "extinction_meteor",
                piercing = true
            )
            proj.targetPosition = Vector2(targetX, targetY)
            projectiles.add(proj)
        }

        return projectiles
    }

    /** 大森林の胞子: 周囲にゆっくり広がる毒霧エリア */
    private fun createSporeProjectiles(player: Player, damage: Float, area: Float, level: Int): List<Projectile> {
        val proj = Projectile(
            x = player.position.x,
            y = player.position.y,
            direction = Vector2(0f, 0f),
            speed = 0f,
            damage = damage * (1f + level * 0.2f),
            size = area * (1f + level * 0.15f),
            type = ProjectileType.SPORE,
            color = Color(0.2f, 0.8f, 0.1f, 0.4f),
            lifetime = 10f,
            weaponId = "carboniferous_spore",
            piercing = true
        )
        proj.persistDuration = 3f + level * 0.5f
        return listOf(proj)
    }

    /** 恐竜の咆哮: 前方円錐形の衝撃波、複数敵をノックバック */
    private fun createRoarProjectiles(player: Player, damage: Float, area: Float, level: Int): List<Projectile> {
        val dir = Vector2(player.facingDirection)
        if (dir.len2() < 0.01f) dir.set(1f, 0f)

        return listOf(Projectile(
            x = player.position.x + dir.x * 10f,
            y = player.position.y + dir.y * 10f,
            direction = dir.nor(),
            speed = 150f + level * 20f,
            damage = damage,
            size = area * (1f + level * 0.1f),
            type = ProjectileType.ROAR,
            color = Color(0.8f, 0.6f, 0.2f, 0.7f),
            lifetime = 0.6f,
            weaponId = "dinosaur_roar",
            piercing = true
        ))
    }

    /** P-T境界の火山: 設置型の溶岩エリア、継続ダメージ */
    private fun createVolcanoProjectiles(player: Player, damage: Float, area: Float, level: Int): List<Projectile> {
        val count = 1 + level / 3
        val projectiles = mutableListOf<Projectile>()

        for (i in 0 until count) {
            val offsetX = MathUtils.random(-100f, 100f)
            val offsetY = MathUtils.random(-100f, 100f)

            val proj = Projectile(
                x = player.position.x + offsetX,
                y = player.position.y + offsetY,
                direction = Vector2(0f, 0f),
                speed = 0f,
                damage = damage * (1f + level * 0.25f),
                size = area * (1f + level * 0.1f),
                type = ProjectileType.LAVA,
                color = Color(1f, 0.3f, 0f, 0.6f),
                lifetime = 10f,
                weaponId = "pt_boundary_volcano",
                piercing = true
            )
            proj.persistDuration = 4f + level * 0.5f
            projectiles.add(proj)
        }

        return projectiles
    }

    /** パッシブ武器の効果を適用 */
    fun applyPassiveEffects(player: Player) {
        // 基本値にリセット
        player.stats.armor = 0f
        player.stats.contactDamage = 0f
        player.stats.xpPickupRange = 50f
        player.stats.hpRegenPerSec = 0f

        for ((weaponId, level) in player.weaponLevels) {
            when (weaponId) {
                "trilobite_armor" -> {
                    // 防御力+15（レベルアップで増加）、接触ダメージ反射
                    player.stats.armor += 15f + (level - 1) * 5f
                    player.stats.contactDamage += 5f + (level - 1) * 3f
                }
                "dna_helix" -> {
                    // XP吸収範囲+50%（レベルアップで増加）、HP回復
                    player.stats.xpPickupRange += 50f * level
                    player.stats.hpRegenPerSec += 1f + (level - 1) * 0.5f
                }
            }
        }
    }
}
