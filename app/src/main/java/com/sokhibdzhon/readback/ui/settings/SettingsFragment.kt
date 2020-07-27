package com.sokhibdzhon.readback.ui.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sokhibdzhon.readback.BaseApplication
import com.sokhibdzhon.readback.R
import com.sokhibdzhon.readback.databinding.FragmentSettingsBinding
import com.sokhibdzhon.readback.util.enum.NavigationType
import com.sokhibdzhon.readback.util.enum.navigate
import com.xw.repo.BubbleSeekBar
import javax.inject.Inject


class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    @Inject
    lateinit var sharedPrefEditor: SharedPreferences
    private var timeSeekbar = 15
    private var skipsSeekbar = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        timeSeekbar = sharedPrefEditor.getInt(getString(R.string.sharedpref_seconds), 15)
        skipsSeekbar = sharedPrefEditor.getInt(getString(R.string.sharedpref_skips), 0)

        binding.textviewTime.text = getString(R.string.seconds, timeSeekbar)
        binding.textviewSkips.text = getString(R.string.skips, skipsSeekbar)

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().applicationContext as BaseApplication).appGraph.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageviewClose.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.textviewStartCustomGame.setOnClickListener {
            sharedPrefEditor.edit().putInt(getString(R.string.sharedpref_seconds), timeSeekbar)
                .apply()
            sharedPrefEditor.edit().putInt(getString(R.string.sharedpref_skips), skipsSeekbar)
                .apply()
            navigate(NavigationType.SETTINGSGAME)
        }

        binding.seekbarTime.setProgress(timeSeekbar.toFloat())
        binding.seekbarTime.onProgressChangedListener =
            object :
                BubbleSeekBar.OnProgressChangedListenerAdapter() {
                override fun onProgressChanged(
                    bubbleSeekBar: BubbleSeekBar?,
                    progress: Int,
                    progressFloat: Float,
                    fromUser: Boolean
                ) {
                    super.onProgressChanged(bubbleSeekBar, progress, progressFloat, fromUser)
                    timeSeekbar = progress
                    binding.textviewTime.text = getString(R.string.seconds, progress)
                }
            }

        binding.seekbarSkips.setProgress(skipsSeekbar.toFloat())
        binding.seekbarSkips.onProgressChangedListener =
            object :
                BubbleSeekBar.OnProgressChangedListenerAdapter() {
                override fun onProgressChanged(
                    bubbleSeekBar: BubbleSeekBar?,
                    progress: Int,
                    progressFloat: Float,
                    fromUser: Boolean
                ) {
                    super.onProgressChanged(bubbleSeekBar, progress, progressFloat, fromUser)
                    skipsSeekbar = progress
                    binding.textviewSkips.text = getString(R.string.skips, progress)
                }
            }


    }

}