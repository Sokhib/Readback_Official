package com.sokhibdzhon.readback.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sokhibdzhon.readback.BaseApplication
import com.sokhibdzhon.readback.R
import com.sokhibdzhon.readback.data.model.Category
import com.sokhibdzhon.readback.databinding.FragmentSettingsBinding
import com.xw.repo.BubbleSeekBar
import javax.inject.Inject


class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var viewModel: SettingsViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var categoryAdapter: CategoryAdapter

    private lateinit var categories: List<Category>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().applicationContext as BaseApplication).appGraph.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categories = listOf(
            Category("Custom", R.drawable.ic_custom, true),
            Category("Sport", R.drawable.ic_sport),
            Category("Travel", R.drawable.ic_travel),
            Category("Fruit", R.drawable.ic_fruit)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SettingsViewModel::class.java)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            settingsViewModel = viewModel
        }



        return binding.root
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
        categoryAdapter.setCategoryList(categories)
        categoryAdapter.onCategoryItemClicked = { position, categoryName ->
            categoryAdapter.setCheckedState(position)
            viewModel.setCategoryType(categoryName)
        }


        binding.textviewStartCustomGame.setOnClickListener {
            viewModel.updateCustomSkips()
            viewModel.updateCustomTime()

            val direction =
                SettingsFragmentDirections.actionSettingsFragmentToGameFragment(type = viewModel.category.value!!)
            this.findNavController().navigate(direction)
        }

        binding.seekbarTime.setProgress(viewModel.getCustomTime().toFloat())
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
                    viewModel.setCustomTime(progress)
                    binding.textviewTime.text = getString(R.string.seconds, progress)
                }
            }

        binding.seekbarSkips.setProgress(viewModel.getCustomSkips().toFloat())
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
                    viewModel.setCustomSkips(progress)
                    binding.textviewSkips.text = getString(R.string.skips, progress)
                }
            }


    }

}