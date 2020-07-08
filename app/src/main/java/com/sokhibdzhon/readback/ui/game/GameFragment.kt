package com.sokhibdzhon.readback.ui.game

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sokhibdzhon.readback.BaseApplication
import com.sokhibdzhon.readback.R
import com.sokhibdzhon.readback.databinding.GameFragmentBinding
import javax.inject.Inject

class GameFragment : Fragment() {

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
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(GameViewModel::class.java)
    }

}