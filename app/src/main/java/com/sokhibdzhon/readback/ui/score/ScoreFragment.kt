package com.sokhibdzhon.readback.ui.score

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import androidx.navigation.fragment.navArgs
import com.google.android.gms.ads.AdRequest
import com.sokhibdzhon.readback.BaseApplication
import com.sokhibdzhon.readback.R
import com.sokhibdzhon.readback.databinding.ScoreFragmentBinding
import com.sokhibdzhon.readback.util.enum.NavigationType
import com.sokhibdzhon.readback.util.enum.navigate
import javax.inject.Inject

//TODO: Move all the logic to ViewModel.
class ScoreFragment : Fragment() {


    private lateinit var viewModel: ScoreViewModel
    private lateinit var binding: ScoreFragmentBinding
    val args: ScoreFragmentArgs by navArgs()

    @Inject
    lateinit var sharedPrefEditor: SharedPreferences

    @Inject
    lateinit var adRequest: AdRequest

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().applicationContext as BaseApplication).appGraph.inject(this)
    }

    private var score = 0
    private var bestScore = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.score_fragment, container, false)
        score = args.score
        binding.textviewStartGameScore.setOnClickListener {
            navigate(NavigationType.SCOREGAME)
        }
        bestScore = sharedPrefEditor.getInt(getString(R.string.score), 0)
        bestScore = bestScore.coerceAtLeast(score)
        sharedPrefEditor.edit().putInt(getString(R.string.score), bestScore)
            .apply()
        //ad
        binding.adViewScore.loadAd(adRequest)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ScoreViewModel::class.java)
        setGameScoreTextSpan(score, bestScore)
        binding.imageviewHome.setOnClickListener {
            navigate(NavigationType.SCOREHOME)
        }
        binding.imageviewShare.setOnClickListener {
            shareMyScore(bestScore)
        }

    }

    private fun shareMyScore(bestScore: Int) {
        val share = Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            type = "plain/*"
            putExtra(
                Intent.EXTRA_TEXT,
                "Hey, I scored $bestScore points on Readback game. Can you beat my record?! Check out app:\n" +
                        "https://play.google.com/store/apps/details?id=${requireContext().packageName}"
            )
        }, "Readback")
        startActivity(share)
    }

    private fun setGameScoreTextSpan(score: Int, bestScore: Int) {
        val scoreTextSpannable =
            SpannableString("game over! ${System.lineSeparator()} ${System.lineSeparator()} your score:\t$score ${System.lineSeparator()} your best score:\t$bestScore")

        //Game Over text size 0.7%
        scoreTextSpannable.setSpan(
            RelativeSizeSpan(0.7f),
            0, scoreTextSpannable.indexOf("!") + 1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        //current score color
        scoreTextSpannable.setSpan(
            ForegroundColorSpan(
                ResourcesCompat.getColor(
                    resources,
                    R.color.yellow_900,
                    requireContext().theme
                )
            ),
            scoreTextSpannable.indexOf("\t"),
            scoreTextSpannable.lastIndexOf("\n"),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        //Best score color
        scoreTextSpannable.setSpan(
            ForegroundColorSpan(
                ResourcesCompat.getColor(
                    resources,
                    R.color.green_500,
                    requireContext().theme
                )
            ),
            scoreTextSpannable.lastIndexOf("\t"),
            scoreTextSpannable.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        //BOLD BEST SCORE
        scoreTextSpannable.setSpan(
            StyleSpan(BOLD),
            scoreTextSpannable.lastIndexOf("\t"),
            scoreTextSpannable.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.textViewScore.text = scoreTextSpannable

    }

}