package com.example.myopeninapp.ui.components.links

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myopeninapp.com.example.myopeninapp.data.model.data.DashboardData
import com.example.myopeninapp.repository.DashboardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import utils.Resource

class LinksViewmodel(private val repository: DashboardRepository) : ViewModel() {
    private val _dashboardData = MutableStateFlow<Resource<DashboardData>>(Resource.Loading())
    val dashboardData: StateFlow<Resource<DashboardData>> = _dashboardData

    init {
        fetchDashboardData()
    }

    private fun fetchDashboardData() {
        viewModelScope.launch {
            _dashboardData.value = Resource.Loading()
            val result = repository.getDashboardData()
            if (result.isSuccessful) {
                _dashboardData.value = Resource.Success(result.body()!!)
            } else {
                _dashboardData.value = Resource.Error(result.message())
            }
        }
    }
}


class DashboardViewModelFactory(
    private val repository: DashboardRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LinksViewmodel::class.java)) {
            return LinksViewmodel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
