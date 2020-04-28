package com.trevexs.kyeko.utils

import android.content.Context
import android.content.SharedPreferences
import com.trevexs.kyeko.interfaces.Constants.Intents.CURRENT_ROUND
import com.trevexs.kyeko.interfaces.Constants.Intents.CURRENT_TEAM
import com.trevexs.kyeko.interfaces.Constants.Intents.GAME_ID


class SharedPrefs(val context: Context) {

    private var preferences: SharedPreferences = context.getSharedPreferences("SengaApp", Context.MODE_PRIVATE)

    fun setGameId(gameId: Long) {
        val editor = preferences.edit()
        editor.putLong(GAME_ID, gameId)
        editor.apply()
    }

    fun getGameId(): Long {
        return preferences.getLong(GAME_ID, -1)
    }

    fun setCurrentTeam(currentTeam: Int) {
        val editor = preferences.edit()
        editor.putInt(CURRENT_TEAM, currentTeam)
        editor.apply()
    }

    fun getCurrentTeam(): Int {
        return preferences.getInt(CURRENT_TEAM, 0)
    }

    fun setCurrentRound(currentRound: Int) {
        val editor = preferences.edit()
        editor.putInt(CURRENT_ROUND, currentRound)
        editor.apply()
    }

    fun getCurrentRound(): Int {
        return preferences.getInt(CURRENT_ROUND, 1)
    }

}