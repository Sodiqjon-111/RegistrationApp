package com.example.testapplogin.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapplogin.domain.PhoneNumberRepo
import com.example.testapplogin.common.Resource
import com.example.testapplogin.common.SendAuth
import com.example.testapplogin.common.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(private val repository: PhoneNumberRepo) :
    ViewModel() {
    private val _phone = MutableStateFlow<UiEvent>(UiEvent.Empty)
    val phone get() = _phone

    fun sendPhone(sendAuth: SendAuth) {
        viewModelScope.launch {
            _phone.value = UiEvent.Loading

            when (val resource = repository.sendPhoneNumber(sendAuth)) {
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