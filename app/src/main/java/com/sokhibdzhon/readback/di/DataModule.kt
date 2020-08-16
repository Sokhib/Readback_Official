package com.sokhibdzhon.readback.di

import android.content.SharedPreferences
import com.google.firebase.firestore.FirebaseFirestore
import com.sokhibdzhon.readback.data.network.custom.CustomGameDataSource
import com.sokhibdzhon.readback.data.network.custom.CustomGameDataSourceImpl
import com.sokhibdzhon.readback.data.repository.GameRepo
import com.sokhibdzhon.readback.data.repository.GameRepoImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**     I ❤ Code:)
╔═══════════════════════════════════════╗
║  Created by Sokhibdzhon Saidmuratov   ║
╠═══════════════════════════════════════╣
║ sokhibsaid@gmail.com                  ║
╚═══════════════════════════════════════╝
 */

@Module(includes = [FirebaseModule::class])
class DataModule {
    @Provides
    @Singleton
    fun provideGameRepo(
        customGameDataSource: CustomGameDataSource,
        sharedPreferences: SharedPreferences
    ): GameRepo =
        GameRepoImpl(customGameDataSource, sharedPreferences)

    @Provides
    @Singleton
    fun provideCustomGameDataSource(firebaseFirestore: FirebaseFirestore): CustomGameDataSource =
        CustomGameDataSourceImpl(firebaseFirestore)


}