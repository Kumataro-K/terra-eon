package com.lifesurvivor.data

import com.badlogic.gdx.graphics.Color

/**
 * 地質時代データ
 * ゲーム内の各エリアに対応する時代情報を定義
 */
data class Era(
    val id: String,            // 一意識別子
    val name: String,          // 時代名
    val description: String,   // 説明文
    val backgroundColor: Color,// 背景色
    val startTimeSec: Int,     // この時代が始まるゲーム内経過時間（秒）
    val durationSec: Int,      // この時代の継続時間（秒）
    val triviaCategory: TriviaCategory // 対応する豆知識カテゴリ
)

/**
 * 地質時代データベース
 * ゲーム中に進行する7つの時代エリアを定義
 */
object EraData {
    val eras = listOf(
        // エリア1: 冥王代・太古代（0:00〜5:00）
        Era(
            id = "hadean",
            name = "冥王代・太古代",
            description = "灼熱の地球、最初の生命が芽生える",
            backgroundColor = Color(0.3f, 0.05f, 0.05f, 1f), // 赤黒（溶岩）
            startTimeSec = 0,
            durationSec = 300,
            triviaCategory = TriviaCategory.ORIGIN
        ),
        // エリア2: 原生代（5:00〜10:00）
        Era(
            id = "proterozoic",
            name = "原生代",
            description = "酸素革命と真核生物の誕生",
            backgroundColor = Color(0.02f, 0.05f, 0.25f, 1f), // 紺青（深海）
            startTimeSec = 300,
            durationSec = 300,
            triviaCategory = TriviaCategory.PROTEROZOIC
        ),
        // エリア3: 古生代前期（10:00〜15:00）
        Era(
            id = "early_paleozoic",
            name = "古生代前期",
            description = "カンブリア爆発、海中の覇者たち",
            backgroundColor = Color(0.0f, 0.3f, 0.3f, 1f), // 青緑（浅海）
            startTimeSec = 600,
            durationSec = 300,
            triviaCategory = TriviaCategory.PALEOZOIC
        ),
        // エリア4: 古生代後期（15:00〜20:00）
        Era(
            id = "late_paleozoic",
            name = "古生代後期",
            description = "上陸と巨大昆虫の時代",
            backgroundColor = Color(0.25f, 0.3f, 0.1f, 1f), // 茶緑（陸地）
            startTimeSec = 900,
            durationSec = 300,
            triviaCategory = TriviaCategory.PALEOZOIC
        ),
        // エリア5: 中生代（20:00〜25:00）
        Era(
            id = "mesozoic",
            name = "中生代",
            description = "恐竜の支配するジャングル",
            backgroundColor = Color(0.3f, 0.35f, 0.0f, 1f), // 黄緑（ジャングル）
            startTimeSec = 1200,
            durationSec = 300,
            triviaCategory = TriviaCategory.MESOZOIC
        ),
        // エリア6: 新生代（25:00〜30:00）
        Era(
            id = "cenozoic",
            name = "新生代",
            description = "哺乳類の逆襲と繁栄",
            backgroundColor = Color(0.1f, 0.35f, 0.1f, 1f), // 緑（草原）
            startTimeSec = 1500,
            durationSec = 300,
            triviaCategory = TriviaCategory.CENOZOIC
        ),
        // エリア7: 現代・ボス（30:00〜）
        Era(
            id = "modern",
            name = "現代",
            description = "最強の捕食者、ホモ・サピエンス",
            backgroundColor = Color(0.3f, 0.3f, 0.3f, 1f), // 灰色（都市）
            startTimeSec = 1800,
            durationSec = 300,
            triviaCategory = TriviaCategory.MODERN
        )
    )

    /** 経過時間から現在の時代を取得 */
    fun getEraByTime(elapsedSeconds: Float): Era {
        val sec = elapsedSeconds.toInt()
        // 逆順で最初にマッチする時代を返す
        return eras.lastOrNull { sec >= it.startTimeSec } ?: eras.first()
    }

    /** IDで時代を取得 */
    fun getEraById(id: String): Era? {
        return eras.find { it.id == id }
    }
}
