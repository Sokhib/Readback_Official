package com.sokhibdzhon.readback.ui.game

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sokhibdzhon.readback.BaseApplication
import com.sokhibdzhon.readback.R
import com.sokhibdzhon.readback.databinding.GameFragmentBinding
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
            progressMax = 10f
        }
        binding.imageviewClose.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.timeLeft.observe(viewLifecycleOwner, Observer { timeLeft ->
            binding.circularTimeView.setProgressWithAnimation(timeLeft.toFloat(), 1000)
        })

        //words
        viewModel.wordList.observe(viewLifecycleOwner, Observer { words ->
            if (words != null) {
                binding.progressGamestart.visibility = View.INVISIBLE
                viewModel.nextWord()
            }
        })

        //Game finish
        viewModel.gameFinish.observe(viewLifecycleOwner, Observer { hasFinished ->
            if (hasFinished) gameFinished()
        })
    }

    private fun gameFinished() {
        Toast.makeText(requireActivity(), "Game Finished", Toast.LENGTH_SHORT).show()
        val action =
            GameFragmentDirections.actionGameFragmentToScoreFragment(viewModel.score.value ?: 0)
        findNavController().navigate(action)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            binding.textOption1.id -> {
                viewModel.checkForCorrectness(binding.textOption1.text.toString())

            }
            binding.textOption2.id -> {
                viewModel.checkForCorrectness(binding.textOption2.text.toString())
            }
            binding.textOption3.id -> {
                viewModel.checkForCorrectness(binding.textOption3.text.toString())
            }
            binding.textOption4.id -> {
                viewModel.checkForCorrectness(binding.textOption4.text.toString())
            }
        }
    }

}