package com.example.testapplogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.testapplogin.common.Profile
import com.example.testapplogin.common.UiEvent
import com.example.testapplogin.databinding.FragmentProfileBinding
import com.example.testapplogin.viewModels.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    val viewModel: ProfileViewModel by viewModels()
    var profile: Profile? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnEdit.setOnClickListener {
            findNavController().navigate(
                R.id.action_profileFragment_to_editProfileFragment,
                bundleOf("profile" to profile)
            )
        }

        binding.profileToolbar.toolbarTitle.text = getString(R.string.profile)
        viewModel.getProfile()
        lifecycleScope.launchWhenCreated {
            viewModel.profile.collectLatest { event ->
                when (event) {
                    is UiEvent.Loading -> {
                        binding.progressBar.isVisible = true
                    }

                    is UiEvent.Empty -> Unit
                    is UiEvent.Success<*> -> {
                        binding.progressBar.isVisible = false
                        val data = event.data as? Profile
                        profile = data
                        binding.textName.text = data?.profile_data?.name.toString()
                        binding.textPhoneNumber.text = data?.profile_data?.phone.toString()
                        binding.textUserName.text = data?.profile_data?.username.toString()
                        binding.textCity.text = data?.profile_data?.city.toString()
                        binding.textInstagram.text = data?.profile_data?.instagram.toString()
                        binding.textStatus.text = data?.profile_data?.status.toString()
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