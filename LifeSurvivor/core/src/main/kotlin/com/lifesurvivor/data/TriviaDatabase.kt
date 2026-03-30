package com.lifesurvivor.data

/**
 * 豆知識カテゴリ
 * 生命史の各時代に対応するカテゴリを定義
 */
enum class TriviaCategory {
    COSMOS,       // 宇宙・誕生
    ORIGIN,       // 生命の起源
    PROTEROZOIC,  // 原生代
    PALEOZOIC,    // 古生代
    MESOZOIC,     // 中生代
    CENOZOIC,     // 新生代
    MODERN        // 現代
}

/**
 * 豆知識エントリ
 * ゲーム中に表示される教育コンテンツの1件分
 */
data class TriviaEntry(
    val id: String,          // 一意識別子
    val title: String,       // 表示タイトル
    val era: String,         // 関連する時代（表示用文字列）
    val body: String,        // 本文（100〜150字程度）
    val category: TriviaCategory  // カテゴリ
)

/**
 * 豆知識データベース
 * 生命史に関する全豆知識を管理するシングルトン
 * 武器獲得・レベルアップ・エリア移行時にランダム表示される
 */
object TriviaDatabase {
    val entries = listOf(
        TriviaEntry(
            id = "bigbang",
            title = "宇宙誕生",
            era = "138億年前",
            body = "ビッグバンから3分後、宇宙の温度が下がり水素とヘリウムの原子核が形成された。初期宇宙には炭素も酸素も存在せず、生命の材料は後の恒星が作り出した。",
            category = TriviaCategory.COSMOS
        ),
        TriviaEntry(
            id = "first_life",
            title = "最初の生命",
            era = "約38〜40億年前",
            body = "最初の生命は海底の熱水噴出孔付近で生まれたと考えられる。RNAが遺伝情報と触媒機能を同時に持つ「RNAワールド」仮説が有力で、酵素の前身だった可能性がある。",
            category = TriviaCategory.ORIGIN
        ),
        TriviaEntry(
            id = "stromatolite",
            title = "ストロマトライト",
            era = "約35億年前",
            body = "シアノバクテリアが層を重ねて作る岩状構造物。現在もオーストラリアのシャーク湾で生きたストロマトライトが見られ、地球最古の生命活動の証拠が現代まで残っている。",
            category = TriviaCategory.PROTEROZOIC
        ),
        TriviaEntry(
            id = "goe",
            title = "大酸化イベント",
            era = "約24億年前",
            body = "シアノバクテリアが放出した酸素は海中の鉄を酸化させ縞状鉄鉱層を形成した後、大気中に蓄積。嫌気性生物には猛毒だったが、酸素呼吸という高効率エネルギー代謝を生んだ。",
            category = TriviaCategory.PROTEROZOIC
        ),
        TriviaEntry(
            id = "eukaryote",
            title = "真核生物の誕生",
            era = "約21億年前",
            body = "ミトコンドリアは元々独立した好気性細菌。別の細菌に取り込まれて共生関係になったのが真核生物の起源（細胞内共生説）。葉緑体も同様にシアノバクテリアが起源とされる。",
            category = TriviaCategory.PROTEROZOIC
        ),
        TriviaEntry(
            id = "snowball_earth",
            title = "スノーボールアース",
            era = "約7.2億〜6.3億年前",
            body = "全球凍結では赤道付近も厚さ数kmの氷に覆われたとされる。生命は深海熱水噴出孔や氷の下で生き延び、凍結解除後の豊かな環境で多細胞生物が爆発的に多様化した。",
            category = TriviaCategory.PROTEROZOIC
        ),
        TriviaEntry(
            id = "ediacara",
            title = "エディアカラ生物群",
            era = "約6〜5.4億年前",
            body = "エディアカラ生物は骨格も目も消化管も確認されていない謎の生物群。捕食者が存在しない『エデンの園』状態だったが、カンブリア爆発で捕食生態系が確立すると急速に姿を消した。",
            category = TriviaCategory.PROTEROZOIC
        ),
        TriviaEntry(
            id = "cambrian",
            title = "カンブリア爆発",
            era = "約5.4億年前",
            body = "わずか数百万年（地質学的には瞬間）で動物の主要な体制（門）のほとんどが出そろった。目の進化が捕食者と被食者の軍拡競争を加速させたという『ライト・スイッチ仮説』がある。",
            category = TriviaCategory.PALEOZOIC
        ),
        TriviaEntry(
            id = "trilobite",
            title = "三葉虫の複眼",
            era = "5〜2.5億年前",
            body = "三葉虫の複眼は方解石（炭酸カルシウム）でできた世界最古の目。現代の昆虫の複眼と同じ原理で、中には個眼が1万5千個以上ある種も。2.7億年の繁栄後、P-T絶滅で消えた。",
            category = TriviaCategory.PALEOZOIC
        ),
        TriviaEntry(
            id = "anomalocaris",
            title = "アノマロカリス",
            era = "約5.2億年前",
            body = "体長最大1mに達するカンブリア紀最大の捕食者。複眼の解像度は現代のトンボに匹敵し、関節のある前肢で三葉虫を捕食した。発見当初は別々の生物の部位と思われていた。",
            category = TriviaCategory.PALEOZOIC
        ),
        TriviaEntry(
            id = "ordovician_extinction",
            title = "第1の大絶滅",
            era = "4.43億年前",
            body = "オルドビス紀末大絶滅の原因にはガンマ線バースト説がある。近傍の恒星が爆発し、放射線が大気のオゾン層を破壊して紫外線が生物を直撃したという仮説で、痕跡を探す研究が続く。",
            category = TriviaCategory.PALEOZOIC
        ),
        TriviaEntry(
            id = "devonian_fish",
            title = "魚類の上陸",
            era = "約3.75億年前",
            body = "ティクターリクは魚と四肢動物の中間種。魚のうろこと鰭を持ちながら、首を左右に回すことができた。浅い水辺で肘をついて這いずる動作が可能で、陸上進出の決定的証拠とされる。",
            category = TriviaCategory.PALEOZOIC
        ),
        TriviaEntry(
            id = "carboniferous",
            title = "石炭紀の巨大生物",
            era = "約3.6〜3億年前",
            body = "石炭紀の高酸素環境（35%超）は気管呼吸の昆虫に有利に働き、巨大化を促した。当時の昆虫に天敵がいなかったことも一因。石炭の大部分はこの時代の植物が分解されずに堆積したもの。",
            category = TriviaCategory.PALEOZOIC
        ),
        TriviaEntry(
            id = "pt_extinction",
            title = "史上最大の絶滅",
            era = "2.52億年前",
            body = "P-T境界（ペルム紀末）大絶滅は海洋生物96%・陸上生物70%が消えた地球史最大の危機。シベリアトラップの超火山噴火で二酸化炭素と硫黄ガスが大量放出され、海洋酸性化と無酸素化が重なった。",
            category = TriviaCategory.PALEOZOIC
        ),
        TriviaEntry(
            id = "dinosaur_origin",
            title = "恐竜の誕生",
            era = "約2.4億年前",
            body = "恐竜はP-T大絶滅の生き残り爬虫類から進化した。初期恐竜は体長1m前後の小型動物。T-J大絶滅（2.01億年前）でライバルの大型爬虫類が消えたことで、恐竜が空いたニッチを占領した。",
            category = TriviaCategory.MESOZOIC
        ),
        TriviaEntry(
            id = "trex",
            title = "T-レックスの真実",
            era = "約6800万〜6600万年前",
            body = "ティラノサウルスは実際には羽毛を持っていた可能性が高い。咬合力は57,000Nで史上最強クラスだが、前肢は極端に小さく用途は不明。嗅覚が非常に発達していたことが脳の化石から判明している。",
            category = TriviaCategory.MESOZOIC
        ),
        TriviaEntry(
            id = "flowering_plants",
            title = "花の誕生",
            era = "約1.4億年前",
            body = "白亜紀に被子植物（花を咲かせる植物）が爆発的に多様化。ダーウィンは被子植物の急速な繁栄を『忌まわしい謎』と呼んだ。虫との共進化（花粉を運ぶ見返りに蜜を提供）が急速拡大の鍵だった。",
            category = TriviaCategory.MESOZOIC
        ),
        TriviaEntry(
            id = "kpg_extinction",
            title = "恐竜絶滅の真実",
            era = "6600万年前",
            body = "小惑星衝突説を提唱したアルバレス父子は当初激しく否定された。決定的証拠はK-Pg境界層に世界中で見られるイリジウム異常（小惑星由来の希少元素）。チクシュルーブクレーターが1991年に確認された。",
            category = TriviaCategory.MESOZOIC
        ),
        TriviaEntry(
            id = "mammal_rise",
            title = "哺乳類の逆転",
            era = "6600万年前〜",
            body = "哺乳類は恐竜時代の1億5千万年間、ほぼ全て小型夜行性動物だった。恐竜絶滅後わずか1000万年で象サイズの哺乳類が出現。空いた生態的ニッチへの適応放散は進化史上最速レベル。",
            category = TriviaCategory.CENOZOIC
        ),
        TriviaEntry(
            id = "human_evolution",
            title = "人類の登場",
            era = "約20万年前",
            body = "ホモ・サピエンスはアフリカで約20万年前に登場。約7万年前の『認知革命』で抽象的思考・言語・協力が飛躍的に発展した。これにより食物連鎖の中位から頂点へわずか数万年で駆け上がった。",
            category = TriviaCategory.CENOZOIC
        ),
        TriviaEntry(
            id = "sixth_extinction",
            title = "第6の大絶滅",
            era = "現代",
            body = "現在の種の消滅速度は過去の大絶滅並みかそれ以上で、人類活動が原因とされる。脊椎動物だけで1970年以降の60年で平均個体数が68%減少。しかし過去の大絶滅では必ず生き残りから新たな繁栄が生まれた。",
            category = TriviaCategory.MODERN
        )
    )

    /** カテゴリで豆知識をフィルタリング */
    fun getByCategory(category: TriviaCategory): List<TriviaEntry> {
        return entries.filter { it.category == category }
    }

    /** IDで豆知識を取得 */
    fun getById(id: String): TriviaEntry? {
        return entries.find { it.id == id }
    }

    /** ランダムに1件取得 */
    fun getRandom(): TriviaEntry {
        return entries.random()
    }

    /** 指定カテゴリからランダムに1件取得 */
    fun getRandomByCategory(category: TriviaCategory): TriviaEntry {
        val filtered = getByCategory(category)
        return if (filtered.isNotEmpty()) filtered.random() else entries.random()
    }
}
