package com.lifesurvivor.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.lifesurvivor.data.EraData
import com.lifesurvivor.entity.Player

/**
 * ヘッドアップディスプレイ
 * HP・XP・タイマー・キル数・時代名を表示
 *
 * レイアウト:
 * [HP ████████░░ 80/100]  [Lv.5]  [02:34]
 * [XP ████░░░░░░ 40%    ]  [x2.5k kills]
 */
class HUD {
    private val barWidth = 200f
    private val barHeight = 16f
    private val padding = 10f

    /** ShapeRendererでバーを描画 */
    fun renderBars(renderer: ShapeRenderer, player: Player, screenWidth: Float, screenHeight: Float) {
        val x = padding
        val topY = screenHeight - padding

        // === HPバー ===
        val hpY = topY - barHeight
        // 背景（暗い赤）
        renderer.color = Color(0.3f, 0.1f, 0.1f, 1f)
        renderer.rect(x, hpY, barWidth, barHeight)
        // HPゲージ（赤グラデーション）
        val hpRatio = (player.stats.hp / player.stats.maxHp).coerceIn(0f, 1f)
        renderer.color = Color(
            0.8f + 0.2f * (1f - hpRatio), // 低HPほど明るい赤
            0.2f * hpRatio,
            0.1f,
            1f
        )
        renderer.rect(x, hpY, barWidth * hpRatio, barHeight)

        // === XPバー ===
        val xpY = hpY - barHeight - 4f
        // 背景（暗い青）
        renderer.color = Color(0.1f, 0.1f, 0.3f, 1f)
        renderer.rect(x, xpY, barWidth, barHeight)
        // XPゲージ（青〜水色）
        val xpRatio = if (player.stats.xpToNextLevel > 0f) {
            (player.stats.xp / player.stats.xpToNextLevel).coerceIn(0f, 1f)
        } else 0f
        renderer.color = Color(0.2f, 0.5f + 0.3f * xpRatio, 0.9f, 1f)
        renderer.rect(x, xpY, barWidth * xpRatio, barHeight)
    }

    /** SpriteBatchでテキストを描画 */
    fun renderText(
        batch: SpriteBatch,
        font: BitmapFont,
        player: Player,
        elapsedTime: Float,
        screenWidth: Float,
        screenHeight: Float
    ) {
        val x = padding
        val topY = screenHeight - padding

        // HP数値
        val hpY = topY - 2f
        font.color = Color.WHITE
        font.draw(batch, "HP ${player.stats.hp.toInt()}/${player.stats.maxHp.toInt()}", x + 4f, hpY)

        // レベル表示（HPバーの右側）
        font.draw(batch, "Lv.${player.stats.level}", x + barWidth + 16f, hpY)

        // XP数値
        val xpY = hpY - barHeight - 4f
        val xpPercent = if (player.stats.xpToNextLevel > 0f) {
            (player.stats.xp / player.stats.xpToNextLevel * 100f).toInt()
        } else 0
        font.draw(batch, "XP $xpPercent%", x + 4f, xpY)

        // タイマー（右上）
        val minutes = (elapsedTime / 60f).toInt()
        val seconds = (elapsedTime % 60f).toInt()
        val timeText = String.format("%02d:%02d", minutes, seconds)
        font.draw(batch, timeText, screenWidth - 80f, topY - 2f)

        // キル数（右下寄り）
        val killText = formatKills(player.stats.kills)
        font.draw(batch, killText, screenWidth - 100f, topY - barHeight - 6f)

        // 現在の時代名（中央上部）
        val era = EraData.getEraByTime(elapsedTime)
        val eraText = era.name
        font.color = Color(1f, 0.9f, 0.7f, 1f)
        font.draw(batch, eraText, screenWidth / 2f - eraText.length * 4f, topY - 2f)
        font.color = Color.WHITE
    }

    /** キル数をフォーマット（1000以上はkで表示） */
    private fun formatKills(kills: Int): String {
        return when {
            kills >= 1000000 -> String.format("x%.1fM kills", kills / 1000000f)
            kills >= 1000 -> String.format("x%.1fk kills", kills / 1000f)
            else -> "x$kills kills"
        }
    }
}
