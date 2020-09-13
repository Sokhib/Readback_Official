package com.sokhibdzhon.readback.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sokhibdzhon.readback.data.repository.GameRepoImpl
import com.sokhibdzhon.readback.util.enums.GameType
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


/**     I ❤ Code:)
╔═══════════════════════════════════════╗
║  Created by Sokhibdzhon Saidmuratov   ║
╠═══════════════════════════════════════╣
║ sokhibsaid@gmail.com                  ║
╚═══════════════════════════════════════╝
 */


class SettingsViewModel @Inject constructor(private val gameRepoImpl: GameRepoImpl) :
    ViewModel() {


    private val _customTime = MutableLiveData(0)
    val customTime: LiveData<Int>
        get() = _customTime

    private val _customSkips = MutableLiveData(0)
    val customSkips: LiveData<Int>
        get() = _customSkips

    private val _category = MutableLiveData(GameType.CUSTOMGAME)
    val category: LiveData<GameType>
        get() = _category

    init {
        viewModelScope.launch {
            _customTime.value = gameRepoImpl.customTime
            _customSkips.value = gameRepoImpl.customSkips

            Timber.d("skips: ${customSkips.value}   time:${customTime.value} ")
        }
    }

    fun setCustomTime(time: Int) {
        _customTime.value = time
    }

    fun setCustomSkips(skips: Int) {
        _customSkips.value = skips
    }

    fun getCustomTime() = customTime.value ?: 15
    fun getCustomSkips() = customSkips.value ?: 0

    fun updateCustomTime() {
        gameRepoImpl.updateCustomTime(customTime.value ?: 15)
    }

    fun updateCustomSkips() {
        gameRepoImpl.updateCustomSkips(customSkips.value ?: 0)
    }

    fun setCategoryType(categoryName: String) {
        GameType.CUSTOMGAME.type = categoryName
        _category.value = GameType.CUSTOMGAME
    }

}