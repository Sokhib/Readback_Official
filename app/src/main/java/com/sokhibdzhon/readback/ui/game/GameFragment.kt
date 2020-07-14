package com.sokhibdzhon.readback.ui.game

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.sokhibdzhon.readback.BaseApplication
import com.sokhibdzhon.readback.R
import com.sokhibdzhon.readback.databinding.GameFragmentBinding
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


class GameFragment : Fragment(), View.OnClickListener {

    private lateinit var viewModel: GameViewModel
    private lateinit var binding: GameFragmentBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().applicationContext as BaseApplication).appGraph.inject(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory).get(GameViewModel::class.java)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
        }
        binding.gmviewmodel = viewModel
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.apply {
            textOption1.setOnClickListener(this@GameFragment)
            textOption2.setOnClickListener(this@GameFragment)
            textOption3.setOnClickListener(this@GameFragment)
            textOption4.setOnClickListener(this@GameFragment)
        }
        //set progressMax from viewModel or db
        binding.circularTimeView.apply {
            progressMax = 30f
        }
        binding.imageviewClose.setOnClickListener {
            findNavController().popBackStack()
        }
        //skip
        binding.skip.setOnClickListener {
            viewModel.nextWord()
            startAnimation()
        }

        viewModel.timeLeft.observe(viewLifecycleOwner, Observer { timeLeft ->
            binding.circularTimeView.setProgressWithAnimation(timeLeft.toFloat(), 1000)
        })

        //words
        viewModel.wordList.observe(viewLifecycleOwner, Observer { words ->
            words?.let {
                binding.progressWordLoad.visibility = View.GONE
                viewModel.nextWord()
                startAnimation()
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

    private fun gameFinished() {
        runBlocking {
            delay(500)
        }
        val action =
            GameFragmentDirections.actionGameFragmentToScoreFragment(viewModel.score.value ?: 0)
        findNavController().navigate(action)
    }

    override fun onClick(v: View?) {
        viewModel.checkForCorrectness((v as TextView).text.toString())
        isActiveOptions(false)
        viewModel.isCorrect()?.let { isCorrect ->
            v.background = if (isCorrect) {
                resources.getDrawable(
                    R.drawable.round_stroke_green_background,
                    requireContext().theme
                )
            } else {
                resources.getDrawable(
                    R.drawable.round_stroke_red_background,
                    requireContext().theme
                )
            }

        }
        lifecycleScope.launch {
            coroutineScope {
                delay(500)
                v.background =
                    resources.getDrawable(R.drawable.round_white_background, requireContext().theme)
                viewModel.nextWord()
                startAnimation()
                isActiveOptions(true)
            }
        }
    }

    private fun isActiveOptions(isActive: Boolean) {
        if (isActive) {
            binding.apply {
                textOption1.setOnClickListener(this@GameFragment)
                textOption2.setOnClickListener(this@GameFragment)
                textOption3.setOnClickListener(this@GameFragment)
                textOption4.setOnClickListener(this@GameFragment)
            }
        } else {
            binding.apply {
                textOption1.setOnClickListener(null)
                textOption2.setOnClickListener(null)
                textOption3.setOnClickListener(null)
                textOption4.setOnClickListener(null)
            }
        }

    }

    private fun startAnimation() {
        with(binding.motionLayout) {
            setTransition(R.id.start, R.id.end)
            transitionToEnd()
        }
    }
}