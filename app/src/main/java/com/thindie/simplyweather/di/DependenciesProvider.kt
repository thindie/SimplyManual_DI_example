package com.thindie.simplyweather.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thindie.simplyweather.MainActivity
import com.thindie.simplyweather.data.ApiService
import com.thindie.simplyweather.data.WeatherRepositoryImpl
import com.thindie.simplyweather.domain.WeatherRepository
import com.thindie.simplyweather.presentation.add_place.viewmodel.AddPlaceViewModel
import com.thindie.simplyweather.presentation.all_places.viewmodel.AllPlacesViewModel
import com.thindie.simplyweather.presentation.detail_place.viewmodel.DetailPlaceViewModel
import com.thindie.simplyweather.routing.AppRouter
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

private const val BASE_URL = "https://api.open-meteo.com"

class DependenciesProvider private constructor() {

    companion object {
        fun getInstance(dependenciesHolder: DependenciesHolder) {
            dependenciesHolder.setDependenciesProvider(DependenciesProvider())
        }
    }

    interface DependenciesHolder {
        fun setDependenciesProvider(dependenciesProvider: DependenciesProvider)
        fun getDependenciesProvider(): DependenciesProvider
    }

    private val router = AppRouter()


    @OptIn(ExperimentalSerializationApi::class)
    private val json: Json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        explicitNulls = false
    }

    private val retrofit = Retrofit
        .Builder()
        .addConverterFactory(
            json
                .asConverterFactory(
                    "application/json; charset=UTF8"
                        .toMediaType()
                )
        )
        .baseUrl(BASE_URL)
        .build()

    private val apiService: ApiService = retrofit.create(ApiService::class.java)

    private val repository: WeatherRepository = WeatherRepositoryImpl(apiService)

    val allPlacesViewModelFactory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
        ): T {
            return AllPlacesViewModel(
                repository = repository,
                routeFlow = router.routeFlow
            ) as T
        }
    }

    val addPlaceViewModelFactory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
        ): T {
            return AddPlaceViewModel(
                repository = repository,
                routeFlow = router.routeFlow
            ) as T
        }
    }

    private lateinit var longitude: String
    private lateinit var latitude: String

    private val detailPlaceViewModelFactory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
        ): T {
            return DetailPlaceViewModel(
                repository = repository,
                routeFlow = router.routeFlow,
                latitude = latitude,
                longitude = longitude
            ) as T
        }
    }

    fun getDetailScreenViewModelFactory(latitude: String, longitude: String) : ViewModelProvider.Factory {
        this.latitude = latitude
        this.longitude = longitude
        return detailPlaceViewModelFactory
    }

    fun inject(activity: MainActivity){
        activity.appRouter = router
    }
}