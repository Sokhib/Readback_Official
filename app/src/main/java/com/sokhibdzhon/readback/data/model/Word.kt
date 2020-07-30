package com.sokhibdzhon.readback.data.model


/**     I ❤ Code:)
╔═══════════════════════════════════════╗
║  Created by Sokhibdzhon Saidmuratov   ║
╠═══════════════════════════════════════╣
║ sokhibsaid@gmail.com                  ║
╚═══════════════════════════════════════╝
 */

data class Word(
    val correct: String = "",
    val options: MutableList<String>,
    val word: String = ""
) {
    @Suppress("unused")
    constructor() : this("", mutableListOf("", "", "", ""), "")
}



