package com.mohsen.nobka.ui.nobka

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohsen.nobka.core.data.remote.error.Failure
import com.mohsen.nobka.core.utils.onFailure
import com.mohsen.nobka.core.utils.onSuccess
import com.mohsen.nobka.domain.model.NobkaMoviesModel
import com.mohsen.nobka.domain.repository.NobkaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NobkaViewModel @Inject constructor(private val repository: NobkaRepository) : ViewModel() {

    private val _nobkaUiState: MutableStateFlow<NobkaUiState> =
        MutableStateFlow(NobkaUiState.Loading)
    val nobkaUiState = _nobkaUiState.asStateFlow()


    fun fetchDataRequest() {
        viewModelScope.launch {
            repository.getMovies().first().apply {
                onSuccess { _nobkaUiState.tryEmit(NobkaUiState.Success(it)) }
                onFailure { _nobkaUiState.tryEmit(NobkaUiState.Error(it)) }
            }
        }
    }

    sealed interface NobkaUiState {
        data class Success(val result: List<NobkaMoviesModel>) : NobkaUiState
        data class Error(val failure: Failure) : NobkaUiState
        object Loading : NobkaUiState
    }

}