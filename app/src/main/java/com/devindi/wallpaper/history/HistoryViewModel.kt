package com.devindi.wallpaper.history

import androidx.lifecycle.ViewModel
import com.devindi.wallpaper.model.history.HistoryManager

class HistoryViewModel(
    historyManager: HistoryManager
) : ViewModel() {

    val historyLiveData = historyManager.getHistory()
}