package com.nsp.timetracker.base

import com.nsp.timetracker.BuildConfig

class Constants {
    companion object {
        const val BASE_URL: String = BuildConfig.SERVER_URL
        const val API_URL: String = "$BASE_URL/api"
        const val API_V3: String = "$API_URL/v3/"

        const val SIMPLE_ENDPOINT = API_V3 + "simple/"
        const val COINS_ENDPOINT = API_V3 + "coins/"

        const val DATABASE_NAME: String = BuildConfig.DATABASE_NAME

        const val CHANNEL_ID: String = "2491740238"

        const val LIVE_REFRESH_TIME: Long = 3_000
    }
}