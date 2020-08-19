package com.sokhibdzhon.readback.data.repository

import android.content.SharedPreferences
import com.sokhibdzhon.readback.data.Resource
import com.sokhibdzhon.readback.data.model.Word
import com.sokhibdzhon.readback.data.network.custom.CustomGameDataSource
import com.sokhibdzhon.readback.util.Constants
import com.sokhibdzhon.readback.util.enums.GameType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**     I ❤ Code:)
╔═══════════════════════════════════════╗
║  Created by Sokhibdzhon Saidmuratov   ║
╠═══════════════════════════════════════╣
║ sokhibsaid@gmail.com                  ║
╚═══════════════════════════════════════╝
 */
@Singleton
class GameRepoImpl @Inject constructor(
    private val customGameDataSourceImpl: CustomGameDataSource,
    private val sharedPref: SharedPreferences
) :
    GameRepo {

    override fun getCustomGameWords(level: Int, type: GameType): Flow<Resource<MutableList<Word>>> {
        return if (type == GameType.CUSTOMGAME)
            customGameDataSourceImpl.getWords()
        else {
            customGameDataSourceImpl.getWords(level.toString())
        }
    }

    override fun getLevel(): Int = sharedPref.getInt(Constants.LEVEL, 1)
    override fun getTimeLeft(type: GameType): Long =
        when (type) {
            GameType.CUSTOMGAME -> sharedPref.getInt(Constants.CUSTOM_SECONDS, 15).toLong()
            GameType.LEVELSGAME -> sharedPref.getInt(Constants.LEVEL_SECONDS, 30).toLong()
        }


    override
    fun getSkips(type: GameType): Int =
        when (type) {
            GameType.CUSTOMGAME -> sharedPref.getInt(Constants.CUSTOM_SKIPS, 1)
            GameType.LEVELSGAME -> sharedPref.getInt(Constants.LEVEL_SKIPS, 5)
        }

    override fun getBestScore(): Int = sharedPref.getInt(Constants.BEST_SCORE, 0)


    override fun updateBestScore(bestScore: Int) {
        sharedPref.edit().putInt(Constants.BEST_SCORE, bestScore).apply()
    }
}

