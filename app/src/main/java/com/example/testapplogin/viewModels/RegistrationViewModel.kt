package com.example.testapplogin.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapplogin.common.CheckAuth
import com.example.testapplogin.common.Registration
import com.example.testapplogin.common.Resource
import com.example.testapplogin.common.UiEvent
import com.example.testapplogin.domain.CheckAuthRepo
import com.example.testapplogin.domain.RegistrationRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(private val repository: RegistrationRepo) :
    ViewModel() {
    private val _phone = MutableStateFlow<UiEvent>(UiEvent.Empty)
    val registration get() = _phone

    fun registration(registration: Registration) {
        viewModelScope.launch {
            _phone.value = UiEvent.Loading

            when (val resource = repository.registration(registration)) {
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