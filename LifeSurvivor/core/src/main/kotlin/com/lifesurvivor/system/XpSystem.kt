package com.lifesurvivor.system

import com.lifesurvivor.entity.Player

/**
 * XPシステム
 * 経験値とレベルアップの計算を管理する
 */
class XpSystem {

    /**
     * レベルアップに必要なXPを計算
     * 計算式: 10 * level^1.3
     */
    fun calculateXpRequired(level: Int): Float {
        return 10f * Math.pow(level.toDouble(), 1.3).toFloat()
    }

    /**
     * レベルアップ時の選択肢を生成
     * 毎回3択（武器 or パッシブをランダム提示）
     */
    fun generateLevelUpChoices(player: Player): List<LevelUpChoice> {
        val choices = mutableListOf<LevelUpChoice>()
        val allWeapons = com.lifesurvivor.data.WeaponDefinitions.weapons.shuffled()

        for (weapon in allWeapons) {
            if (choices.size >= 3) break

            val currentLevel = player.weaponLevels[weapon.id]

            if (currentLevel == null) {
                // 新規武器の獲得
                choices.add(LevelUpChoice(
                    weaponId = weapon.id,
                    weaponName = weapon.name,
                    description = weapon.description,
                    isNew = true,
                    newLevel = 1
                ))
            } else if (currentLevel < weapon.maxLevel) {
                // 既存武器の強化
                choices.add(LevelUpChoice(
                    weaponId = weapon.id,
                    weaponName = weapon.name,
                    description = "Lv.${currentLevel + 1} に強化",
                    isNew = false,
                    newLevel = currentLevel + 1
                ))
            }
        }

        // 選択肢が3つに満たない場合は回復オプションを追加
        while (choices.size < 3) {
            choices.add(LevelUpChoice(
                weaponId = "_heal",
                weaponName = "生命の回復",
                description = "HPを30%回復する",
                isNew = false,
                newLevel = 0
            ))
            if (choices.size < 3) {
                choices.add(LevelUpChoice(
                    weaponId = "_speed",
                    weaponName = "進化の加速",
                    description = "移動速度+10%",
                    isNew = false,
                    newLevel = 0
                ))
            }
        }

        return choices.take(3)
    }
}

/**
 * レベルアップ選択肢データ
 */
data class LevelUpChoice(
    val weaponId: String,
    val weaponName: String,
    val description: String,
    val isNew: Boolean,    // 新規武器かどうか
    val newLevel: Int
)
