package com.lifesurvivor.system

import com.badlogic.gdx.math.Vector2
import com.lifesurvivor.entity.Enemy
import com.lifesurvivor.entity.Player
import com.lifesurvivor.entity.Projectile
import com.lifesurvivor.entity.ProjectileType

/**
 * 衝突検出システム
 * 弾 vs 敵、敵 vs プレイヤーの衝突を処理する
 */
class CollisionSystem {
    // XPオーブ: 位置、値、吸収中フラグ
    data class XpOrb(
        val position: Vector2,
        val value: Float,
        var beingCollected: Boolean = false,
        var collectTimer: Float = 0f
    )

    val xpOrbs = mutableListOf<XpOrb>()

    /**
     * 弾と敵の衝突を判定
     * @return 倒れた敵のリスト
     */
    fun checkProjectileEnemyCollisions(
        projectiles: MutableList<Projectile>,
        enemies: MutableList<Enemy>
    ): List<Enemy> {
        val killedEnemies = mutableListOf<Enemy>()

        for (projectile in projectiles) {
            if (!projectile.active) continue

            for (enemy in enemies) {
                if (!enemy.isAlive()) continue
                if (projectile.hitEnemies.contains(enemy)) continue

                val collisionDist = when (projectile.type) {
                    ProjectileType.BULLET -> enemy.data.size + 4f
                    ProjectileType.METEOR -> if (projectile.exploding) projectile.size else enemy.data.size + 6f
                    ProjectileType.WHIP -> enemy.data.size + 8f
                    ProjectileType.ROAR -> enemy.data.size + projectile.size * (projectile.timeAlive / projectile.lifetime).coerceAtMost(1f)
                    ProjectileType.SPORE -> enemy.data.size + projectile.size
                    ProjectileType.LAVA -> enemy.data.size + projectile.size
                }

                val dx = projectile.position.x - enemy.position.x
                val dy = projectile.position.y - enemy.position.y
                val distSq = dx * dx + dy * dy

                if (distSq < collisionDist * collisionDist) {
                    enemy.takeDamage(projectile.damage)
                    projectile.hitEnemies.add(enemy)

                    // 弾タイプによってノックバック
                    if (projectile.type == ProjectileType.ROAR) {
                        val knockDir = Vector2(enemy.position.x - projectile.position.x,
                            enemy.position.y - projectile.position.y)
                        enemy.applyKnockback(knockDir, 200f)
                    }

                    // 非貫通弾は消滅（ただし範囲攻撃系は残る）
                    if (!projectile.piercing &&
                        projectile.type != ProjectileType.SPORE &&
                        projectile.type != ProjectileType.LAVA &&
                        projectile.type != ProjectileType.ROAR &&
                        !projectile.exploding
                    ) {
                        projectile.active = false
                    }

                    if (!enemy.isAlive()) {
                        killedEnemies.add(enemy)
                    }
                }
            }
        }

        return killedEnemies
    }

    /**
     * 敵とプレイヤーの衝突を判定
     * @return プレイヤーが死亡したらtrue
     */
    fun checkEnemyPlayerCollisions(
        enemies: List<Enemy>,
        player: Player,
        delta: Float
    ): Boolean {
        for (enemy in enemies) {
            if (!enemy.isAlive()) continue

            val collisionDist = player.size + enemy.data.size
            val dx = player.position.x - enemy.position.x
            val dy = player.position.y - enemy.position.y
            val distSq = dx * dx + dy * dy

            if (distSq < collisionDist * collisionDist) {
                val dead = player.takeDamage(enemy.data.damage * delta)

                // 三葉虫アーマーの接触ダメージ反射
                if (player.stats.contactDamage > 0f) {
                    enemy.takeDamage(player.stats.contactDamage * delta)
                }

                if (dead) return true
            }
        }
        return false
    }

    /**
     * XPオーブの更新と収集判定
     * @return レベルアップしたらtrue
     */
    fun updateXpOrbs(player: Player, delta: Float): Boolean {
        var leveledUp = false
        val iterator = xpOrbs.iterator()

        while (iterator.hasNext()) {
            val orb = iterator.next()

            val dx = player.position.x - orb.position.x
            val dy = player.position.y - orb.position.y
            val distSq = dx * dx + dy * dy
            val pickupRange = player.stats.xpPickupRange

            // 吸収範囲内に入ったら引き寄せ開始
            if (distSq < pickupRange * pickupRange) {
                orb.beingCollected = true
            }

            if (orb.beingCollected) {
                // プレイヤーに向かって移動
                val dir = Vector2(dx, dy).nor()
                orb.position.x += dir.x * 300f * delta
                orb.position.y += dir.y * 300f * delta

                // 十分近づいたら収集
                val newDx = player.position.x - orb.position.x
                val newDy = player.position.y - orb.position.y
                if (newDx * newDx + newDy * newDy < 100f) {
                    if (player.gainXp(orb.value)) {
                        leveledUp = true
                    }
                    iterator.remove()
                    continue
                }

                orb.collectTimer += delta
                if (orb.collectTimer > 10f) {
                    iterator.remove()
                }
            }
        }

        return leveledUp
    }

    /** 敵を倒した時にXPオーブをスポーンする */
    fun spawnXpOrb(position: Vector2, value: Float) {
        xpOrbs.add(XpOrb(Vector2(position.x, position.y), value))
    }
}
