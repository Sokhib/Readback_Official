package com.sokhibdzhon.readback.ui.settings

import com.sokhibdzhon.readback.data.model.Category


/**     I ❤ Code:)
╔═══════════════════════════════════════╗
║  Created by Sokhibdzhon Saidmuratov   ║
╠═══════════════════════════════════════╣
║ sokhibsaid@gmail.com                  ║
╚═══════════════════════════════════════╝
 */

data class CategoryItemViewState(val category: Category) {
    fun getCategoryName() = category.categoryName
    fun getCategoryLogo() = category.categoryIcon
    fun getIsClicked() = category.isClicked
}