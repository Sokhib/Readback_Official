package com.sokhibdzhon.readback.util.binding

import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.widget.TextView
import androidx.databinding.BindingAdapter


/**     I ❤ Code:)
╔═══════════════════════════════════════╗
║  Created by Sokhibdzhon Saidmuratov   ║
╠═══════════════════════════════════════╣
║ sokhibsaid@gmail.com                  ║
╚═══════════════════════════════════════╝
 */


@BindingAdapter("spanText")
fun spanText(textView: TextView, score: Int = 0) {
    val scoreSpan = SpannableString("score${System.lineSeparator()}$score")
    scoreSpan.setSpan(
        RelativeSizeSpan(0.9f),
        0, scoreSpan.indexOf("\n"),
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    textView.text = scoreSpan
}