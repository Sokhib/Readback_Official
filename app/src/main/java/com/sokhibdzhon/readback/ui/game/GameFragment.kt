package com.sokhibdzhon.readback.ui.game

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.get
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

    private var timeSeekbar: Float = 15.0f

    @Inject
    lateinit var sharedPrefEditor: SharedPreferences

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
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        timeSeekbar =
            sharedPrefEditor.getInt(getString(R.string.sharedpref_seconds), 15).toFloat()
        //set progressMax from viewModel or db
        binding.circularTimeView.apply {
            progressMax = timeSeekbar
        }
        binding.imageviewClose.setOnClickListener {
            findNavController().popBackStack()
        }
        //timeleft
        viewModel.timeLeft.observe(viewLifecycleOwner, Observer { timeLeft ->
            binding.circularTimeView.setProgressWithAnimation(timeLeft.toFloat(), 1000)
        })
        //checkInternet connection
        viewModel.isConnected.observe(viewLifecycleOwner, Observer { isConnected ->
            if (!isConnected) {
                Toast.makeText(requireActivity(), "Check internet connection", Toast.LENGTH_SHORT)
                    .show()
            }
        })
        //words
        viewModel.wordList.observe(viewLifecycleOwner, Observer { words ->
            words?.let {
                binding.progressWordLoad.visibility = View.GONE
                options.forEach { option ->
                    option.setOnClickListener(this)
                }
                viewModel.nextWord()
                startAnimation()
                binding.skip.setOnClickListener {
                    viewModel.nextWord()
                    viewModel.minusSkip()
                    startAnimation()
                }
            }
        })
        //skipNumber
        viewModel.skipNumber.observe(viewLifecycleOwner, Observer { numberOfSkips ->

            if (numberOfSkips == 0) {
                Toast.makeText(requireActivity(), "No Skips Left", Toast.LENGTH_SHORT)
                    .show()
                binding.skip.setOnClickListener(null)
            }


        })
        //start timer
        viewModel.startTimer.observe(viewLifecycleOwner, Observer {
            viewModel.startTimer()
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
        viewModel.checkForCorrectness((v as TextView).text.toString())
        isActiveOptions(false)
        viewModel.isCorrect()?.let { isCorrect ->
            setOptionBackground(v, isCorrect)
        }
        lifecycleScope.launch {
            coroutineScope {
                delay(500)
                options.forEach { option ->
                    option.background = resources.getDrawable(
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
        runBlocking {
            delay(500)
        }
        val action =
            GameFragmentDirections.actionGameFragmentToScoreFragment(viewModel.score.value ?: 0)
        with(findNavController()) {
            if (currentDestination != graph[R.id.scoreFragment]) {
                navigate(action)
            }
        }
    }

    private fun setOptionBackground(v: View, isCorrect: Boolean) {
        v.background = if (isCorrect) {
            resources.getDrawable(
                R.drawable.round_stroke_green_background,
                requireContext().theme
            )
        } else {
            options.forEach { option ->
                if (option.text.toString() == viewModel.current.value!!.correct)
                    option.background = resources.getDrawable(
                        R.drawable.round_stroke_green_background,
                        requireContext().theme
                    )
            }
            resources.getDrawable(
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