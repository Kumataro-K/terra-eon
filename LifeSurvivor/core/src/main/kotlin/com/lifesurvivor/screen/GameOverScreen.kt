package com.lifesurvivor.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.lifesurvivor.LifeSurvivorGame
import com.lifesurvivor.data.EraData
import com.lifesurvivor.data.TriviaDatabase
import com.lifesurvivor.entity.PlayerStats

/**
 * ゲームオーバー画面
 * スコアの表示とリトライ/タイトルへ戻るボタンを提供
 */
class GameOverScreen(
    private val game: LifeSurvivorGame,
    private val stats: PlayerStats,
    private val elapsedTime: Float
) : ScreenAdapter() {

    private val shapeRenderer = ShapeRenderer()
    private val batch = SpriteBatch()
    private val font = BitmapFont()

    // ゲームオーバー時にランダムな豆知識を表示
    private val randomTrivia = TriviaDatabase.getRandom()

    // ボタン配置
    private var retryButtonY = 0f
    private var titleButtonY = 0f
    private val buttonWidth = 250f
    private val buttonHeight = 40f

    override fun render(delta: Float) {
        val screenWidth = Gdx.graphics.width.toFloat()
        val screenHeight = Gdx.graphics.height.toFloat()
        val centerX = screenWidth / 2f

        // 暗い赤の背景
        Gdx.gl.glClearColor(0.15f, 0.05f, 0.05f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)

        // ボタン座標の計算
        retryButtonY = screenHeight * 0.25f
        titleButtonY = retryButtonY - 55f

        // ShapeRenderer描画
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)

        // スコアパネルの背景
        shapeRenderer.color = Color(0.1f, 0.1f, 0.15f, 0.8f)
        shapeRenderer.rect(centerX - 200f, screenHeight * 0.4f, 400f, 200f)

        // リトライボタン
        shapeRenderer.color = Color(0.3f, 0.15f, 0.15f, 0.9f)
        shapeRenderer.rect(centerX - buttonWidth / 2f, retryButtonY, buttonWidth, buttonHeight)

        // タイトルボタン
        shapeRenderer.color = Color(0.15f, 0.15f, 0.3f, 0.9f)
        shapeRenderer.rect(centerX - buttonWidth / 2f, titleButtonY, buttonWidth, buttonHeight)

        shapeRenderer.end()

        // テキスト描画
        batch.begin()

        // GAME OVER タイトル
        font.color = Color(1f, 0.3f, 0.3f, 1f)
        font.draw(batch, "GAME OVER", centerX - 45f, screenHeight * 0.85f)

        // 絶滅メッセージ
        font.color = Color(0.8f, 0.7f, 0.6f, 1f)
        font.draw(batch, "あなたの生命は途絶えた...", centerX - 90f, screenHeight * 0.75f)

        // スコア詳細
        val era = EraData.getEraByTime(elapsedTime)
        val minutes = (elapsedTime / 60f).toInt()
        val seconds = (elapsedTime % 60f).toInt()

        font.color = Color.WHITE
        val scoreY = screenHeight * 0.62f
        font.draw(batch, "到達時代: ${era.name}", centerX - 160f, scoreY)
        font.draw(batch, "生存時間: ${String.format("%02d:%02d", minutes, seconds)}", centerX - 160f, scoreY - 25f)
        font.draw(batch, "到達レベル: ${stats.level}", centerX - 160f, scoreY - 50f)
        font.draw(batch, "撃破数: ${formatKills(stats.kills)}", centerX - 160f, scoreY - 75f)

        // 豆知識（ゲームオーバー画面でも教育コンテンツ表示）
        font.color = Color(0.9f, 0.85f, 0.7f, 1f)
        font.draw(batch, "【${randomTrivia.title}】${randomTrivia.era}", centerX - 180f, screenHeight * 0.38f)
        font.color = Color(0.75f, 0.7f, 0.6f, 1f)
        // 本文を折り返して表示
        val lines = wrapText(randomTrivia.body, 35)
        var lineY = screenHeight * 0.35f
        for (line in lines) {
            font.draw(batch, line, centerX - 180f, lineY)
            lineY -= 18f
        }

        // ボタンテキスト
        font.color = Color.WHITE
        font.draw(batch, "もう一度挑戦する", centerX - 55f, retryButtonY + 28f)
        font.draw(batch, "タイトルに戻る", centerX - 45f, titleButtonY + 28f)

        batch.end()

        // 入力処理
        if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            val touchY = screenHeight - Gdx.input.y.toFloat()

            if (touchY >= retryButtonY && touchY <= retryButtonY + buttonHeight) {
                game.setScreen(GameScreen(game))
            } else if (touchY >= titleButtonY && touchY <= titleButtonY + buttonHeight) {
                game.setScreen(TitleScreen(game))
            }
        }

        // SPACEキーでリトライ
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.setScreen(GameScreen(game))
        }
    }

    /** キル数をフォーマット */
    private fun formatKills(kills: Int): String {
        return when {
            kills >= 1000000 -> String.format("%.1fM", kills / 1000000f)
            kills >= 1000 -> String.format("%.1fk", kills / 1000f)
            else -> kills.toString()
        }
    }

    /** テキスト折り返し */
    private fun wrapText(text: String, maxChars: Int): List<String> {
        val lines = mutableListOf<String>()
        var remaining = text
        while (remaining.isNotEmpty()) {
            if (remaining.length <= maxChars) {
                lines.add(remaining)
                break
            }
            var breakPos = maxChars
            for (i in maxChars downTo maxChars - 5) {
                if (i < remaining.length && (remaining[i] == '。' || remaining[i] == '、')) {
                    breakPos = i + 1
                    break
                }
            }
            breakPos = breakPos.coerceAtMost(remaining.length)
            lines.add(remaining.substring(0, breakPos))
            remaining = remaining.substring(breakPos)
        }
        return lines
    }

    override fun dispose() {
        shapeRenderer.dispose()
        batch.dispose()
        font.dispose()
    }
}
