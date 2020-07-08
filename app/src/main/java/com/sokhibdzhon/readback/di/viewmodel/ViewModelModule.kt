package com.sokhibdzhon.readback.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sokhibdzhon.readback.ui.game.GameViewModel
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

}