package com.sokhibdzhon.readback.data.repository

import com.sokhibdzhon.readback.data.Resource
import com.sokhibdzhon.readback.data.model.Word
import com.sokhibdzhon.readback.data.network.custom.CustomGameDataSource
import com.sokhibdzhon.readback.util.enum.GameType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**     I ❤ Code:)
╔═══════════════════════════════════════╗
║  Created by Sokhibdzhon Saidmuratov   ║
╠═══════════════════════════════════════╣
║ sokhibsaid@gmail.com                  ║
╚═══════════════════════════════════════╝
 */

class GameRepoImpl @Inject constructor(private val customGameDataSourceImpl: CustomGameDataSource) :
    GameRepo {

    override fun getCustomGameWords(level: Int, type: Int): Flow<Resource<MutableList<Word>>> {
        return if (type == GameType.CUSTOMGAME.type)
            customGameDataSourceImpl.getWords()
        else {
            customGameDataSourceImpl.getWords(level.toString())
        }
    }

}

