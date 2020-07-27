package com.sokhibdzhon.readback.ui.start

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdRequest
import com.sokhibdzhon.readback.BaseApplication
import com.sokhibdzhon.readback.R
import com.sokhibdzhon.readback.databinding.StartFragmentBinding
import com.sokhibdzhon.readback.util.enum.NavigationType
import com.sokhibdzhon.readback.util.enum.navigate
import javax.inject.Inject

class StartFragment : Fragment() {

    private lateinit var binding: StartFragmentBinding
    //private lateinit var viewModel: StartViewModel

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
        binding.lifecycleOwner = viewLifecycleOwner
        //ad
        binding.adViewStart.loadAd(adRequest)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.textviewStartGame.setOnClickListener {
            navigate(NavigationType.STARTGAME)
        }
        binding.imageviewSettings.setOnClickListener {
            navigate(NavigationType.STARTSETTINGS)
        }

    }

}