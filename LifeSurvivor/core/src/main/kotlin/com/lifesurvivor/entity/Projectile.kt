package com.lifesurvivor.entity

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2

/**
 * 弾の種類
 */
enum class ProjectileType {
    BULLET,     // 直進弾（酸素バーストなど）
    METEOR,     // 落下型（絶滅隕石）
    WHIP,       // 円弧型（光合成ウィップ）
    ROAR,       // 円錐型（恐竜の咆哮）
    SPORE,      // 範囲持続型（大森林の胞子）
    LAVA        // 設置型（P-T境界の火山）
}

/**
 * 弾エンティティ
 * 武器から発射される攻撃判定を持つオブジェクト
 */
class Projectile(
    x: Float,
    y: Float,
    val direction: Vector2,
    val speed: Float,
    val damage: Float,
    val size: Float,
    val type: ProjectileType,
    val color: Color,
    val lifetime: Float = 3f,     // 生存時間（秒）
    val weaponId: String = "",    // 発射元の武器ID
    val piercing: Boolean = false // 貫通するか
) {
    val position = Vector2(x, y)
    var timeAlive = 0f
    var active = true

    // 隕石用: 着弾位置と爆発状態
    var targetPosition: Vector2? = null
    var exploding = false
    var explosionTimer = 0f
    val explosionDuration = 0.5f

    // 溶岩・胞子用: 持続時間
    var persistTimer = 0f
    var persistDuration = 3f

    // 円弧用: 角度
    var arcAngle = 0f
    var arcSpeed = 0f
    var arcOrigin: Vector2? = null

    // ヒット済み敵のセット（貫通弾の多重ヒット防止）
    val hitEnemies = mutableSetOf<Enemy>()

    /** フレーム更新 */
    fun update(delta: Float) {
        timeAlive += delta

        when (type) {
            ProjectileType.BULLET -> {
                // 直進
                position.x += direction.x * speed * delta
                position.y += direction.y * speed * delta
                if (timeAlive >= lifetime) active = false
            }

            ProjectileType.METEOR -> {
                if (!exploding) {
                    // 着弾位置へ落下
                    val target = targetPosition ?: run { active = false; return }
                    val toTarget = Vector2(target.x - position.x, target.y - position.y)
                    if (toTarget.len2() < 100f) {
                        // 着弾 → 爆発
                        position.set(target)
                        exploding = true
                        explosionTimer = 0f
                    } else {
                        toTarget.nor().scl(speed * delta)
                        position.add(toTarget)
                    }
                } else {
                    explosionTimer += delta
                    if (explosionTimer >= explosionDuration) {
                        active = false
                    }
                }
            }

            ProjectileType.WHIP -> {
                // 円弧攻撃: プレイヤー周囲を回転
                val origin = arcOrigin ?: run { active = false; return }
                arcAngle += arcSpeed * delta
                position.x = origin.x + size * kotlin.math.cos(arcAngle)
                position.y = origin.y + size * kotlin.math.sin(arcAngle)
                if (timeAlive >= lifetime) active = false
            }

            ProjectileType.ROAR -> {
                // 円錐衝撃波: 前方に広がりながら進む
                position.x += direction.x * speed * delta
                position.y += direction.y * speed * delta
                if (timeAlive >= lifetime) active = false
            }

            ProjectileType.SPORE -> {
                // 毒霧: その場に留まって持続ダメージ
                persistTimer += delta
                if (persistTimer >= persistDuration) active = false
            }

            ProjectileType.LAVA -> {
                // 溶岩エリア: 設置型持続ダメージ
                persistTimer += delta
                if (persistTimer >= persistDuration) active = false
            }
        }
    }

    /** ShapeRendererで描画 */
    fun render(renderer: ShapeRenderer) {
        if (!active) return

        renderer.color = color

        when (type) {
            ProjectileType.BULLET -> {
                renderer.circle(position.x, position.y, 4f)
            }

            ProjectileType.METEOR -> {
                if (exploding) {
                    // 爆発エフェクト: 拡大する円
                    val progress = explosionTimer / explosionDuration
                    val radius = size * (0.5f + progress * 0.5f)
                    renderer.color = Color(1f, 0.5f, 0f, 1f - progress * 0.5f)
                    renderer.circle(position.x, position.y, radius)
                } else {
                    // 落下中
                    renderer.circle(position.x, position.y, 6f)
                }
            }

            ProjectileType.WHIP -> {
                renderer.color = Color(0.3f, 1f, 0.3f, 0.8f)
                renderer.circle(position.x, position.y, 8f)
            }

            ProjectileType.ROAR -> {
                // 衝撃波: 拡大する半透明円
                val expandSize = size * (timeAlive / lifetime).coerceAtMost(1f)
                renderer.color = Color(0.8f, 0.6f, 0.2f, 1f - timeAlive / lifetime)
                renderer.circle(position.x, position.y, expandSize)
            }

            ProjectileType.SPORE -> {
                // 毒霧: 緑色の半透明円
                val alpha = 0.4f * (1f - persistTimer / persistDuration)
                renderer.color = Color(0.2f, 0.8f, 0.1f, alpha)
                renderer.circle(position.x, position.y, size)
            }

            ProjectileType.LAVA -> {
                // 溶岩: 赤橙の半透明円
                val alpha = 0.6f * (1f - persistTimer / persistDuration)
                renderer.color = Color(1f, 0.3f, 0.0f, alpha)
                renderer.circle(position.x, position.y, size)
            }
        }
    }
}
