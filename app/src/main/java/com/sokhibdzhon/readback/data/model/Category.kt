package com.sokhibdzhon.readback.data.model

import androidx.annotation.DrawableRes


/**     I ❤ Code:)
╔═══════════════════════════════════════╗
║  Created by Sokhibdzhon Saidmuratov   ║
╠═══════════════════════════════════════╣
║ sokhibsaid@gmail.com                  ║
╚═══════════════════════════════════════╝
 */

data class Category(
    val categoryName: String,
    @DrawableRes val categoryIcon: Int,
    var isClicked: Boolean = false
)