package com.sokhibdzhon.readback.data.repository

import android.content.SharedPreferences
import com.sokhibdzhon.readback.data.Resource
import com.sokhibdzhon.readback.data.model.Word
import com.sokhibdzhon.readback.data.network.custom.CustomGameDataSource
import com.sokhibdzhon.readback.util.enums.GameType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**     I ❤ Code:)
╔═══════════════════════════════════════╗
║  Created by Sokhibdzhon Saidmuratov   ║
╠═══════════════════════════════════════╣
║ sokhibsaid@gmail.com                  ║
╚═══════════════════════════════════════╝
 */

class GameRepoImpl @Inject constructor(
    private val customGameDataSourceImpl: CustomGameDataSource,
    private val sharedPref: SharedPreferences
) :
    GameRepo {
    companion object {
        private const val LEVELSKIPS = "levelSkips"
        private const val LEVEL = "level"
    }

    override fun getCustomGameWords(level: Int, type: GameType): Flow<Resource<MutableList<Word>>> {
        return if (type == GameType.CUSTOMGAME)
            customGameDataSourceImpl.getWords()
        else {
            customGameDataSourceImpl.getWords(level.toString())
        }
    }

    override fun getLevelSkips(): Int = sharedPref.getInt(LEVELSKIPS, 15)
    override fun getLevel(): Int = sharedPref.getInt(LEVEL, 1)


}

