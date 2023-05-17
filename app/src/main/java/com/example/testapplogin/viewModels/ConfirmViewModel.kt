package com.example.testapplogin.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapplogin.common.CheckAuth
import com.example.testapplogin.common.Resource
import com.example.testapplogin.common.UiEvent
import com.example.testapplogin.domain.CheckAuthRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmViewModel @Inject constructor(private val repository: CheckAuthRepo) : ViewModel() {
    private val _phone = MutableStateFlow<UiEvent>(UiEvent.Empty)
    val code get() = _phone

    fun sendPhoneCode(checkAuth: CheckAuth) {
        viewModelScope.launch {
            _phone.value = UiEvent.Loading

            when (val resource = repository.checkAuth(checkAuth)) {
                is Resource.Success -> {
                    _phone.value = UiEvent.Success(resource.data)
                }

                is Resource.Error -> {
                    _phone.value = UiEvent.Error(resource.message)

                }
            }
        }
    }
}