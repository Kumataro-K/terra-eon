# Life Survivor: 生命38億年の戦い

Vampire Survivorsスタイルの自動攻撃サバイバルゲーム。宇宙誕生から現代まで、38億年の生命史を駆け抜けろ！

武器を集め、敵を倒し、生命の歴史に関する豆知識を手に入れよう。

## 技術スタック

- **言語**: Kotlin
- **ゲームエンジン**: LibGDX 1.12.1
- **ビルドシステム**: Gradle Kotlin DSL
- **プラットフォーム**: Android / Desktop (JVM)
- **描画**: ShapeRenderer + BitmapFont（テクスチャ不要のプレースホルダーグラフィック）

## ゲーム概要

### コンセプト
- 自動攻撃で大量の敵を倒すサバイバーゲーム
- 地球の生命史をテーマにした7つの時代を進行
- 武器獲得・レベルアップ・時代遷移で生命史の豆知識が表示される教育要素

### 操作方法
| 入力 | 操作 |
|------|------|
| WASD / 矢印キー | 移動（Desktop） |
| タッチ＆ドラッグ | ジョイスティック移動（Android） |
| ESC | ポーズ |
| SPACE / ENTER | ゲーム開始（タイトル画面） |

武器は**自動攻撃**！移動に集中してXPを集めよう。

## 7つの地質時代エリア

| # | 時代 | 時間 | 背景色 |
|---|------|------|--------|
| 1 | 冥王代・太古代 | 0:00〜5:00 | 赤黒（溶岩） |
| 2 | 原生代 | 5:00〜10:00 | 紺青（深海） |
| 3 | 古生代前期 | 10:00〜15:00 | 青緑（浅海） |
| 4 | 古生代後期 | 15:00〜20:00 | 茶緑（陸地） |
| 5 | 中生代 | 20:00〜25:00 | 黄緑（ジャングル） |
| 6 | 新生代 | 25:00〜30:00 | 緑（草原） |
| 7 | 現代 | 30:00〜 | 灰色（都市） |

## 武器一覧（8種）

### アクティブ武器（6種）
| 武器名 | 攻撃タイプ | 説明 |
|--------|-----------|------|
| 光合成ウィップ | 円弧攻撃 | 太陽光エネルギーのムチが周囲を薙ぎ払う |
| 酸素バースト | 全方向弾 | 猛毒の酸素を8方向に放射する |
| 絶滅隕石 | 落下爆発 | 小惑星が空から降り注ぎ爆発する |
| 大森林の胞子 | 毒霧エリア | 石炭紀の巨大植物が毒の胞子を撒き散らす |
| 恐竜の咆哮 | 前方衝撃波 | T-レックスの衝撃波が敵を吹き飛ばす |
| P-T境界の火山 | 設置型溶岩 | 史上最大の超火山噴火が大地を焼き尽くす |

### パッシブ武器（2種）
| 武器名 | 効果 |
|--------|------|
| 三葉虫アーマー | 防御力UP + 接触ダメージ反射 |
| DNA二重螺旋 | XP吸収範囲拡大 + HP自然回復 |

## 敵一覧（19種）

各時代に通常敵とボスが出現。時代の80%経過でボスがスポーンする。

| 時代 | 通常敵 | ボス |
|------|--------|------|
| 冥王代 | 嫌気性菌、原始ウイルス | — |
| 原生代 | シアノバクテリア群、鉄酸化細菌 | — |
| 古生代前期 | 三葉虫、ウミサソリ | アノマロカリス |
| 古生代後期 | 巨大トンボ、巨大ヤスデ | ダンクルオステウス |
| 中生代 | ヴェロキラプトル、プテラノドン | ティラノサウルス |
| 新生代 | マンモス、剣歯虎、恐鳥 | — |
| 現代 | ホモ・サピエンス | 人類文明 |

## 豆知識システム

全21件の生命史豆知識がゲーム中に表示されます：

- **武器獲得時**: 100% の確率で関連豆知識を表示
- **武器強化時**: 20% の確率でランダム豆知識を表示
- **時代遷移時**: その時代に関連する豆知識を表示
- **タイトル画面・ゲームオーバー画面**: ランダム豆知識を表示

豆知識カードは羊皮紙風デザインで、最低3秒間は読んでから先へ進めます。

## ゲームバランス

### XPシステム
- 次のレベルに必要なXP: `10 × レベル^1.3`
- レベルアップ時に3択（新武器 / 武器強化 / 回復・速度UP）

### 敵スポーンテーブル
| 経過時間 | スポーン率(/秒) | 最大同時出現数 |
|----------|----------------|---------------|
| 0秒〜 | 1.0 | 30 |
| 120秒〜 | 2.0 | 60 |
| 300秒〜 | 3.5 | 100 |
| 600秒〜 | 6.0 | 200 |
| 900秒〜 | 10.0 | 300 |

## プロジェクト構成

```
LifeSurvivor/
├── build.gradle.kts              # ルートビルド設定
├── settings.gradle.kts           # マルチモジュール設定
├── gradle.properties             # Gradle設定
├── core/                         # 共通ゲームロジック
│   ├── build.gradle.kts
│   └── src/main/kotlin/com/lifesurvivor/
│       ├── LifeSurvivorGame.kt   # メインゲームクラス
│       ├── data/                 # データ定義
│       │   ├── TriviaDatabase.kt     # 豆知識DB (21件)
│       │   ├── EraData.kt            # 時代データ (7時代)
│       │   ├── EnemyDefinitions.kt   # 敵データ (19種)
│       │   └── WeaponDefinitions.kt  # 武器データ (8種)
│       ├── entity/               # エンティティ
│       │   ├── Player.kt
│       │   ├── Enemy.kt
│       │   └── Projectile.kt
│       ├── system/               # ゲームシステム
│       │   ├── MovementSystem.kt
│       │   ├── AttackSystem.kt
│       │   ├── CollisionSystem.kt
│       │   ├── EnemySpawnSystem.kt
│       │   └── XpSystem.kt
│       ├── ui/                   # UI
│       │   ├── HUD.kt
│       │   └── TriviaCard.kt
│       ├── screen/               # 画面
│       │   ├── TitleScreen.kt
│       │   ├── GameScreen.kt
│       │   ├── GameOverScreen.kt
│       │   ├── LevelUpScreen.kt
│       │   └── TriviaScreen.kt
│       └── weapon/               # 武器
│           ├── Weapon.kt
│           ├── WeaponDefinitions.kt
│           └── weapons/          # 各武器クラス (8ファイル)
├── android/                      # Android版
│   ├── build.gradle.kts
│   ├── AndroidManifest.xml
│   └── src/main/kotlin/.../AndroidLauncher.kt
└── desktop/                      # Desktop版
    ├── build.gradle.kts
    └── src/main/kotlin/.../DesktopLauncher.kt
```

## ビルド方法

### Desktop版
```bash
cd LifeSurvivor
./gradlew :desktop:run
```

### Android版
```bash
cd LifeSurvivor
./gradlew :app:assembleDebug
```

APKは `android/build/outputs/apk/debug/` に出力されます。


## Android StudioでSyncできない場合（`error: module not specified`）

- **開くフォルダは `LifeSurvivor/` を指定**してください（Android StudioのGradleプロジェクトはこの階層です）。
- 既に別フォルダ（例: リポジトリ直下）を開いている場合は、いったん閉じて `LifeSurvivor/` を開き直してください。
- `Gradle JDK` は **17** を指定してください。
- うまくいかない場合は `File > Invalidate Caches...` 実行後、再Syncしてください。
- `Run/Debug Configuration` の `Module` が空の場合は **`app`** を選択し、Gradle-aware Make を有効にしてください。

## 必要環境

- JDK 17+
- Android SDK (Android版ビルド時: minSdk 21 / targetSdk 34)
- Gradle 8.x+

## ライセンス

このプロジェクトは教育目的のゲームです。
