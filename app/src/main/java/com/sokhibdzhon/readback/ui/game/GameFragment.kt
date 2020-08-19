package com.sokhibdzhon.readback.ui.game

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.get
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
                            viewModel.nextWord()
                            viewModel.minusSkip()
                            viewModel.updateScore(1)
                            startAnimation()
                        }
                    }
                    viewModel.nextWord()
                    startAnimation()
                }
                Status.ERROR -> {
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
        lifecycleScope.launch {
            coroutineScope {
                delay(500)
                options.forEach { option ->
                    option.background = ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.round_white_background,
                        requireContext().theme
                    )
                }

                viewModel.nextWord()
                startAnimation()
                isActiveOptions(true)
            }
        }
    }

    //Functions
    private fun gameFinished() {
        val action = if (args.type == GameType.CUSTOMGAME)
            GameFragmentDirections.actionGameFragmentToScoreFragment(viewModel.score.value ?: 0)
        else {
            if (viewModel.isCorrect()!!) {
                Timber.d("isCorrect in Main Game Finish: $isCorrect")
                Timber.d("viewModel.isCorrect() in Main Game Finish: ${viewModel.isCorrect()}")
                GameFragmentDirections.actionGameFragmentToLevelScoreFragment(LevelResult.SUCCESS)
            } else GameFragmentDirections.actionGameFragmentToLevelScoreFragment(LevelResult.FAIL)
        }
        with(findNavController()) {
            if (currentDestination != graph[R.id.scoreFragment] || currentDestination != graph[R.id.levelScoreFragment]) {
                navigate(action)
            }
        }
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
}