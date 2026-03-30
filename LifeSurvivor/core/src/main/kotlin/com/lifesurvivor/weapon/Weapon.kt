package com.lifesurvivor.weapon

import com.lifesurvivor.data.WeaponData
import com.lifesurvivor.data.WeaponDefinitions

/**
 * 武器基底クラス
 * 武器データへのアクセスとレベル管理を提供する
 */
open class Weapon(val weaponId: String) {
    val data: WeaponData? = WeaponDefinitions.getById(weaponId)
    var level: Int = 1

    /** 現在のダメージ（レベル補正込み） */
    fun getDamage(damageMultiplier: Float = 1f): Float {
        val base = data?.baseDamage ?: 0f
        return base * (1f + (level - 1) * 0.3f) * damageMultiplier
    }

    /** 現在のクールダウン（攻撃速度補正込み） */
    fun getCooldown(attackSpeedMultiplier: Float = 1f): Float {
        val base = data?.baseCooldown ?: 1f
        return base / attackSpeedMultiplier
    }

    /** 現在の範囲（範囲補正込み） */
    fun getArea(areaMultiplier: Float = 1f): Float {
        val base = data?.baseArea ?: 0f
        return base * areaMultiplier * (1f + (level - 1) * 0.1f)
    }
}
