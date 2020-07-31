package com.sokhibdzhon.readback.util.binding

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.sokhibdzhon.readback.data.Status


/**     I ❤ Code:)
╔═══════════════════════════════════════╗
║  Created by Sokhibdzhon Saidmuratov   ║
╠═══════════════════════════════════════╣
║ sokhibsaid@gmail.com                  ║
╚═══════════════════════════════════════╝
 */

@BindingAdapter("progressBarVisibility")
fun progressBarVisibility(progressBar: ProgressBar, status: Status?) {
    progressBar.visibility = status?.let { it ->
        when (it) {
            Status.LOADING -> {
                View.VISIBLE
            }
            else -> {
                View.GONE
            }
        }
    } ?: run {
        View.VISIBLE
    }
}