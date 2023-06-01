package com.example.testapplogin.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.testapplogin.R
import com.example.testapplogin.common.Constants
import com.example.testapplogin.common.Profile
import com.example.testapplogin.common.RefreshToken
import com.example.testapplogin.common.TokenResponse
import com.example.testapplogin.common.UiEvent
import com.example.testapplogin.databinding.FragmentProfileBinding
import com.example.testapplogin.viewModels.ProfileViewModel
import com.example.testapplogin.viewModels.RefreshTokenViewModel
import com.orhanobut.hawk.Hawk
import com.squareup.picasso.Callback
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import java.lang.Exception

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    val viewModel: ProfileViewModel by viewModels()
    val viewModelToken: RefreshTokenViewModel by viewModels()
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
                        if (data?.profile_data?.avatar.isNullOrBlank()) {
                            binding.imageProfile.setImageResource(R.drawable.person)
                        } else {
                            val imageFileName = data?.profile_data?.avatar
                            val imageUrl = "https://plannerok.ru/path/to/images/$imageFileName"
                            val accessToken = Hawk.get<String>("accessToken")
                            val picassoBuilder = Picasso.Builder(requireContext())
                            picassoBuilder.downloader(
                                OkHttp3Downloader(
                                    createAuthenticatedOkHttpClient(accessToken)
                                )
                            )
                            val picasso = picassoBuilder.build()
                            picasso.load(imageUrl)
                                .error(R.drawable.person)
                                .into(binding.imageProfile, object : Callback {
                                    override fun onSuccess() {
                                        Log.d(TAG, "111111:    success")
                                    }
                                    override fun onError(e: Exception?) {
                                    }

                                })
                        }
                    }

                    is UiEvent.Error -> {
                        val code = Hawk.get<Int>("ExpiredAccessToken")
                        if (code == Constants.ExpiredTokenCode) {
                            viewModelToken.refreshToken(RefreshToken(Hawk.get("refreshToken")))
                        } else {
                            binding.progressBar.isVisible = false
                            binding.errorTv.isVisible = true
                        }

                    }
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModelToken.accessToken.collectLatest { event ->
                when (event) {
                    is UiEvent.Loading -> {
                        binding.progressBar.isVisible = true
                    }

                    is UiEvent.Empty -> Unit
                    is UiEvent.Success<*> -> {
                        val data = event.data as? TokenResponse
                        binding.progressBar.isVisible = false
                        Hawk.put("accessToken", data?.access_token)
                        Hawk.put("refreshToken", data?.refresh_token)
                        Hawk.put("ExpiredAccessToken", 200)
                        viewModel.getProfile()
                    }

                    is UiEvent.Error -> {
                        binding.progressBar.isVisible = false
                        binding.errorTv.isVisible = true
                    }
                }
            }
        }
    }

    private fun createAuthenticatedOkHttpClient(accessToken: String): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
        clientBuilder.addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val originalRequest = chain.request()
                val authenticatedRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $accessToken")
                    .build()
                return chain.proceed(authenticatedRequest)
            }
        })
        return clientBuilder.build()
    }

}