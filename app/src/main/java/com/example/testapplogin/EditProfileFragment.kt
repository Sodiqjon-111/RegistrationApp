package com.example.testapplogin

import android.Manifest
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.testapplogin.common.Avatar
import com.example.testapplogin.common.Constants
import com.example.testapplogin.common.Profile
import com.example.testapplogin.common.RefreshToken
import com.example.testapplogin.common.TokenResponse
import com.example.testapplogin.common.UiEvent
import com.example.testapplogin.common.UpdateProfile
import com.example.testapplogin.common.extensions.encodeImageToBase64
import com.example.testapplogin.databinding.FragmentEditProfileBinding
import com.example.testapplogin.viewModels.RefreshTokenViewModel
import com.example.testapplogin.viewModels.UpdateProfileViewModel
import com.orhanobut.hawk.Hawk
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.io.ByteArrayOutputStream
import java.io.File

@AndroidEntryPoint
class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    val viewModel: UpdateProfileViewModel by viewModels()
    val viewModelToken: RefreshTokenViewModel by viewModels()
    var profile: Profile? = null
    private var selectedImage: Uri? = null
    private var selectedImageName: String? = null
    private var senderImage: String? = null
    private var avatar: Avatar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profile = arguments?.getParcelable("profile")
        binding.let {
            it.profileToolbar.toolbarTitle.text = getString(R.string.editProfile)
            it.profileToolbar.toolbarBackButton.setOnClickListener {
                findNavController().navigateUp()
            }
            it.changePhoto.setOnClickListener {
                if (PermissionUtils.hasPermissions(
                        requireContext(), permissions = arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                    )
                ) {
                    val intent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    imagePickerLauncher.launch(intent)
                } else {
                    regFileLaunch.launch(
                        arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                    )
                }
//
            }
            it.textName.setText(profile?.profile_data?.name)
            it.textCity.setText(profile?.profile_data?.city)
            it.textInstagram.setText(profile?.profile_data?.instagram)
            it.textStatus.setText(profile?.profile_data?.status)
            it.textPhoneNumber.setText(profile?.profile_data?.phone)
            it.textUserName.setText(profile?.profile_data?.username)
            it.updateBtn.setOnClickListener {
                viewModel.updateProfile(
                    updateProfile = UpdateProfile(
                        binding.textName.text.toString(),
                        binding.textUserName.text.toString(),
                        binding.textCity.text.toString(),
                        binding.textInstagram.text.toString(),
                        binding.textStatus.text.toString(),
                        avatar
                    )
                )
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.profile.collectLatest { event ->
                when (event) {
                    is UiEvent.Loading -> {
                        binding.progressBar.isVisible = true
                    }

                    is UiEvent.Empty -> Unit
                    is UiEvent.Success<*> -> {
                        binding.progressBar.isVisible = false
                        findNavController().navigate(
                            R.id.action_editProfileFragment_to_profileFragment,
                        )
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
                        viewModel.updateProfile(
                            updateProfile = UpdateProfile(
                                binding.textName.text.toString(),
                                binding.textUserName.text.toString(),
                                binding.textCity.text.toString(),
                                binding.textInstagram.text.toString(),
                                binding.textStatus.text.toString(),
                                avatar
                            )
                        )
                    }

                    is UiEvent.Error -> {
                        binding.progressBar.isVisible = false
                        binding.errorTv.isVisible = true

                    }
                }
            }
        }
    }

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    selectedImage = data.data
                    val imagePath = selectedImage?.let { uri ->
                        getRealPathFromURI(uri)
                    }
                    selectedImageName = imagePath
                    Log.d(TAG, "666666666    $imagePath ")
                    binding.imageProfile.setImageURI(selectedImage)
                    // senderImage = imagePath?.let { encodeImageToBase64(it) }
                    senderImage = imagePath?.encodeImageToBase64()
                    avatar = Avatar(imagePath, senderImage)
                }
            }
        }

    private fun getRealPathFromURI(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = requireActivity().contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            if (it.moveToFirst()) {
                return it.getString(columnIndex)
            }
        }
        return null
    }

    private val regFileLaunch =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { granted ->

            if (granted.values.all { it }) {
                // getFileFromStorage()
            } else {
//                showToast("We need File permission else you can't get a file")
            }
        }

}