package com.sokhibdzhon.readback.ui.score

import android.graphics.Typeface.BOLD
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sokhibdzhon.readback.R
import com.sokhibdzhon.readback.databinding.ScoreFragmentBinding

class ScoreFragment : Fragment() {


    private lateinit var viewModel: ScoreViewModel
    private lateinit var binding: ScoreFragmentBinding
    val args: ScoreFragmentArgs by navArgs()

    //TODO: score should go to viewModel and come from there + max score should be save in Room DB to show in start screen
    private var score = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.score_fragment, container, false)
        score = args.score
        binding.textviewStartGameScore.setOnClickListener {
            startNewGame()
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ScoreViewModel::class.java)
        setGameScoreTextSpan(score)
    }

    private fun setGameScoreTextSpan(score: Int) {
        val scoreTextSpannable =
            SpannableString("game over! ${System.lineSeparator()} ${System.lineSeparator()} your score:  $score")

        //Game Over text size 0.7%
        scoreTextSpannable.setSpan(
            RelativeSizeSpan(0.7f),
            0, scoreTextSpannable.indexOf("!") + 1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // SCORE COLOR
        scoreTextSpannable.setSpan(
            ForegroundColorSpan(
                ResourcesCompat.getColor(
                    resources,
                    R.color.yellow_900,
                    requireContext().theme
                )
            ),
            scoreTextSpannable.lastIndexOf(" "),
            scoreTextSpannable.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        //BOLD SCORE
        scoreTextSpannable.setSpan(
            StyleSpan(BOLD),
            scoreTextSpannable.lastIndexOf(" "),
            scoreTextSpannable.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.textViewScore.text = scoreTextSpannable

    }

    private fun startNewGame() {
        val action = ScoreFragmentDirections.actionScoreFragmentToGameFragment()
        findNavController().navigate(action)
    }

}