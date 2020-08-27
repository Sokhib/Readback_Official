package com.sokhibdzhon.readback.ui.score

import android.content.Context
import android.content.Intent
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.gms.ads.AdRequest
import com.sokhibdzhon.readback.BaseApplication
import com.sokhibdzhon.readback.R
import com.sokhibdzhon.readback.databinding.ScoreFragmentBinding
import com.sokhibdzhon.readback.util.enums.NavigationType
import com.sokhibdzhon.readback.util.enums.navigate
import javax.inject.Inject

//TODO: Move all the logic to ViewModel.
class ScoreFragment : Fragment() {


    private lateinit var viewModel: ScoreViewModel
    private lateinit var binding: ScoreFragmentBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val args: ScoreFragmentArgs by navArgs()

    @Inject
    lateinit var adRequest: AdRequest

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().applicationContext as BaseApplication).appGraph.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.score_fragment, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ScoreViewModel::class.java)
        //ad
        binding.adViewScore.loadAd(adRequest)
        lifecycleScope.launchWhenCreated {
            viewModel.setScore(args.score)
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setGameScoreTextSpan(viewModel.score.value ?: 0, viewModel.bestScore.value ?: 0)
        binding.imageviewHome.setOnClickListener {
            navigate(NavigationType.SCOREHOME)
        }

        binding.imageviewShare.setOnClickListener {
            shareMyScore(viewModel.bestScore.value ?: 0)
        }

        binding.textviewStartGameScore.setOnClickListener {
            navigate(NavigationType.SCOREGAME)
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
        }, getString(R.string.app_name))
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