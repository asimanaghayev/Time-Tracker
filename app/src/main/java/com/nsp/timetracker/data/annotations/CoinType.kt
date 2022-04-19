package com.nsp.timetracker.data.annotations

import androidx.annotation.StringDef
import com.nsp.timetracker.data.annotations.CoinType.Companion.BITCOIN
import com.nsp.timetracker.data.annotations.CoinType.Companion.ETHEREUM
import com.nsp.timetracker.data.annotations.CoinType.Companion.RIPPLE

@StringDef(BITCOIN, ETHEREUM, RIPPLE)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class CoinType {
    companion object {
        const val BITCOIN = "bitcoin"

        const val ETHEREUM = "ethereum"

        const val RIPPLE = "ripple"

        val ALL = listOf(BITCOIN, ETHEREUM, RIPPLE)
    }
}