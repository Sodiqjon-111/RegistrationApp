package com.example.testapplogin.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapplogin.common.RefreshToken
import com.example.testapplogin.common.Registration
import com.example.testapplogin.common.Resource
import com.example.testapplogin.common.UiEvent
import com.example.testapplogin.domain.RefreshTokenRepo
import com.example.testapplogin.domain.RegistrationRepo
import com.orhanobut.hawk.Hawk
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RefreshTokenViewModel @Inject constructor(private val repository: RefreshTokenRepo) :
    ViewModel() {
    private val _accessToken = MutableStateFlow<UiEvent>(UiEvent.Empty)
    val accessToken get() = _accessToken

    fun refreshToken(refreshToken: RefreshToken) {
        viewModelScope.launch {
            _accessToken.value = UiEvent.Loading

            when (val resource = repository.refreshToken(refreshToken)) {
                is Resource.Success -> {
                    _accessToken.value = UiEvent.Success(resource.data?.body())
                }
                is Resource.Error -> {
                    _accessToken.value = UiEvent.Error(resource.message)
                }
            }
        }
    }
}