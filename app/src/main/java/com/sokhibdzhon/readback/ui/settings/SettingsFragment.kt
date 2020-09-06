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
import com.google.android.material.snackbar.Snackbar
import com.sokhibdzhon.readback.BaseApplication
import com.sokhibdzhon.readback.R
import com.sokhibdzhon.readback.data.model.Category
import com.sokhibdzhon.readback.databinding.FragmentSettingsBinding
import com.sokhibdzhon.readback.util.Constants
import com.sokhibdzhon.readback.util.enums.GameType
import com.xw.repo.BubbleSeekBar
import javax.inject.Inject


class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    @Inject
    lateinit var sharedPrefEditor: SharedPreferences
    private var timeSeekbar = 15
    private var skipsSeekbar = 0
    private var isClicked = false

    @Inject
    lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        timeSeekbar = sharedPrefEditor.getInt(Constants.CUSTOM_SECONDS, 15)
        skipsSeekbar = sharedPrefEditor.getInt(Constants.CUSTOM_SKIPS, 0)

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
        //recyclerView
        binding.recyclerviewCategory.apply {
            adapter = categoryAdapter
        }
        //Getting data from viewModel or Repo and setting adapter data
        categoryAdapter.setCategoryList(
            listOf(
                Category("Custom", R.drawable.ic_custom, true),
                Category("Sport", R.drawable.ic_sport),
                Category("Travel", R.drawable.ic_travel),
                Category("Fruit", R.drawable.ic_fruit)
            )
        )
        categoryAdapter.onCategoryItemClicked = { position, categoryName ->
            //Give categoryName to viewModel and while starting new game get from there...
            categoryAdapter.setCheckedState(position)
            Snackbar.make(requireView(), "$position $categoryName", Snackbar.LENGTH_SHORT).show()
        }


        binding.textviewStartCustomGame.setOnClickListener {
            sharedPrefEditor.edit().putInt(Constants.CUSTOM_SECONDS, timeSeekbar)
                .apply()
            sharedPrefEditor.edit().putInt(Constants.CUSTOM_SKIPS, skipsSeekbar)
                .apply()

            val direction =
                SettingsFragmentDirections.actionSettingsFragmentToGameFragment(type = GameType.CUSTOMGAME)
            this.findNavController().navigate(direction)
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