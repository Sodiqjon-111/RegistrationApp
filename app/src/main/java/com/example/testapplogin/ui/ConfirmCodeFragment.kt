package com.example.testapplogin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.testapplogin.R
import com.example.testapplogin.common.CheckAuth
import com.example.testapplogin.common.CheckAuthResponse
import com.example.testapplogin.common.Constants
import com.example.testapplogin.common.UiEvent
import com.example.testapplogin.databinding.FragmentConfirmCodeBinding
import com.example.testapplogin.viewModels.ConfirmViewModel
import com.orhanobut.hawk.Hawk
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ConfirmCodeFragment : Fragment() {
    private var _binding: FragmentConfirmCodeBinding? = null
    private val binding get() = _binding!!
    val viewModel: ConfirmViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConfirmCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val phoneNumber = Hawk.get<String>("phoneNumber")
        binding.btnVerify.setOnClickListener {
            val code = binding.confirmCodeText.getOTPText()
            if (code == Constants.ConfirmCode) {
                viewModel.sendPhoneCode(checkAuth = CheckAuth(phoneNumber, code))
            } else {
                Toast.makeText(
                    requireContext(), "Confirmation code is not valid", Toast.LENGTH_SHORT
                ).show()
            }

        }

        lifecycleScope.launchWhenCreated {
            viewModel.code.collectLatest { event ->
                when (event) {
                    is UiEvent.Loading -> {
                        binding.progressBar.isVisible = true
                    }

                    is UiEvent.Empty -> Unit
                    is UiEvent.Success<*> -> {
                        binding.progressBar.isVisible = false
                        val data = event.data as? CheckAuthResponse
                        Hawk.put("accessToken", data?.access_token)
                        Hawk.put("refreshToken", data?.refresh_token)
                        if (data != null) {
                            if (data.is_user_exists) {
                                findNavController().navigate(R.id.action_confirmCodeFragment_to_profileFragment)
                            } else {
                                findNavController().navigate(R.id.action_confirmCodeFragment_to_registrationFragment)

                            }
                        } else {
                            findNavController().navigate(R.id.action_confirmCodeFragment_to_registrationFragment)

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