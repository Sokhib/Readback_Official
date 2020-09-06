package com.sokhibdzhon.readback.ui.settings

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sokhibdzhon.readback.data.model.Category
import javax.inject.Inject


/**     I ❤ Code:)
╔═══════════════════════════════════════╗
║  Created by Sokhibdzhon Saidmuratov   ║
╠═══════════════════════════════════════╣
║ sokhibsaid@gmail.com                  ║
╚═══════════════════════════════════════╝
 */

class CategoryAdapter @Inject constructor() : RecyclerView.Adapter<CategoryViewHolder>() {

    private val categoryList = arrayListOf<Category>()
    var onCategoryItemClicked: ((Int, String) -> Unit)? = null
    fun setCategoryList(categoryList: List<Category>) {
        this.categoryList.apply {
            clear()
            addAll(categoryList)
        }
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CategoryViewHolder.create(
            parent,
            onCategoryItemClicked
        )

    override fun getItemCount() = categoryList.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        return holder.bind(categoryList[position])
    }

    fun setCheckedState(position: Int) {
        categoryList.forEach {
            it.isClicked = false
        }
        categoryList[position].isClicked = true
        notifyDataSetChanged()
    }

}