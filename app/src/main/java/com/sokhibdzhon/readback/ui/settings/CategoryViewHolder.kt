package com.sokhibdzhon.readback.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sokhibdzhon.readback.R
import com.sokhibdzhon.readback.data.model.Category
import com.sokhibdzhon.readback.databinding.CategoryItemBinding
import timber.log.Timber
import java.util.*


/**     I ❤ Code:)
╔═══════════════════════════════════════╗
║  Created by Sokhibdzhon Saidmuratov   ║
╠═══════════════════════════════════════╣
║ sokhibsaid@gmail.com                  ║
╚═══════════════════════════════════════╝
 */

class CategoryViewHolder(
    private val binding: CategoryItemBinding,
    private val onCategoryItemClicked: ((Int, String) -> Unit)?
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.linearCategory.setOnClickListener {
            onCategoryItemClicked?.invoke(
                layoutPosition,
                binding.textviewCategoryName.text.toString().toLowerCase(Locale.getDefault())
            )
        }
    }

    fun bind(category: Category) {
        binding.categoryItemViewState =
            CategoryItemViewState(category)
        Timber.d(category.categoryIcon.toString())
        binding.executePendingBindings()
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onMatchItemClicked: ((Int, String) -> Unit)?
        ): CategoryViewHolder {
            val binding = DataBindingUtil.inflate<CategoryItemBinding>(
                LayoutInflater.from(parent.context),
                R.layout.category_item,
                parent,
                false
            )
            return CategoryViewHolder(
                binding,
                onMatchItemClicked
            )
        }
    }


}