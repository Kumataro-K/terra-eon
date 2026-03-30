package com.lifesurvivor.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import com.lifesurvivor.LifeSurvivorGame
import com.lifesurvivor.data.TriviaDatabase

/**
 * タイトル画面
 * ゲーム名表示、スタートボタン、ランダム豆知識を表示
 */
class TitleScreen(private val game: LifeSurvivorGame) : ScreenAdapter() {

    private val shapeRenderer = ShapeRenderer()
    private val batch = SpriteBatch()
    private val font = BitmapFont()

    // 背景の星を表現するパーティクル
    private data class Star(var x: Float, var y: Float, var size: Float, var brightness: Float, var speed: Float)
    private val stars = mutableListOf<Star>()

    // タイトルアニメーション
    private var timer = 0f
    private val trivia = TriviaDatabase.getRandom()

    // ボタン座標
    private var startButtonX = 0f
    private var startButtonY = 0f
    private val startButtonWidth = 280f
    private val startButtonHeight = 50f

    init {
        // 背景の星を生成
        for (i in 0 until 100) {
            stars.add(Star(
                x = MathUtils.random(0f, 800f),
                y = MathUtils.random(0f, 480f),
                size = MathUtils.random(1f, 3f),
                brightness = MathUtils.random(0.3f, 1f),
                speed = MathUtils.random(5f, 20f)
            ))
        }
    }

    override fun render(delta: Float) {
        timer += delta
        val screenWidth = Gdx.graphics.width.toFloat()
        val screenHeight = Gdx.graphics.height.toFloat()
        val centerX = screenWidth / 2f

        // 深い宇宙色の背景
        Gdx.gl.glClearColor(0.02f, 0.02f, 0.08f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)

        // ボタン位置計算
        startButtonX = centerX - startButtonWidth / 2f
        startButtonY = screenHeight * 0.35f

        // === ShapeRenderer描画 ===
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)

        // 星の描画と更新
        for (star in stars) {
            val twinkle = (MathUtils.sin(timer * star.speed + star.x) + 1f) / 2f
            val alpha = star.brightness * (0.5f + 0.5f * twinkle)
            shapeRenderer.color = Color(1f, 1f, 0.9f, alpha)
            shapeRenderer.circle(star.x, star.y, star.size)

            // ゆっくり流れる
            star.x -= star.speed * 0.1f * delta
            if (star.x < -5f) {
                star.x = screenWidth + 5f
                star.y = MathUtils.random(0f, screenHeight)
            }
        }

        // タイトル背景の装飾（円弧）
        val pulseSize = 80f + MathUtils.sin(timer * 0.5f) * 10f
        shapeRenderer.color = Color(0.15f, 0.1f, 0.3f, 0.3f)
        shapeRenderer.circle(centerX, screenHeight * 0.72f, pulseSize)
        shapeRenderer.color = Color(0.1f, 0.2f, 0.15f, 0.2f)
        shapeRenderer.circle(centerX, screenHeight * 0.72f, pulseSize * 1.3f)

        // スタートボタン
        val btnPulse = 0.8f + MathUtils.sin(timer * 2f) * 0.2f
        shapeRenderer.color = Color(0.2f * btnPulse, 0.4f * btnPulse, 0.2f * btnPulse, 0.9f)
        shapeRenderer.rect(startButtonX, startButtonY, startButtonWidth, startButtonHeight)

        // ボタン枠
        shapeRenderer.color = Color(0.4f, 0.8f, 0.4f, 0.6f)
        shapeRenderer.rect(startButtonX, startButtonY, startButtonWidth, 2f)
        shapeRenderer.rect(startButtonX, startButtonY + startButtonHeight - 2f, startButtonWidth, 2f)
        shapeRenderer.rect(startButtonX, startButtonY, 2f, startButtonHeight)
        shapeRenderer.rect(startButtonX + startButtonWidth - 2f, startButtonY, 2f, startButtonHeight)

        // 豆知識パネル背景
        shapeRenderer.color = Color(0.96f, 0.90f, 0.78f, 0.15f)
        shapeRenderer.rect(centerX - 200f, screenHeight * 0.05f, 400f, 120f)

        shapeRenderer.end()

        // === テキスト描画 ===
        batch.begin()

        // メインタイトル
        font.color = Color(0.9f, 0.95f, 1f, 1f)
        font.draw(batch, "Life Survivor", centerX - 55f, screenHeight * 0.88f)

        // サブタイトル
        font.color = Color(0.7f, 0.8f, 0.6f, 0.9f)
        font.draw(batch, "生命38億年の戦い", centerX - 60f, screenHeight * 0.80f)

        // ゲーム説明
        font.color = Color(0.6f, 0.6f, 0.7f, 0.8f)
        font.draw(batch, "宇宙誕生から現代まで、生命の歴史を駆け抜けろ", centerX - 170f, screenHeight * 0.68f)
        font.draw(batch, "武器を集め、敵を倒し、生命史の知識を手に入れよう", centerX - 175f, screenHeight * 0.63f)

        // 操作説明
        font.color = Color(0.5f, 0.5f, 0.6f, 0.7f)
        font.draw(batch, "移動: WASD / 矢印キー / タッチ操作", centerX - 130f, screenHeight * 0.50f)
        font.draw(batch, "武器は自動攻撃！敵を倒してXPを集めよう", centerX - 145f, screenHeight * 0.45f)

        // スタートボタンテキスト
        font.color = Color(1f, 1f, 1f, 1f)
        font.draw(batch, "▶  ゲームスタート", centerX - 60f, startButtonY + 33f)

        // 画面下部に豆知識を表示
        font.color = Color(0.9f, 0.85f, 0.7f, 0.8f)
        font.draw(batch, "【${trivia.title}】${trivia.era}", centerX - 180f, screenHeight * 0.18f)
        font.color = Color(0.7f, 0.65f, 0.55f, 0.7f)
        val lines = wrapText(trivia.body, 40)
        var lineY = screenHeight * 0.14f
        for (line in lines) {
            font.draw(batch, line, centerX - 180f, lineY)
            lineY -= 16f
        }

        batch.end()

        // 入力処理
        if (Gdx.input.justTouched()) {
            val touchX = Gdx.input.x.toFloat()
            val touchY = screenHeight - Gdx.input.y.toFloat()
            if (touchX >= startButtonX && touchX <= startButtonX + startButtonWidth &&
                touchY >= startButtonY && touchY <= startButtonY + startButtonHeight
            ) {
                game.setScreen(GameScreen(game))
            }
        }

        // SPACEまたはENTERでもスタート可能
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(GameScreen(game))
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
            for (i in maxChars downTo (maxChars - 5).coerceAtLeast(0)) {
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
