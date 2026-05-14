package com.vmpt.zakhar.koriakin.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vmpt.zakhar.koriakin.domain.model.MatchRecord
import com.vmpt.zakhar.koriakin.domain.usecase.ObserveMatchHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    observeMatchHistory: ObserveMatchHistoryUseCase
) : ViewModel() {

    val history: StateFlow<List<MatchRecord>> = observeMatchHistory()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}
