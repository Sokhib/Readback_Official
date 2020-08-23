package com.sokhibdzhon.readback.ui.game

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.get
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.material.snackbar.Snackbar
import com.sokhibdzhon.readback.BaseApplication
import com.sokhibdzhon.readback.R
import com.sokhibdzhon.readback.data.Status
import com.sokhibdzhon.readback.databinding.GameFragmentBinding
import com.sokhibdzhon.readback.util.enums.GameType
import com.sokhibdzhon.readback.util.enums.LevelResult
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class GameFragment : Fragment(), View.OnClickListener {

    private lateinit var viewModel: GameViewModel
    private lateinit var binding: GameFragmentBinding

    @Inject
    lateinit var rewardedAd: RewardedAd

    @Inject
    lateinit var adRequest: AdRequest

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val args: GameFragmentArgs by navArgs()
    private var isCorrect = false
    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().applicationContext as BaseApplication).appGraph.inject(this)
    }

    private var options = listOf<TextView>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory).get(GameViewModel::class.java)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            gmviewmodel = viewModel
        }
        options = listOf(
            binding.textOption1,
            binding.textOption2,
            binding.textOption3,
            binding.textOption4
        )
        lifecycleScope.launchWhenCreated {
            viewModel.setType(args.type)
            viewModel.getTimeLeftByType(args.type)
            viewModel.getSkipByType(args.type)
            viewModel.getWords(args.level, args.type)
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.circularTimeView.apply { progressMax = Float.MAX_VALUE }
        binding.imageviewClose.setOnClickListener {
            findNavController().popBackStack()
        }
        //timeleft
        viewModel.timeLeft.observe(viewLifecycleOwner, Observer { timeLeft ->
            if (binding.circularTimeView.progressMax == Float.MAX_VALUE)
                binding.circularTimeView.progressMax = timeLeft.toFloat()
            binding.circularTimeView.setProgressWithAnimation(timeLeft.toFloat(), 1000)
        })
        //words
        viewModel.wordList.observe(viewLifecycleOwner, Observer { words ->
            when (words.status) {
                Status.SUCCESS -> {
                    viewModel.startTimer()
                    binding.size = words.data?.size
                    options.forEach { option ->
                        option.setOnClickListener(this)
                    }
                    binding.skip.setOnClickListener {
                        if (viewModel.getSkipNumber() == 0) {
                            Snackbar.make(binding.root, "No Skips Left", Snackbar.LENGTH_SHORT)
                                .show()
                            binding.skip.setOnClickListener(null)
                        } else {
                            viewModel.setCorrectness(true)
                            viewModel.updateScore(1)
                            viewModel.minusSkip()
                            viewModel.nextWord()
                            startAnimation()
                        }
                    }
                    viewModel.nextWord()
                    startAnimation()
                }
                Status.ERROR -> {
                    binding.progressWordLoad.visibility = View.INVISIBLE
                    Toast.makeText(
                        requireActivity(),
                        "No Words or Check Internet Connection",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                Status.LOADING -> {
                }
            }
        })
        //Game finish
        viewModel.gameFinish.observe(viewLifecycleOwner, Observer { hasFinished ->
            if (hasFinished) {
                gameFinished()
                viewModel.onGameFinished()
            }
        })
    }

    override fun onClick(v: View?) {
        isActiveOptions(false)
        isCorrect = viewModel.checkForCorrectness((v as TextView).text.toString())
        setOptionBackground(v, isCorrect)
        //set game finish
        if (!isCorrect) viewModel.setGameFinish(true)
        else {
            getNextWord()
        }

    }

    //Functions
    private fun clearOptionsBackground() {
        options.forEach { option ->
            option.background = ResourcesCompat.getDrawable(
                resources,
                R.drawable.round_white_background,
                requireContext().theme
            )
        }
    }

    private fun getNextWord() {
        lifecycleScope.launch {
            coroutineScope {
                delay(500)
                clearOptionsBackground()
                viewModel.nextWord()
                startAnimation()
                isActiveOptions(true)
            }

        }

    }

    private fun gameFinished() {
        val action: NavDirections
        if (args.type == GameType.CUSTOMGAME) {
            action =
                GameFragmentDirections.actionGameFragmentToScoreFragment(viewModel.score.value ?: 0)
            with(findNavController()) {
                if (currentDestination != graph[R.id.scoreFragment] || currentDestination != graph[R.id.levelScoreFragment]) {
                    navigate(action)
                }
            }
        } else {
            if (viewModel.isCorrect()!!) {
                Timber.d("isCorrect in Main Game Finish: $isCorrect")
                Timber.d("viewModel.isCorrect() in Main Game Finish: ${viewModel.isCorrect()}")
                action =
                    GameFragmentDirections.actionGameFragmentToLevelScoreFragment(LevelResult.SUCCESS)
                with(findNavController()) {
                    if (currentDestination != graph[R.id.scoreFragment] || currentDestination != graph[R.id.levelScoreFragment]) {
                        navigate(action)
                    }
                }
            } else {
                viewModel.pauseTimer()
                if (viewModel.getAdWatched()!!) {
                    navigateToFail()
                } else {
                    viewModel.updateAdWatch()
                    showAlertDialog()
                }
            }
        }

    }

    private fun showAlertDialog() {
        AlertDialog.Builder(requireActivity())
            .setTitle("Continue...")
            .setMessage("Do You Want to Watch Ad to Continue?")
            .setPositiveButton("YES") { dialog, _ ->
                Timber.d("CONFIRM")
                binding.progressWordLoad.visibility = View.VISIBLE
                rewardedAd.loadAd(adRequest, object : RewardedAdLoadCallback() {
                    override fun onRewardedAdLoaded() {
                        super.onRewardedAdLoaded()
                        binding.progressWordLoad.visibility = View.INVISIBLE
                        if (rewardedAd.isLoaded) {
                            rewardedAd.show(
                                requireActivity(),
                                object : RewardedAdCallback() {
                                    var rewardEarned = false
                                    override fun onRewardedAdFailedToShow(p0: AdError?) {
                                        super.onRewardedAdFailedToShow(p0)
                                        Snackbar.make(
                                            requireView(),
                                            "Sorry, Ad Failed to Show ${p0?.message}",
                                            Snackbar.LENGTH_SHORT
                                        ).show()
                                        navigateToFail()
                                    }

                                    override fun onRewardedAdClosed() {
                                        super.onRewardedAdClosed()
                                        if (!rewardEarned) {
                                            Snackbar.make(
                                                requireView(),
                                                "Sorry, You Closed Ad.",
                                                Snackbar.LENGTH_SHORT
                                            ).show()
                                            navigateToFail()
                                        } else {
                                            viewModel.updateScore(1)
                                            clearOptionsBackground()
                                            isActiveOptions(true)
                                            viewModel.addTimeAndStartTimer()
                                        }
                                    }

                                    override fun onUserEarnedReward(p0: RewardItem) {
                                        rewardEarned = true
                                        clearOptionsBackground()
                                        isActiveOptions(true)
                                        viewModel.addTimeAndStartTimer()
                                    }
                                })
                        } else {
                            Snackbar.make(
                                requireView(),
                                "Failed to Load Ad",
                                Snackbar.LENGTH_SHORT
                            ).show()
                            navigateToFail()
                        }
                    }

                    override fun onRewardedAdFailedToLoad(adError: LoadAdError) {
                        binding.progressWordLoad.visibility = View.INVISIBLE
                        Snackbar.make(
                            requireView(),
                            "Failed to Load Ad",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        navigateToFail()
                        Timber.d("Message: ${adError.message}\n Cause: ${adError.cause} \n Domain: ${adError.domain}")
                    }
                })
                dialog.dismiss()
                dialog.cancel()
            }
            .setNegativeButton("NO") { dialog, _ ->
                dialog.dismiss()
                dialog.cancel()
                navigateToFail()
            }
            .setCancelable(false)
            .create().show()
    }

    private fun setOptionBackground(v: View, isCorrect: Boolean) {
        v.background = if (isCorrect) {
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.round_stroke_green_background,
                requireContext().theme
            )
        } else {
            val correct = viewModel.current.value!!.correct
            options.forEach { option ->
                if (option.text.toString() == correct)
                    option.background = ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.round_stroke_green_background,
                        requireContext().theme
                    )
            }
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.round_stroke_red_background,
                requireContext().theme
            )
        }
    }

    private fun isActiveOptions(isActive: Boolean) {
        options.forEach { option ->
            option.setOnClickListener(if (isActive) this else null)
        }
    }

    private fun startAnimation() {
        with(binding.motionLayout) {
            setTransition(R.id.start, R.id.end)
            transitionToEnd()
        }
    }

    private fun navigateToFail() {
        val direction =
            GameFragmentDirections.actionGameFragmentToLevelScoreFragment(
                LevelResult.FAIL
            )
        with(findNavController()) {
            if (currentDestination != graph[R.id.scoreFragment] || currentDestination != graph[R.id.levelScoreFragment]) {
                navigate(direction)
            }
        }
    }
}