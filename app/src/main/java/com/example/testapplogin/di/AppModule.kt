package com.example.testapplogin.di

import android.content.Context
import com.example.testapplogin.api.ApiInterface
import com.example.testapplogin.common.Constants.BASE_URL
import com.example.testapplogin.data.SharedPref
import com.example.testapplogin.domain.CheckAuthRepo
import com.example.testapplogin.domain.CheckAuthRepoImp
import com.example.testapplogin.domain.PhoneNumberRepo
import com.example.testapplogin.domain.PhoneNumberRepoImp
import com.example.testapplogin.domain.ProfileRepo
import com.example.testapplogin.domain.ProfileRepoImp
import com.example.testapplogin.domain.RefreshTokenRepo
import com.example.testapplogin.domain.RefreshTokenRepoImp
import com.example.testapplogin.domain.RegistrationRepo
import com.example.testapplogin.domain.RegistrationRepoImp
import com.example.testapplogin.domain.UpdateProfileRepo
import com.example.testapplogin.domain.UpdateProfileRepoImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun getOkHttp(@ApplicationContext context: Context): OkHttpClient = try {
        val builder = OkHttpClient.Builder().apply {
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }

        builder.build()
    } catch (e: Exception) {
        throw RuntimeException(e)
    }


    @Singleton
    @Provides
    fun getServiceApi(client: OkHttpClient): ApiInterface {
        return Retrofit.Builder().baseUrl(BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ApiInterface::class.java)
    }

    @Singleton
    @Provides
    fun getSharedPref(@ApplicationContext context: Context) = SharedPref(context)

    @Singleton
    @Provides
    fun sendPhoneNumber(
        serviceapi: ApiInterface
    ): PhoneNumberRepo = PhoneNumberRepoImp(serviceapi)


    @Singleton
    @Provides
    fun checkAuth(
        serviceapi: ApiInterface
    ): CheckAuthRepo = CheckAuthRepoImp(serviceapi)

    @Singleton
    @Provides
    fun getProfile(
        serviceapi: ApiInterface
    ): ProfileRepo = ProfileRepoImp(serviceapi)

    @Singleton
    @Provides
    fun registration(
        serviceapi: ApiInterface
    ): RegistrationRepo = RegistrationRepoImp(serviceapi)

    @Singleton
    @Provides
    fun updateProfile(
        serviceapi: ApiInterface
    ): UpdateProfileRepo = UpdateProfileRepoImp(serviceapi)

    @Singleton
    @Provides
    fun refreshToken(
        serviceapi: ApiInterface
    ): RefreshTokenRepo = RefreshTokenRepoImp(serviceapi)
}