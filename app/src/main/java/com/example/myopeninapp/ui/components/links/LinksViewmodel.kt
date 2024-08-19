package com.example.myopeninapp.ui.components.links

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myopeninapp.data.model.data.LinksData
import com.example.myopeninapp.repository.LinksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import utils.Resource

class LinksViewmodel(private val repository: LinksRepository) : ViewModel() {
    private val _linksData = MutableStateFlow<Resource<LinksData>>(Resource.Loading())
    val dashboardData: StateFlow<Resource<LinksData>> = _linksData

    init {
        fetchLinksData()
    }

    private fun fetchLinksData() {
        viewModelScope.launch {
            _linksData.value = Resource.Loading()
            val result = repository.getLinksData()
            if (result.isSuccessful) {
                _linksData.value = Resource.Success(result.body()!!)
            } else {
                _linksData.value = Resource.Error(result.message())
            }
        }
    }
}


class LinksViewModelFactory(
    private val repository: LinksRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LinksViewmodel::class.java)) {
            return LinksViewmodel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}