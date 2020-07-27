package com.sokhibdzhon.readback.util.enum

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.sokhibdzhon.readback.ui.score.ScoreFragmentDirections
import com.sokhibdzhon.readback.ui.settings.SettingsFragmentDirections
import com.sokhibdzhon.readback.ui.start.StartFragmentDirections


/**     I ❤ Code:)
╔═══════════════════════════════════════╗
║  Created by Sokhibdzhon Saidmuratov   ║
╠═══════════════════════════════════════╣
║ sokhibsaid@gmail.com                  ║
╚═══════════════════════════════════════╝
 */

enum class NavigationType {
    STARTGAME, STARTSETTINGS, SCOREHOME, SCOREGAME, SETTINGSGAME
}

fun Fragment.navigate(navType: NavigationType) {
    val direction: NavDirections = when (navType) {
        NavigationType.STARTGAME -> {
            StartFragmentDirections.actionStartFragmentToGameFragment()
        }
        NavigationType.STARTSETTINGS -> {
            StartFragmentDirections.actionStartFragmentToSettingsFragment()
        }
        NavigationType.SCOREHOME -> {
            ScoreFragmentDirections.actionScoreFragmentToStartFragment()
        }
        NavigationType.SCOREGAME -> {
            ScoreFragmentDirections.actionScoreFragmentToGameFragment()
        }
        NavigationType.SETTINGSGAME -> {
            SettingsFragmentDirections.actionSettingsFragmentToGameFragment()
        }
    }
    this.findNavController().navigate(direction)
}
