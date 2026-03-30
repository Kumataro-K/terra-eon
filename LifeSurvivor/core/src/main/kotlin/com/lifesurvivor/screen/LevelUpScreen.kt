package com.lifesurvivor.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.lifesurvivor.LifeSurvivorGame
import com.lifesurvivor.system.LevelUpChoice

/**
 * レベルアップ選択画面
 * GameScreen内のオーバーレイとして実装されているため、
 * このクラスは独立した画面として使用する場合の代替実装
 *
 * 注: 実際のレベルアップ選択はGameScreen内で処理される
 *     このクラスは将来的な拡張用に残している
 */
class LevelUpScreen(
    private val game: LifeSurvivorGame,
    private val choices: List<LevelUpChoice>,
    private val onSelect: (LevelUpChoice) -> Unit
) : ScreenAdapter() {

    private val shapeRenderer = ShapeRenderer()
    private val batch = SpriteBatch()
    private val font = BitmapFont()

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        val screenWidth = Gdx.graphics.width.toFloat()
        val screenHeight = Gdx.graphics.height.toFloat()
        val centerX = screenWidth / 2f
        val centerY = screenHeight / 2f

        // 選択肢の描画
        Gdx.gl.glEnable(GL20.GL_BLEND)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)

        val choiceWidth = 350f
        val choiceHeight = 70f

        for (i in choices.indices) {
            val y = centerY + (1 - i) * choiceHeight

            shapeRenderer.color = Color(0.2f, 0.2f, 0.35f, 0.9f)
            shapeRenderer.rect(centerX - choiceWidth / 2f, y, choiceWidth, choiceHeight - 5f)
        }

        shapeRenderer.end()

        // テキスト描画
        batch.begin()

        font.color = Color(1f, 0.9f, 0.3f, 1f)
        font.draw(batch, "LEVEL UP!", centerX - 40f, centerY + 150f)

        for (i in choices.indices) {
            val choice = choices[i]
            val y = centerY + (1 - i) * choiceHeight

            font.color = Color.WHITE
            font.draw(batch, choice.weaponName, centerX - 150f, y + choiceHeight - 15f)

            font.color = Color(0.7f, 0.7f, 0.7f, 1f)
            font.draw(batch, choice.description, centerX - 150f, y + choiceHeight - 35f)
        }

        batch.end()

        // タッチ入力処理
        if (Gdx.input.justTouched()) {
            val touchY = screenHeight - Gdx.input.y.toFloat()
            for (i in choices.indices) {
                val y = centerY + (1 - i) * choiceHeight
                if (touchY >= y && touchY < y + choiceHeight) {
                    onSelect(choices[i])
                    return
                }
            }
        }
    }

    override fun dispose() {
        shapeRenderer.dispose()
        batch.dispose()
        font.dispose()
    }
}
