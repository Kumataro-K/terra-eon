package com.lifesurvivor.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.lifesurvivor.LifeSurvivorGame
import com.lifesurvivor.data.EraData
import com.lifesurvivor.data.TriviaDatabase
import com.lifesurvivor.data.WeaponDefinitions
import com.lifesurvivor.entity.Enemy
import com.lifesurvivor.entity.Player
import com.lifesurvivor.entity.Projectile
import com.lifesurvivor.system.*
import com.lifesurvivor.ui.HUD
import com.lifesurvivor.ui.TriviaCard

/**
 * メインゲームプレイスクリーン
 * 全てのゲームロジックとレンダリングを統合する
 */
class GameScreen(private val game: LifeSurvivorGame) : ScreenAdapter() {

    // レンダリング
    private val camera = OrthographicCamera()
    private val shapeRenderer = ShapeRenderer()
    private val batch = SpriteBatch()
    private val font = BitmapFont()

    // エンティティ
    private val player = Player(0f, 0f)
    private val enemies = mutableListOf<Enemy>()
    private val projectiles = mutableListOf<Projectile>()

    // システム
    private val movementSystem = MovementSystem()
    private val attackSystem = AttackSystem()
    private val collisionSystem = CollisionSystem()
    private val enemySpawnSystem = EnemySpawnSystem()
    private val xpSystem = XpSystem()

    // UI
    private val hud = HUD()
    private val triviaCard = TriviaCard()

    // ゲーム状態
    private var elapsedTime = 0f
    private var paused = false
    private var previousEraId = ""

    // レベルアップ状態
    private var showingLevelUp = false
    private var levelUpChoices = listOf<LevelUpChoice>()
    private var showingTrivia = false

    // 画面サイズ
    private var screenWidth = 800f
    private var screenHeight = 480f

    init {
        // 初期武器として光合成ウィップを付与
        player.addOrUpgradeWeapon("photosynthesis_whip")
    }

    override fun show() {
        screenWidth = Gdx.graphics.width.toFloat()
        screenHeight = Gdx.graphics.height.toFloat()
        camera.setToOrtho(false, screenWidth, screenHeight)
        font.color = Color.WHITE
    }

    override fun render(delta: Float) {
        // デルタタイムを制限（フレーム落ち対策）
        val dt = delta.coerceAtMost(0.05f)

        // 入力処理
        handleInput()

        // ゲーム更新
        if (!paused && !showingLevelUp && !showingTrivia) {
            update(dt)
        }

        // 豆知識カードの更新（表示中でもタイマーは進める）
        triviaCard.update(dt)

        // 描画
        draw()
    }

    /** 入力処理 */
    private fun handleInput() {
        // ESCでポーズ
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            paused = !paused
        }

        // 豆知識カード表示中のタッチ処理
        if (triviaCard.isVisible() && Gdx.input.justTouched()) {
            val touchX = Gdx.input.x.toFloat()
            val touchY = screenHeight - Gdx.input.y.toFloat() // Y軸反転
            if (triviaCard.isButtonTouched(touchX, touchY)) {
                triviaCard.hide()
                showingTrivia = false
                // レベルアップ選択がまだなら表示
                if (showingLevelUp) {
                    // レベルアップ画面はそのまま
                } else {
                    // ゲーム再開
                }
            }
        }

        // レベルアップ選択中のタッチ処理
        if (showingLevelUp && !triviaCard.isVisible() && Gdx.input.justTouched()) {
            val touchY = screenHeight - Gdx.input.y.toFloat()
            handleLevelUpSelection(touchY)
        }
    }

    /** レベルアップの選択処理 */
    private fun handleLevelUpSelection(touchY: Float) {
        if (levelUpChoices.isEmpty()) return

        val centerY = screenHeight / 2f
        val choiceHeight = 60f
        val totalHeight = levelUpChoices.size * choiceHeight
        val startY = centerY + totalHeight / 2f

        for (i in levelUpChoices.indices) {
            val choiceY = startY - (i + 1) * choiceHeight
            if (touchY >= choiceY && touchY < choiceY + choiceHeight) {
                applyLevelUpChoice(levelUpChoices[i])
                return
            }
        }
    }

    /** レベルアップの選択を適用 */
    private fun applyLevelUpChoice(choice: LevelUpChoice) {
        when (choice.weaponId) {
            "_heal" -> {
                player.stats.hp = (player.stats.hp + player.stats.maxHp * 0.3f).coerceAtMost(player.stats.maxHp)
            }
            "_speed" -> {
                player.stats.moveSpeed *= 1.1f
            }
            else -> {
                val isNew = player.addOrUpgradeWeapon(choice.weaponId)
                attackSystem.applyPassiveEffects(player)

                // 新規武器獲得時は必ず豆知識表示
                if (isNew) {
                    val weaponData = WeaponDefinitions.getById(choice.weaponId)
                    if (weaponData != null) {
                        val trivia = TriviaDatabase.getById(weaponData.id)
                            ?: TriviaDatabase.getRandom()
                        triviaCard.show(trivia)
                        showingTrivia = true
                        showingLevelUp = false
                        return
                    }
                } else {
                    // 既存武器強化時は20%の確率で豆知識表示
                    if (MathUtils.random() < 0.2f) {
                        triviaCard.show(TriviaDatabase.getRandom())
                        showingTrivia = true
                        showingLevelUp = false
                        return
                    }
                }
            }
        }

        showingLevelUp = false
        levelUpChoices = emptyList()
    }

    /** ゲームロジックの更新 */
    private fun update(delta: Float) {
        elapsedTime += delta

        // 時代の変化チェック
        val currentEra = EraData.getEraByTime(elapsedTime)
        if (currentEra.id != previousEraId) {
            previousEraId = currentEra.id
            // 時代が変わった時に豆知識を表示
            if (elapsedTime > 1f) { // ゲーム開始直後は除外
                val trivia = TriviaDatabase.getRandomByCategory(currentEra.triviaCategory)
                triviaCard.show(trivia)
                showingTrivia = true
            }
        }

        // プレイヤー移動
        movementSystem.update(player)
        player.update(delta)

        // 武器の自動攻撃
        val newProjectiles = attackSystem.update(delta, player)
        projectiles.addAll(newProjectiles)

        // 弾の更新
        val projectileIterator = projectiles.iterator()
        while (projectileIterator.hasNext()) {
            val proj = projectileIterator.next()
            proj.update(delta)
            if (!proj.active) {
                projectileIterator.remove()
            }
        }

        // 敵のスポーン
        val newEnemies = enemySpawnSystem.update(
            delta, elapsedTime, enemies.size, player.position, screenWidth, screenHeight
        )
        enemies.addAll(newEnemies)

        // 敵の更新
        enemies.forEach { it.update(delta, player.position) }

        // 画面外の遠い敵を除去（パフォーマンス）
        val cullDistSq = (screenWidth + 200f) * (screenWidth + 200f)
        enemies.removeAll { it.distanceSq(player.position) > cullDistSq }

        // 弾と敵の衝突判定
        val killedEnemies = collisionSystem.checkProjectileEnemyCollisions(projectiles, enemies)
        for (killed in killedEnemies) {
            collisionSystem.spawnXpOrb(killed.position, killed.data.xpValue)
            player.stats.kills++
        }
        enemies.removeAll { !it.isAlive() }

        // 敵とプレイヤーの衝突判定
        val playerDied = collisionSystem.checkEnemyPlayerCollisions(enemies, player, delta)
        if (playerDied) {
            game.setScreen(GameOverScreen(game, player.stats, elapsedTime))
            return
        }

        // XPオーブの更新
        val leveledUp = collisionSystem.updateXpOrbs(player, delta)
        if (leveledUp) {
            // レベルアップ選択画面表示
            showingLevelUp = true
            levelUpChoices = xpSystem.generateLevelUpChoices(player)
        }
    }

    /** 描画 */
    private fun draw() {
        // 背景色（時代に応じて変更）
        val era = EraData.getEraByTime(elapsedTime)
        val bg = era.backgroundColor
        Gdx.gl.glClearColor(bg.r, bg.g, bg.b, bg.a)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // カメラをプレイヤーに追従
        camera.position.set(player.position.x, player.position.y, 0f)
        camera.update()

        // === ShapeRenderer描画（ゲームワールド） ===
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)

        shapeRenderer.projectionMatrix = camera.combined
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)

        // XPオーブの描画
        for (orb in collisionSystem.xpOrbs) {
            shapeRenderer.color = Color(0.3f, 0.6f, 1f, 0.9f)
            shapeRenderer.circle(orb.position.x, orb.position.y, 3f)
        }

        // 弾の描画
        for (proj in projectiles) {
            proj.render(shapeRenderer)
        }

        // 敵の描画
        for (enemy in enemies) {
            enemy.render(shapeRenderer)
        }

        // プレイヤーの描画
        player.render(shapeRenderer)

        shapeRenderer.end()

        // === HUD描画（画面座標系） ===
        shapeRenderer.projectionMatrix = batch.projectionMatrix
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        hud.renderBars(shapeRenderer, player, screenWidth, screenHeight)

        // レベルアップ選択画面の背景
        if (showingLevelUp && !triviaCard.isVisible()) {
            drawLevelUpBackground()
        }

        // 豆知識カードの背景
        if (triviaCard.isVisible()) {
            triviaCard.renderBackground(shapeRenderer, screenWidth, screenHeight)
        }

        // ポーズ中の暗転
        if (paused) {
            shapeRenderer.color = Color(0f, 0f, 0f, 0.5f)
            shapeRenderer.rect(0f, 0f, screenWidth, screenHeight)
        }

        shapeRenderer.end()

        // === テキスト描画 ===
        batch.begin()
        hud.renderText(batch, font, player, elapsedTime, screenWidth, screenHeight)

        // レベルアップ選択のテキスト
        if (showingLevelUp && !triviaCard.isVisible()) {
            drawLevelUpText()
        }

        // 豆知識カードのテキスト
        if (triviaCard.isVisible()) {
            triviaCard.renderText(batch, font, screenWidth, screenHeight)
        }

        // ポーズテキスト
        if (paused) {
            font.color = Color.WHITE
            font.draw(batch, "PAUSED", screenWidth / 2f - 30f, screenHeight / 2f)
            font.draw(batch, "ESCで再開", screenWidth / 2f - 40f, screenHeight / 2f - 25f)
        }

        batch.end()
    }

    /** レベルアップ選択画面の背景描画 */
    private fun drawLevelUpBackground() {
        // 半透明の暗い背景
        shapeRenderer.color = Color(0f, 0f, 0f, 0.7f)
        shapeRenderer.rect(0f, 0f, screenWidth, screenHeight)

        val centerX = screenWidth / 2f
        val centerY = screenHeight / 2f
        val choiceWidth = 350f
        val choiceHeight = 55f

        for (i in levelUpChoices.indices) {
            val y = centerY + (1 - i) * choiceHeight - choiceHeight / 2f

            // 選択肢の背景
            shapeRenderer.color = if (levelUpChoices[i].isNew) {
                Color(0.2f, 0.15f, 0.4f, 0.9f) // 新武器は紫系
            } else {
                Color(0.15f, 0.25f, 0.15f, 0.9f) // 強化は緑系
            }
            shapeRenderer.rect(centerX - choiceWidth / 2f, y, choiceWidth, choiceHeight - 5f)

            // 枠線
            shapeRenderer.color = Color(0.8f, 0.7f, 0.4f, 0.8f)
            shapeRenderer.rect(centerX - choiceWidth / 2f, y, choiceWidth, 2f)
            shapeRenderer.rect(centerX - choiceWidth / 2f, y + choiceHeight - 7f, choiceWidth, 2f)
        }
    }

    /** レベルアップ選択のテキスト描画 */
    private fun drawLevelUpText() {
        val centerX = screenWidth / 2f
        val centerY = screenHeight / 2f

        // タイトル
        font.color = Color(1f, 0.9f, 0.3f, 1f)
        font.draw(batch, "LEVEL UP! 武器を選択", centerX - 80f, centerY + 110f)

        val choiceHeight = 55f

        for (i in levelUpChoices.indices) {
            val choice = levelUpChoices[i]
            val y = centerY + (1 - i) * choiceHeight - choiceHeight / 2f

            // 武器名
            font.color = Color.WHITE
            val prefix = if (choice.isNew) "[NEW] " else ""
            font.draw(batch, "$prefix${choice.weaponName}", centerX - 150f, y + choiceHeight - 15f)

            // 説明
            font.color = Color(0.8f, 0.8f, 0.7f, 1f)
            font.draw(batch, choice.description, centerX - 150f, y + choiceHeight - 35f)
        }
    }

    override fun resize(width: Int, height: Int) {
        screenWidth = width.toFloat()
        screenHeight = height.toFloat()
        camera.setToOrtho(false, screenWidth, screenHeight)
    }

    override fun dispose() {
        shapeRenderer.dispose()
        batch.dispose()
        font.dispose()
    }
}
