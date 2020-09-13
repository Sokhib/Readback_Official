package com.sokhibdzhon.readback.data.model

import androidx.annotation.DrawableRes
import com.sokhibdzhon.readback.R


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

object Categories {
    fun getCategoriesList() = listOf(
        Category("Custom", R.drawable.ic_custom, true),
        Category("Sport", R.drawable.ic_sport),
        Category("Animals", R.drawable.ic_animals),
        Category("Food & Drinks", R.drawable.ic_food),
        Category("Clothes", R.drawable.ic_clothes),
        Category("Jobs", R.drawable.ic_jobs),
        Category("Body Parts", R.drawable.ic_bodyparts),
        Category("Plant & Flower", R.drawable.ic_plants),
        Category("Countries", R.drawable.ic_countries),
        Category("Fruits", R.drawable.ic_fruit)
    )
}
