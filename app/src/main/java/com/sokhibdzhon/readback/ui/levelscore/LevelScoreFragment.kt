package com.sokhibdzhon.readback.ui.levelscore

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.ads.AdRequest
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.review.ReviewManagerFactory
import com.sokhibdzhon.readback.BaseApplication
import com.sokhibdzhon.readback.R
import com.sokhibdzhon.readback.databinding.LevelScoreFragmentBinding
import com.sokhibdzhon.readback.util.enums.LevelResult
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

class LevelScoreFragment : Fragment() {

    private lateinit var levelResult: LevelResult
    private lateinit var viewModel: LevelScoreViewModel
    private lateinit var binding: LevelScoreFragmentBinding
    private val args: LevelScoreFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var adRequest: AdRequest

    private val reviewManager by lazy { ReviewManagerFactory.create(requireActivity()) }
    private val mpLevelFail by lazy { MediaPlayer.create(requireContext(), R.raw.failfare) }
    private val mpLevelPass by lazy { MediaPlayer.create(requireContext(), R.raw.goodresult) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().applicationContext as BaseApplication).appGraph.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.level_score_fragment, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory).get(LevelScoreViewModel::class.java)
        levelResult = args.result
        if (levelResult == LevelResult.SUCCESS) {
            binding.animationViewLottie.setAnimation(R.raw.partypopper)
            mpLevelPass.start()
            Timber.d("${viewModel.getLevel()}")
            if (viewModel.getLevel() == 10) {
                Timber.d("Place to show review... ")
                initReview()
            }

        } else {
            binding.animationViewLottie.setAnimation(R.raw.failedattempt)
            mpLevelFail.start()
        }
        binding.adViewScore.loadAd(adRequest)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.isSaved.onEach { isSaved ->
            if (!isSaved && levelResult == LevelResult.SUCCESS) {
                viewModel.updateLevel()
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.textviewContinue.setOnClickListener {
            val direction = LevelScoreFragmentDirections.actionLevelScoreFragmentToStartFragment()
            this.findNavController().navigate(direction)
        }

    }

    private fun initReview() {
        reviewManager.requestReviewFlow().addOnCompleteListener { request ->
            if (request.isSuccessful) {
                Timber.d("Review request  SUCCESSFUL")
                reviewManager.launchReviewFlow(requireActivity(), request.result)
                    .addOnFailureListener {
                        Snackbar.make(requireView(), "Review Not Successful", Snackbar.LENGTH_SHORT)
                            .show()
                    }
                    .addOnCompleteListener {
                        Snackbar.make(
                            requireView(),
                            "Thanks for the Review.",
                            Snackbar.LENGTH_SHORT
                        )
                            .show()
                    }
            } else {
                Timber.d("Review Request Not Successful")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mpLevelPass.stop()
        mpLevelPass.release()
        mpLevelFail.stop()
        mpLevelFail.release()
    }

}