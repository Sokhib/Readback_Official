package com.sokhibdzhon.readback.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.rewarded.RewardedAd
import com.sokhibdzhon.readback.ui.settings.CategoryAdapter
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

    @Provides
    fun provideRewardedAd(context: Context): RewardedAd =
        RewardedAd(context, "ca-app-pub-3940256099942544/5224354917")

    @Provides
    fun provideCategoryAdapter() = CategoryAdapter()
}