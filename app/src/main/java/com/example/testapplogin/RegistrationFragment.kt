package com.example.testapplogin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.testapplogin.common.CheckAuthResponse
import com.example.testapplogin.common.Registration
import com.example.testapplogin.common.RegistrationResponse
import com.example.testapplogin.common.UiEvent
import com.example.testapplogin.databinding.FragmentRegistrationBinding
import com.example.testapplogin.viewModels.RefreshTokenViewModel

import com.example.testapplogin.viewModels.RegistrationViewModel
import com.orhanobut.hawk.Hawk
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class RegistrationFragment : Fragment() {
    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!
    val viewModel: RegistrationViewModel by viewModels()
    val viewModelToken: RefreshTokenViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registartionToolbar.toolbarTitle.text = getString(R.string.registartion)


        binding.btnNext.setOnClickListener {
            viewModel.registration(
                registration = Registration(
                    binding.phoneNumber.text.toString(),
                    binding.name.text.toString(),
                    binding.userName.text.toString()
                )
            )
        }
        lifecycleScope.launchWhenCreated {
            viewModel.registration.collectLatest { event ->
                when (event) {
                    is UiEvent.Loading -> {
                        binding.progressBar.isVisible = true
                    }

                    is UiEvent.Empty -> Unit
                    is UiEvent.Success<*> -> {
                        binding.progressBar.isVisible = false
                        val data = event.data as? RegistrationResponse
                        Hawk.put("accessToken", data?.access_token)
                        if (data != null) {
                            findNavController().navigate(R.id.action_registrationFragment_to_profileFragment)
                        }
                    }

                    is UiEvent.Error -> {
                        binding.progressBar.isVisible = false
                        binding.errorTv.isVisible = true

                    }
                }

            }
        }


    }
}