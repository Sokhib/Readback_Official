package com.sokhibdzhon.readback.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.google.android.gms.ads.AdRequest
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

@Module
class AppModule {
    @Provides
    @Singleton
    fun provideSharePref(context: Context): SharedPreferences {
        return context.getSharedPreferences(
            "com.sokhibdzhon.readback.sharedpreferences",
            MODE_PRIVATE
        )
    }

    @Singleton
    @Provides
    fun provideAdRequest(): AdRequest = AdRequest.Builder().build()
}