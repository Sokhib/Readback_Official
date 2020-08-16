package com.sokhibdzhon.readback.ui.levelscore

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.ads.AdRequest
import com.sokhibdzhon.readback.BaseApplication
import com.sokhibdzhon.readback.R
import com.sokhibdzhon.readback.databinding.LevelScoreFragmentBinding
import com.sokhibdzhon.readback.util.enums.LevelResult
import javax.inject.Inject

class LevelScoreFragment : Fragment() {

    private lateinit var viewModel: LevelScoreViewModel
    private lateinit var binding: LevelScoreFragmentBinding
    val args: LevelScoreFragmentArgs by navArgs()

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
        binding = DataBindingUtil.inflate(inflater, R.layout.level_score_fragment, container, false)
        if (args.result == LevelResult.SUCCESS) {
            binding.animationViewLottie.setAnimation(R.raw.partypopper)
        } else {
            binding.animationViewLottie.setAnimation(R.raw.failedattempt)
        }
        binding.adViewScore.loadAd(adRequest)


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.textviewContinue.setOnClickListener {
            val direction = LevelScoreFragmentDirections.actionLevelScoreFragmentToStartFragment()
            this.findNavController().navigate(direction)
        }

    }


}