package com.sokhibdzhon.readback.di

import android.content.Context
import com.sokhibdzhon.readback.di.viewmodel.ViewModelModule
import com.sokhibdzhon.readback.ui.game.GameFragment
import com.sokhibdzhon.readback.ui.score.ScoreFragment
import com.sokhibdzhon.readback.ui.settings.SettingsFragment
import com.sokhibdzhon.readback.ui.start.StartFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


/**     I ❤ Code:)
╔═══════════════════════════════════════╗
║  Created by Sokhibdzhon Saidmuratov   ║
╠═══════════════════════════════════════╣
║ sokhibsaid@gmail.com                  ║
╚═══════════════════════════════════════╝
 */

@Singleton
@Component(modules = [ViewModelModule::class, DataModule::class, AppModule::class])
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(gameFragment: GameFragment)
    fun inject(scoreFragment: ScoreFragment)
    fun inject(startFragment: StartFragment)
    fun inject(settingsFragment: SettingsFragment)

}