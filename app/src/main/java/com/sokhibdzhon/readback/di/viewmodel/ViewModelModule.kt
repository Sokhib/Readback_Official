package com.sokhibdzhon.readback.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sokhibdzhon.readback.ui.game.GameViewModel
import com.sokhibdzhon.readback.ui.levelscore.LevelScoreViewModel
import com.sokhibdzhon.readback.ui.score.ScoreViewModel
import com.sokhibdzhon.readback.ui.start.StartViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


/**     I ❤ Code:)
╔═══════════════════════════════════════╗
║  Created by Sokhibdzhon Saidmuratov   ║
╠═══════════════════════════════════════╣
║ sokhibsaid@gmail.com                  ║
╚═══════════════════════════════════════╝
 */

@Module
abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelMapKey(GameViewModel::class)
    internal abstract fun bindGameViewModel(viewModel: GameViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelMapKey(ScoreViewModel::class)
    internal abstract fun bindScoreViewModel(viewModel: ScoreViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelMapKey(StartViewModel::class)
    internal abstract fun bindStartViewModel(viewModel: StartViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelMapKey(LevelScoreViewModel::class)
    internal abstract fun bindGameScoreViewModel(viewModel: LevelScoreViewModel): ViewModel
}