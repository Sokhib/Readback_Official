package com.sokhibdzhon.readback.util.binding

import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.sokhibdzhon.readback.R
import com.sokhibdzhon.readback.util.enums.GameType
import timber.log.Timber


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

@BindingAdapter(value = ["type", "score", "size"])
fun spanScoreText(textView: TextView, type: GameType, score: Int = 0, size: Int = 0) {
    Timber.d("Sizeis in Binding: $size")
    textView.text = if (type == GameType.LEVELSGAME) {
        "$score / $size"
    } else {
        val scoreSpan = SpannableString("score${System.lineSeparator()}$score")
        scoreSpan.setSpan(
            RelativeSizeSpan(0.9f),
            0, scoreSpan.indexOf("\n"),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        scoreSpan
    }
}

@BindingAdapter("levelText")
fun levelText(textView: TextView, level: Int = 1) {
    textView.text = textView.resources.getString(R.string.level, level)
}

@BindingAdapter("customSkipText")
fun customSkipText(textView: TextView, skip: Int) {
    Timber.d("Skips in binding: $skip")
    textView.text = textView.resources.getString(R.string.skips, skip)
}

@BindingAdapter("customTimeText")
fun customTimeText(textView: TextView, time: Int) {
    textView.text = textView.resources.getString(R.string.seconds, time)
}