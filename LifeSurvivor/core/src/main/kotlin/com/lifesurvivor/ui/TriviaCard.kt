package com.lifesurvivor.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.lifesurvivor.data.TriviaEntry

/**
 * 豆知識カードUI
 * 武器獲得またはレベルアップ選択時に表示されるモーダルカード
 *
 * ┌─────────────────────────────────┐
 * │  生命史豆知識                    │
 * │  ─────────────────────────      │
 * │  【三葉虫の複眼】5〜2.5億年前    │
 * │                                  │
 * │  三葉虫の複眼は方解石でできた    │
 * │  世界最古の目。現代の昆虫の複眼  │
 * │  と同じ原理で...（本文）         │
 * │                                  │
 * │  ─────────────────────────      │
 * │  この武器を獲得して先へ進む ▶    │
 * └─────────────────────────────────┘
 */
class TriviaCard {
    private val cardWidth = 450f
    private val cardHeight = 320f
    private val cardColor = Color(0.96f, 0.90f, 0.78f, 0.95f) // 羊皮紙風 #F5E6C8
    private val borderColor = Color(0.6f, 0.5f, 0.3f, 1f)
    private val textColor = Color(0.2f, 0.15f, 0.1f, 1f)
    private val titleColor = Color(0.4f, 0.2f, 0.05f, 1f)
    private val eraColor = Color(0.5f, 0.4f, 0.3f, 1f)
    private val buttonColor = Color(0.4f, 0.3f, 0.15f, 1f)
    private val buttonDisabledColor = Color(0.6f, 0.55f, 0.5f, 1f)

    // 表示する豆知識
    var currentTrivia: TriviaEntry? = null
    var showTimer = 0f
    private val minDisplayTime = 3f // 最低3秒表示

    // ボタンの位置（タッチ判定用）
    var buttonX = 0f
    var buttonY = 0f
    var buttonWidth = 0f
    var buttonHeight = 30f

    private val glyphLayout = GlyphLayout()

    /** 豆知識カードを表示開始 */
    fun show(trivia: TriviaEntry) {
        currentTrivia = trivia
        showTimer = 0f
    }

    /** 非表示にする */
    fun hide() {
        currentTrivia = null
        showTimer = 0f
    }

    /** 表示中か */
    fun isVisible(): Boolean = currentTrivia != null

    /** ボタンが押せる状態か（最低表示時間経過後） */
    fun isButtonEnabled(): Boolean = showTimer >= minDisplayTime

    /** 更新 */
    fun update(delta: Float) {
        if (isVisible()) {
            showTimer += delta
        }
    }

    /** 指定座標がボタンの範囲内か判定 */
    fun isButtonTouched(touchX: Float, touchY: Float): Boolean {
        if (!isButtonEnabled()) return false
        return touchX >= buttonX && touchX <= buttonX + buttonWidth &&
               touchY >= buttonY && touchY <= buttonY + buttonHeight
    }

    /** ShapeRendererでカード背景を描画 */
    fun renderBackground(renderer: ShapeRenderer, screenWidth: Float, screenHeight: Float) {
        if (!isVisible()) return

        // 画面全体を暗くする
        renderer.color = Color(0f, 0f, 0f, 0.6f)
        renderer.rect(0f, 0f, screenWidth, screenHeight)

        val cardX = (screenWidth - cardWidth) / 2f
        val cardY = (screenHeight - cardHeight) / 2f

        // カード背景
        renderer.color = cardColor
        renderer.rect(cardX, cardY, cardWidth, cardHeight)

        // カード枠線
        renderer.color = borderColor
        // 上辺
        renderer.rect(cardX, cardY + cardHeight - 2f, cardWidth, 2f)
        // 下辺
        renderer.rect(cardX, cardY, cardWidth, 2f)
        // 左辺
        renderer.rect(cardX, cardY, 2f, cardHeight)
        // 右辺
        renderer.rect(cardX + cardWidth - 2f, cardY, 2f, cardHeight)

        // 区切り線（タイトル下）
        renderer.color = Color(0.7f, 0.6f, 0.4f, 0.8f)
        renderer.rect(cardX + 20f, cardY + cardHeight - 60f, cardWidth - 40f, 1f)

        // 区切り線（ボタン上）
        renderer.rect(cardX + 20f, cardY + 50f, cardWidth - 40f, 1f)

        // ボタン背景
        buttonWidth = cardWidth - 60f
        buttonX = cardX + 30f
        buttonY = cardY + 12f
        val btnColor = if (isButtonEnabled()) buttonColor else buttonDisabledColor
        renderer.color = btnColor
        renderer.rect(buttonX, buttonY, buttonWidth, buttonHeight)
    }

    /** SpriteBatchでテキストを描画 */
    fun renderText(batch: SpriteBatch, font: BitmapFont, screenWidth: Float, screenHeight: Float) {
        if (!isVisible()) return
        val trivia = currentTrivia ?: return

        val cardX = (screenWidth - cardWidth) / 2f
        val cardY = (screenHeight - cardHeight) / 2f

        // ヘッダー: "生命史豆知識"
        font.color = titleColor
        font.draw(batch, "生命史豆知識", cardX + 20f, cardY + cardHeight - 15f)

        // タイトルと時代
        font.color = textColor
        val titleText = "【${trivia.title}】${trivia.era}"
        font.draw(batch, titleText, cardX + 20f, cardY + cardHeight - 70f)

        // 本文（手動改行）
        font.color = textColor
        val body = trivia.body
        val maxCharsPerLine = 28 // 1行あたりの最大文字数
        val lines = wrapText(body, maxCharsPerLine)
        var lineY = cardY + cardHeight - 100f

        for (line in lines) {
            font.draw(batch, line, cardX + 20f, lineY)
            lineY -= 22f
        }

        // ボタンテキスト
        val buttonText = if (isButtonEnabled()) {
            "この知識を得て先へ進む  ▶"
        } else {
            val remaining = (minDisplayTime - showTimer).coerceAtLeast(0f).toInt() + 1
            "読んでください... (${remaining}秒)"
        }
        font.color = Color.WHITE
        font.draw(batch, buttonText, buttonX + 10f, buttonY + 22f)
    }

    /** テキストを指定文字数で折り返す */
    private fun wrapText(text: String, maxChars: Int): List<String> {
        val lines = mutableListOf<String>()
        var remaining = text

        while (remaining.isNotEmpty()) {
            if (remaining.length <= maxChars) {
                lines.add(remaining)
                break
            }

            // 句読点や空白で区切れる位置を探す
            var breakPos = maxChars
            for (i in maxChars downTo maxChars - 5) {
                if (i < remaining.length && (remaining[i] == '。' || remaining[i] == '、' || remaining[i] == '）' || remaining[i] == '」')) {
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
}
