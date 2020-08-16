package com.sokhibdzhon.readback.data.repository

import com.sokhibdzhon.readback.data.Resource
import com.sokhibdzhon.readback.data.model.Word
import com.sokhibdzhon.readback.util.enums.GameType
import kotlinx.coroutines.flow.Flow


/**     I ❤ Code:)
╔═══════════════════════════════════════╗
║  Created by Sokhibdzhon Saidmuratov   ║
╠═══════════════════════════════════════╣
║ sokhibsaid@gmail.com                  ║
╚═══════════════════════════════════════╝
 */

interface GameRepo {
    //Custom Game
    fun getCustomGameWords(level: Int, type: GameType): Flow<Resource<MutableList<Word>>>
    fun getLevelSkips(): Int
    fun getLevel(): Int
}