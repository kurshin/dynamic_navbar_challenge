package com.cinemo.test.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.cinemo.test.data.MediaDataRepository
import com.cinemo.test.domain.MediaData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.cinemo.data_parser.Result

class MainViewModel(private val repository: MediaDataRepository) : ViewModel() {

    private val _mediaData = MutableLiveData<Result<MediaData>>()
    val mediaData: LiveData<Result<MediaData>> get() = _mediaData

    fun loadMediaData(fileName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getMediaData(fileName)
            _mediaData.postValue(result)
        }
    }
}

class MainViewModelFactory(
    private val repository: MediaDataRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}