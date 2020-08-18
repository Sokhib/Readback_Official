package com.sokhibdzhon.readback.ui.start

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdRequest
import com.sokhibdzhon.readback.BaseApplication
import com.sokhibdzhon.readback.R
import com.sokhibdzhon.readback.databinding.StartFragmentBinding
import com.sokhibdzhon.readback.util.enums.GameType
import com.sokhibdzhon.readback.util.enums.NavigationType
import com.sokhibdzhon.readback.util.enums.navigate
import javax.inject.Inject

//TODO: onSkips click Snackbar how many skips left.
class StartFragment : Fragment() {

    private lateinit var binding: StartFragmentBinding
    private lateinit var viewModel: StartViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

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
        binding = DataBindingUtil.inflate(inflater, R.layout.start_fragment, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory).get(StartViewModel::class.java)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            startviewmodel = viewModel
        }
        //ad
        binding.adViewStart.loadAd(adRequest)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.textviewStartGame.setOnClickListener {
            val direction = StartFragmentDirections.actionStartFragmentToGameFragment(
                level = viewModel.getLevel() ?: 1,
                type = GameType.LEVELSGAME
            )
            this.findNavController().navigate(direction)

        }
        binding.imageviewSettings.setOnClickListener {
            navigate(NavigationType.STARTSETTINGS)
        }

    }

}