package com.lifesurvivor.data

/**
 * 武器タイプ
 * アクティブ武器とパッシブ効果を区別する
 */
enum class WeaponType {
    ACTIVE,   // 自動攻撃武器
    PASSIVE   // パッシブ効果
}

/**
 * 武器データ定義
 * 各武器の基本パラメータと関連豆知識を保持
 */
data class WeaponData(
    val id: String,
    val name: String,
    val description: String,
    val triviaTitle: String,       // 豆知識タイトル
    val triviaBody: String,        // 豆知識本文（100〜150字）
    val triviaEra: String,         // 関連する地質時代
    val type: WeaponType = WeaponType.ACTIVE,
    val maxLevel: Int = 8,
    val baseDamage: Float,
    val baseCooldown: Float,       // 秒
    val baseArea: Float,
    val projectileSpeed: Float = 200f
)

/**
 * 全武器データベース
 * ゲームに登場する全8種の武器を定義
 */
object WeaponDefinitions {
    val weapons = listOf(
        // 1. 光合成ウィップ — 近距離円弧攻撃
        WeaponData(
            id = "photosynthesis_whip",
            name = "光合成ウィップ",
            description = "太陽光エネルギーのムチが周囲を薙ぎ払う",
            triviaTitle = "光合成の誕生",
            triviaBody = "シアノバクテリアは約35億年前に光合成を獲得。太陽光を使ってATPを生成し、副産物として酸素を放出した。これが後の大酸化イベントの引き金となった。",
            triviaEra = "約35億年前",
            baseDamage = 12f,
            baseCooldown = 1.2f,
            baseArea = 60f // 円弧の半径
        ),

        // 2. 酸素バースト — 全方向8方向弾
        WeaponData(
            id = "oxygen_burst",
            name = "酸素バースト",
            description = "猛毒の酸素を8方向に放射する",
            triviaTitle = "大酸化イベント",
            triviaBody = "大酸化イベント（GOE）は約24億年前、酸素が大気の21%に達した大変革。当時の嫌気性生物にとって酸素は猛毒であり、これが地球史上初の大量絶滅を引き起こした。",
            triviaEra = "約24億年前",
            baseDamage = 8f,
            baseCooldown = 2.0f,
            baseArea = 10f, // 弾のサイズ
            projectileSpeed = 250f
        ),

        // 3. 絶滅隕石 — 画面上部からランダム落下＆爆発
        WeaponData(
            id = "extinction_meteor",
            name = "絶滅隕石",
            description = "小惑星が空から降り注ぎ爆発する",
            triviaTitle = "恐竜を滅ぼした隕石",
            triviaBody = "6600万年前、直径約10kmの小惑星がユカタン半島に衝突。衝突の冬で光合成が停止し食物連鎖が崩壊、非鳥類恐竜を含む全生物の76%が絶滅した。",
            triviaEra = "6600万年前",
            baseDamage = 30f,
            baseCooldown = 3.5f,
            baseArea = 50f, // 爆発半径
            projectileSpeed = 300f
        ),

        // 4. 三葉虫アーマー — パッシブ防御
        WeaponData(
            id = "trilobite_armor",
            name = "三葉虫アーマー",
            description = "古代の甲殻が身を守り、接触した敵にダメージを与える",
            triviaTitle = "三葉虫の繁栄",
            triviaBody = "三葉虫は5億2000万年前から2億5000万年前まで約2.7億年間生き続けた古生代の覇者。複眼を持つ最初期の生物の一つで、1万種以上が確認されている。",
            triviaEra = "5.2億〜2.5億年前",
            type = WeaponType.PASSIVE,
            baseDamage = 5f,   // 接触ダメージ反射
            baseCooldown = 0f, // パッシブなのでクールダウンなし
            baseArea = 30f     // 反射範囲
        ),

        // 5. 大森林の胞子 — 毒霧エリア
        WeaponData(
            id = "carboniferous_spore",
            name = "大森林の胞子",
            description = "石炭紀の巨大植物が毒の胞子を撒き散らす",
            triviaTitle = "石炭紀の高酸素時代",
            triviaBody = "石炭紀（3.6〜3億年前）の大気中酸素濃度は35%超。現代の21%と比べて非常に高く、これが翼開長70cmを超える巨大トンボや体長2mのヤスデを可能にした。",
            triviaEra = "約3.6〜3億年前",
            baseDamage = 4f,  // 毎秒ダメージ
            baseCooldown = 4.0f,
            baseArea = 80f    // 毒霧の半径
        ),

        // 6. DNA二重螺旋 — パッシブ（XP吸収＋HP回復）
        WeaponData(
            id = "dna_helix",
            name = "DNA二重螺旋",
            description = "真核生物の力でXP吸収範囲が拡大しHPが回復する",
            triviaTitle = "細胞内共生",
            triviaBody = "真核生物は約21億年前、細菌を細胞内に取り込む『細胞内共生』で生まれた。ミトコンドリアは元々独立した好気性細菌で、その証拠に今も独自のDNAを持つ。",
            triviaEra = "約21億年前",
            type = WeaponType.PASSIVE,
            baseDamage = 0f,
            baseCooldown = 0f,
            baseArea = 50f // XP吸収範囲のボーナス
        ),

        // 7. 恐竜の咆哮 — 前方円錐衝撃波
        WeaponData(
            id = "dinosaur_roar",
            name = "恐竜の咆哮",
            description = "T-レックスの咬合力57,000Nの衝撃波が敵を吹き飛ばす",
            triviaTitle = "最強の顎",
            triviaBody = "最大の陸上肉食恐竜ティラノサウルスの咬合力は約57,000N。現代の動物で最強とされるナイルワニ（22,000N）の2倍以上。しかし小惑星1つで全て終わった。",
            triviaEra = "約6800万年前",
            baseDamage = 18f,
            baseCooldown = 2.5f,
            baseArea = 70f, // 円錐の長さ
            projectileSpeed = 0f // 即時発動
        ),

        // 8. P-T境界の火山 — 設置型溶岩エリア
        WeaponData(
            id = "pt_boundary_volcano",
            name = "P-T境界の火山",
            description = "史上最大の超火山噴火が大地を焼き尽くす",
            triviaTitle = "史上最悪の大絶滅",
            triviaBody = "ペルム紀末大絶滅（2.52億年前）はシベリアの超火山噴火が原因とされる。約100万年にわたる噴火で海洋生物の96%・陸上生物の70%が絶滅した史上最大の絶滅事変。",
            triviaEra = "2.52億年前",
            baseDamage = 6f,  // 毎秒ダメージ
            baseCooldown = 5.0f,
            baseArea = 60f    // 溶岩エリアの半径
        )
    )

    /** IDで武器データを取得 */
    fun getById(id: String): WeaponData? {
        return weapons.find { it.id == id }
    }

    /** アクティブ武器のみ取得 */
    fun getActiveWeapons(): List<WeaponData> {
        return weapons.filter { it.type == WeaponType.ACTIVE }
    }

    /** パッシブ武器のみ取得 */
    fun getPassiveWeapons(): List<WeaponData> {
        return weapons.filter { it.type == WeaponType.PASSIVE }
    }

    /** ランダムに武器を選択（レベルアップ選択用） */
    fun getRandomWeapons(count: Int, excludeIds: List<String> = emptyList()): List<WeaponData> {
        return weapons.filter { it.id !in excludeIds }.shuffled().take(count)
    }
}
