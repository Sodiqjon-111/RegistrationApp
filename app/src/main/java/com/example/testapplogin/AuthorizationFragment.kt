package com.example.testapplogin

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
import com.example.testapplogin.common.Constants
import com.example.testapplogin.common.SendAuth
import com.example.testapplogin.common.UiEvent
import com.example.testapplogin.databinding.FragmentAuthorizationBinding
import com.example.testapplogin.viewModels.AuthorizationViewModel
import com.orhanobut.hawk.Hawk
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AuthorizationFragment : Fragment() {
    private var _binding: FragmentAuthorizationBinding? = null
    private val binding get() = _binding!!
    val viewModel: AuthorizationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAuthorizationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.setOnClickListener {
            val text = binding.phoneEditText.text.toString().replace(" ", "")
            if (text.length == Constants.PhoneNumberLength) {
                Hawk.put("phoneNumber", text)
                viewModel.sendPhone(sendAuth = SendAuth(text))
            } else {
                Toast.makeText(requireContext(), "Enter Valid Number", Toast.LENGTH_SHORT).show()
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.phone.collectLatest { event ->
                when (event) {
                    is UiEvent.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    is UiEvent.Empty -> Unit
                    is UiEvent.Success<*> -> {
                        findNavController().navigate(R.id.action_authorizationFragment_to_confirmCodeFragment)
                        binding.progressBar.isVisible = false
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