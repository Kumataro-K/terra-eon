package com.lifesurvivor.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.lifesurvivor.LifeSurvivorGame
import com.lifesurvivor.data.TriviaEntry
import com.lifesurvivor.ui.TriviaCard

/**
 * 豆知識表示画面
 * 武器獲得時に独立した画面として豆知識を表示する
 *
 * 注: 実際の豆知識表示はGameScreen内のTriviaCardオーバーレイで処理される
 *     このクラスは独立画面として使用する場合の代替実装
 */
class TriviaScreen(
    private val game: LifeSurvivorGame,
    private val trivia: TriviaEntry,
    private val onDismiss: () -> Unit
) : ScreenAdapter() {

    private val shapeRenderer = ShapeRenderer()
    private val batch = SpriteBatch()
    private val font = BitmapFont()
    private val triviaCard = TriviaCard()

    init {
        triviaCard.show(trivia)
    }

    override fun render(delta: Float) {
        triviaCard.update(delta)

        val screenWidth = Gdx.graphics.width.toFloat()
        val screenHeight = Gdx.graphics.height.toFloat()

        // 暗い背景
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)

        // カード背景
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        triviaCard.renderBackground(shapeRenderer, screenWidth, screenHeight)
        shapeRenderer.end()

        // カードテキスト
        batch.begin()
        triviaCard.renderText(batch, font, screenWidth, screenHeight)
        batch.end()

        // タッチ入力
        if (Gdx.input.justTouched()) {
            val touchX = Gdx.input.x.toFloat()
            val touchY = screenHeight - Gdx.input.y.toFloat()
            if (triviaCard.isButtonTouched(touchX, touchY)) {
                onDismiss()
            }
        }
    }

    override fun dispose() {
        shapeRenderer.dispose()
        batch.dispose()
        font.dispose()
    }
}
