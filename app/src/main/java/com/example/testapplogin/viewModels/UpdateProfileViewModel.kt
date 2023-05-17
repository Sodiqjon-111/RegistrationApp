package com.example.testapplogin.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapplogin.common.Resource
import com.example.testapplogin.common.UiEvent
import com.example.testapplogin.common.UpdateProfile
import com.example.testapplogin.domain.UpdateProfileRepo
import com.orhanobut.hawk.Hawk
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateProfileViewModel @Inject constructor(private val repository: UpdateProfileRepo) :
    ViewModel() {

    private val _phone = MutableStateFlow<UiEvent>(UiEvent.Empty)
    val profile get() = _phone

    fun updateProfile(updateProfile: UpdateProfile) {
        viewModelScope.launch {
            _phone.value = UiEvent.Loading

            when (val resource = repository.updateProfile(updateProfile)) {
                is Resource.Success -> {
                    _phone.value = UiEvent.Success(resource.data?.body())
                }

                is Resource.Error -> {
                    if (resource.data?.code() == 401) {
                        Hawk.put("ExpiredAccessToken", 401)
                    } else {
                        _phone.value = UiEvent.Error(resource.message)
                    }


                }
            }
        }
    }


}