package com.sokhibdzhon.readback.di

import android.content.Context
import com.sokhibdzhon.readback.di.viewmodel.ViewModelModule
import com.sokhibdzhon.readback.ui.game.GameFragment
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
@Component(modules = [ViewModelModule::class, FirebaseModule::class])
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(gameFragment: GameFragment)

}