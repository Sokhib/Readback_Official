package com.sokhibdzhon.readback.data.repository

import android.content.SharedPreferences
import com.sokhibdzhon.readback.data.Resource
import com.sokhibdzhon.readback.data.model.Word
import com.sokhibdzhon.readback.data.network.custom.CustomGameDataSource
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
    companion object {
        private const val LEVELSKIPS = "levelSkips"
        private const val LEVEL = "level"
        private const val SECONDS = "seconds"
        private const val SKIPS = "skips"


    }

    override fun getCustomGameWords(level: Int, type: GameType): Flow<Resource<MutableList<Word>>> {
        return if (type == GameType.CUSTOMGAME)
            customGameDataSourceImpl.getWords()
        else {
            customGameDataSourceImpl.getWords(level.toString())
        }
    }

    override fun getLevelSkips(): Int = sharedPref.getInt(LEVELSKIPS, 1)
    override fun getLevel(): Int = sharedPref.getInt(LEVEL, 1)
    override fun getTimeLeft(): Long = sharedPref.getInt(SECONDS, 15).toLong()
    override fun getCustomSkips(): Int = sharedPref.getInt(SKIPS, 1)

}

