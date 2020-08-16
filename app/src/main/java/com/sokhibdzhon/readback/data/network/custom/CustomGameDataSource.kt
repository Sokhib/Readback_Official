package com.sokhibdzhon.readback.data.network.custom

import com.sokhibdzhon.readback.data.Resource
import com.sokhibdzhon.readback.data.model.Word
import kotlinx.coroutines.flow.Flow


/**     I ❤ Code:)
╔═══════════════════════════════════════╗
║  Created by Sokhibdzhon Saidmuratov   ║
╠═══════════════════════════════════════╣
║ sokhibsaid@gmail.com                  ║
╚═══════════════════════════════════════╝
 */

interface CustomGameDataSource {
    fun getWords(level: Int, type: Int): Flow<Resource<MutableList<Word>>>
}