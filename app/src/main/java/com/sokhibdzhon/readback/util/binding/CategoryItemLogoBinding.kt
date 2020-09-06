package com.sokhibdzhon.readback.util.binding

import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.setPadding
import androidx.databinding.BindingAdapter
import com.sokhibdzhon.readback.R


/**     I ❤ Code:)
╔═══════════════════════════════════════╗
║  Created by Sokhibdzhon Saidmuratov   ║
╠═══════════════════════════════════════╣
║ sokhibsaid@gmail.com                  ║
╚═══════════════════════════════════════╝
 */

@BindingAdapter("linearBackground")
fun setLinearBackground(linear: LinearLayoutCompat, isClicked: Boolean) {
    linear.background = if (isClicked) ResourcesCompat.getDrawable(
        linear.resources,
        R.drawable.round_stroke_green_background,
        linear.context.theme
    ) else ResourcesCompat.getDrawable(
        linear.resources,
        R.drawable.round_white_background,
        linear.context.theme
    )
    linear.setPadding(8)
}