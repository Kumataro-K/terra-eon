package com.lifesurvivor.system

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.lifesurvivor.data.EnemyData
import com.lifesurvivor.data.EnemyDefinitions
import com.lifesurvivor.data.EraData
import com.lifesurvivor.entity.Enemy

/**
 * スポーン設定
 * 経過時間に応じた敵のスポーンレートと最大数
 */
data class SpawnConfig(
    val rate: Float,       // 1秒あたりのスポーン数
    val maxOnScreen: Int   // 画面上の最大敵数
)

/**
 * 敵スポーンシステム
 * 時間経過に応じて敵を生成する
 */
class EnemySpawnSystem {
    // 経過時間ごとのスポーン設定
    private val spawnTable = mapOf(
        0 to SpawnConfig(rate = 1.0f, maxOnScreen = 30),
        120 to SpawnConfig(rate = 2.0f, maxOnScreen = 60),
        300 to SpawnConfig(rate = 3.5f, maxOnScreen = 100),
        600 to SpawnConfig(rate = 6.0f, maxOnScreen = 200),
        900 to SpawnConfig(rate = 10.0f, maxOnScreen = 300)
    )

    private var spawnAccumulator = 0f
    private var bossSpawned = mutableSetOf<String>() // スポーン済みボスの時代ID

    /** 現在のスポーン設定を取得 */
    private fun getCurrentConfig(elapsedTime: Float): SpawnConfig {
        val sec = elapsedTime.toInt()
        var config = SpawnConfig(1.0f, 30)
        for ((threshold, cfg) in spawnTable) {
            if (sec >= threshold) {
                config = cfg
            }
        }
        return config
    }

    /**
     * 敵のスポーンを更新
     * @return 新しく生成された敵のリスト
     */
    fun update(
        delta: Float,
        elapsedTime: Float,
        currentEnemyCount: Int,
        playerPos: Vector2,
        screenWidth: Float,
        screenHeight: Float
    ): List<Enemy> {
        val config = getCurrentConfig(elapsedTime)
        val newEnemies = mutableListOf<Enemy>()

        // 現在の時代に応じた敵データを取得
        val era = EraData.getEraByTime(elapsedTime)
        val availableEnemies = EnemyDefinitions.getEnemiesByEra(era.id)

        if (availableEnemies.isEmpty()) return newEnemies

        // スポーンレートに応じて敵を生成
        spawnAccumulator += config.rate * delta

        while (spawnAccumulator >= 1f && currentEnemyCount + newEnemies.size < config.maxOnScreen) {
            spawnAccumulator -= 1f

            // ランダムに敵タイプを選択
            val enemyData = availableEnemies.random()

            // 画面外のランダムな位置にスポーン
            val spawnPos = getSpawnPosition(playerPos, screenWidth, screenHeight)
            newEnemies.add(Enemy(enemyData, spawnPos.x, spawnPos.y))
        }

        // ボスのスポーン判定（各時代の終わりにボスが出現）
        if (era.id !in bossSpawned) {
            val timeInEra = elapsedTime - era.startTimeSec
            // 時代の80%が経過したらボスをスポーン
            if (timeInEra >= era.durationSec * 0.8f) {
                val bossData = EnemyDefinitions.getBossByEra(era.id)
                if (bossData != null) {
                    val spawnPos = getSpawnPosition(playerPos, screenWidth, screenHeight)
                    newEnemies.add(Enemy(bossData, spawnPos.x, spawnPos.y))
                    bossSpawned.add(era.id)
                }
            }
        }

        return newEnemies
    }

    /** 画面外のランダム位置を生成 */
    private fun getSpawnPosition(playerPos: Vector2, screenWidth: Float, screenHeight: Float): Vector2 {
        val margin = 50f
        val halfW = screenWidth / 2f + margin
        val halfH = screenHeight / 2f + margin

        // 4辺のうちランダムに1辺を選択
        return when (MathUtils.random(3)) {
            0 -> Vector2(playerPos.x - halfW, playerPos.y + MathUtils.random(-halfH, halfH)) // 左
            1 -> Vector2(playerPos.x + halfW, playerPos.y + MathUtils.random(-halfH, halfH)) // 右
            2 -> Vector2(playerPos.x + MathUtils.random(-halfW, halfW), playerPos.y + halfH) // 上
            else -> Vector2(playerPos.x + MathUtils.random(-halfW, halfW), playerPos.y - halfH) // 下
        }
    }
}
