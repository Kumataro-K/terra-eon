package com.lifesurvivor.data

import com.badlogic.gdx.graphics.Color

/**
 * 敵データ定義
 * 各敵の基本パラメータを保持
 */
data class EnemyData(
    val id: String,
    val name: String,          // 例: "三葉虫", "アノマロカリス"
    val hp: Float,
    val speed: Float,
    val damage: Float,
    val xpValue: Float,
    val size: Float,           // ShapeRenderer円の半径
    val color: Color,
    val era: String,           // 出現時代ID
    val isBoss: Boolean = false // ボスフラグ
)

/**
 * 敵定義データベース
 * 各時代に出現する敵の全データを管理
 */
object EnemyDefinitions {
    val enemies = listOf(
        // === 冥王代・太古代の敵 ===
        EnemyData(
            id = "anaerobic_bacterium",
            name = "嫌気性菌",
            hp = 5f,
            speed = 30f,
            damage = 3f,
            xpValue = 1f,
            size = 4f,
            color = Color(0.6f, 0.2f, 0.6f, 1f), // 紫
            era = "hadean"
        ),
        EnemyData(
            id = "primordial_virus",
            name = "原始ウイルス",
            hp = 3f,
            speed = 50f,
            damage = 2f,
            xpValue = 1f,
            size = 3f,
            color = Color(0.8f, 0.3f, 0.3f, 1f), // 赤
            era = "hadean"
        ),

        // === 原生代の敵 ===
        EnemyData(
            id = "cyanobacteria_swarm",
            name = "シアノバクテリア群",
            hp = 8f,
            speed = 25f,
            damage = 4f,
            xpValue = 2f,
            size = 6f,
            color = Color(0.0f, 0.6f, 0.4f, 1f), // 青緑
            era = "proterozoic"
        ),
        EnemyData(
            id = "iron_oxidizer",
            name = "鉄酸化細菌",
            hp = 12f,
            speed = 20f,
            damage = 5f,
            xpValue = 3f,
            size = 7f,
            color = Color(0.7f, 0.4f, 0.1f, 1f), // 茶色
            era = "proterozoic"
        ),

        // === 古生代前期の敵 ===
        EnemyData(
            id = "trilobite",
            name = "三葉虫",
            hp = 20f,
            speed = 35f,
            damage = 6f,
            xpValue = 4f,
            size = 8f,
            color = Color(0.5f, 0.4f, 0.3f, 1f), // 茶灰色
            era = "early_paleozoic"
        ),
        EnemyData(
            id = "sea_scorpion",
            name = "ウミサソリ",
            hp = 25f,
            speed = 40f,
            damage = 8f,
            xpValue = 5f,
            size = 10f,
            color = Color(0.4f, 0.3f, 0.2f, 1f), // 暗い茶色
            era = "early_paleozoic"
        ),
        EnemyData(
            id = "anomalocaris",
            name = "アノマロカリス",
            hp = 150f,
            speed = 45f,
            damage = 15f,
            xpValue = 50f,
            size = 20f,
            color = Color(0.8f, 0.4f, 0.2f, 1f), // オレンジ
            era = "early_paleozoic",
            isBoss = true
        ),

        // === 古生代後期の敵 ===
        EnemyData(
            id = "dunkleosteus",
            name = "ダンクルオステウス",
            hp = 100f,
            speed = 50f,
            damage = 20f,
            xpValue = 30f,
            size = 18f,
            color = Color(0.3f, 0.3f, 0.4f, 1f), // 鉄灰色
            era = "late_paleozoic",
            isBoss = true
        ),
        EnemyData(
            id = "giant_dragonfly",
            name = "巨大トンボ",
            hp = 15f,
            speed = 70f,
            damage = 7f,
            xpValue = 5f,
            size = 9f,
            color = Color(0.2f, 0.7f, 0.3f, 1f), // 緑
            era = "late_paleozoic"
        ),
        EnemyData(
            id = "giant_millipede",
            name = "巨大ヤスデ",
            hp = 35f,
            speed = 25f,
            damage = 10f,
            xpValue = 6f,
            size = 12f,
            color = Color(0.4f, 0.2f, 0.1f, 1f), // 暗褐色
            era = "late_paleozoic"
        ),

        // === 中生代の敵 ===
        EnemyData(
            id = "velociraptor",
            name = "ヴェロキラプトル",
            hp = 30f,
            speed = 65f,
            damage = 12f,
            xpValue = 8f,
            size = 10f,
            color = Color(0.6f, 0.5f, 0.2f, 1f), // 黄褐色
            era = "mesozoic"
        ),
        EnemyData(
            id = "pteranodon",
            name = "プテラノドン",
            hp = 20f,
            speed = 80f,
            damage = 8f,
            xpValue = 7f,
            size = 11f,
            color = Color(0.5f, 0.5f, 0.6f, 1f), // 灰青色
            era = "mesozoic"
        ),
        EnemyData(
            id = "trex",
            name = "ティラノサウルス",
            hp = 300f,
            speed = 40f,
            damage = 30f,
            xpValue = 80f,
            size = 25f,
            color = Color(0.4f, 0.3f, 0.1f, 1f), // 暗い黄褐色
            era = "mesozoic",
            isBoss = true
        ),

        // === 新生代の敵 ===
        EnemyData(
            id = "mammoth",
            name = "マンモス",
            hp = 80f,
            speed = 35f,
            damage = 18f,
            xpValue = 12f,
            size = 16f,
            color = Color(0.5f, 0.35f, 0.2f, 1f), // 茶色
            era = "cenozoic"
        ),
        EnemyData(
            id = "saber_tooth",
            name = "剣歯虎",
            hp = 50f,
            speed = 60f,
            damage = 15f,
            xpValue = 10f,
            size = 12f,
            color = Color(0.7f, 0.6f, 0.3f, 1f), // 金色
            era = "cenozoic"
        ),
        EnemyData(
            id = "terror_bird",
            name = "恐鳥",
            hp = 40f,
            speed = 70f,
            damage = 14f,
            xpValue = 9f,
            size = 13f,
            color = Color(0.6f, 0.2f, 0.2f, 1f), // 赤褐色
            era = "cenozoic"
        ),

        // === 現代の敵（最終ボス） ===
        EnemyData(
            id = "homo_sapiens",
            name = "ホモ・サピエンス",
            hp = 60f,
            speed = 55f,
            damage = 20f,
            xpValue = 15f,
            size = 10f,
            color = Color(0.9f, 0.8f, 0.6f, 1f), // 肌色
            era = "modern"
        ),
        EnemyData(
            id = "homo_sapiens_boss",
            name = "人類文明",
            hp = 500f,
            speed = 30f,
            damage = 40f,
            xpValue = 200f,
            size = 30f,
            color = Color(0.9f, 0.9f, 0.9f, 1f), // 白
            era = "modern",
            isBoss = true
        )
    )

    /** 指定した時代の通常敵を取得 */
    fun getEnemiesByEra(eraId: String): List<EnemyData> {
        return enemies.filter { it.era == eraId && !it.isBoss }
    }

    /** 指定した時代のボスを取得 */
    fun getBossByEra(eraId: String): EnemyData? {
        return enemies.find { it.era == eraId && it.isBoss }
    }

    /** IDで敵データを取得 */
    fun getById(id: String): EnemyData? {
        return enemies.find { it.id == id }
    }
}
