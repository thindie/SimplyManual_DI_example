package com.thindie.simplyweather.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.thindie.simplyweather.MainActivity
import com.thindie.simplyweather.data.ApiService
import com.thindie.simplyweather.data.WeatherRepositoryImpl
import com.thindie.simplyweather.database.WeatherDb
import com.thindie.simplyweather.domain.WeatherRepository
import com.thindie.simplyweather.presentation.add_place.viewmodel.AddPlaceViewModel
import com.thindie.simplyweather.presentation.all_places.viewmodel.AllPlacesViewModel
import com.thindie.simplyweather.presentation.detail_place.viewmodel.DetailPlaceViewModel
import com.thindie.simplyweather.presentation.rename_place.viewmodel.RenamePlaceViewModel
import com.thindie.simplyweather.presentation.stored_places.viewmodel.StoredPlaceViewModel
import com.thindie.simplyweather.routing.AppRouter
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

private const val BASE_URL = "https://api.open-meteo.com"

class DependenciesProvider private constructor(private val context: Context) {

    private lateinit var sqlDriver: SqlDriver
    private lateinit var weatherDb: WeatherDb

    companion object {
        fun getInstance(dependenciesHolder: DependenciesHolder, context: Context) {
            dependenciesHolder.setDependenciesProvider(DependenciesProvider(context))
        }
    }

    interface DependenciesHolder {
        fun setDependenciesProvider(dependenciesProvider: DependenciesProvider)
        fun getDependenciesProvider(): DependenciesProvider
    }

    private lateinit var router: AppRouter

    init {
        AppRouter.inject(this)
    }


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

    private fun getSqlDriver(context: Context): SqlDriver {
        return if (::sqlDriver.isInitialized) {
            sqlDriver
        } else {
            sqlDriver = AndroidSqliteDriver(
                WeatherDb.Schema,
                context,
                name = "WeatherTitleDb"
            )
            sqlDriver
        }
    }

    private fun getWeatherTitleDb(): WeatherDb {
        val sqlDriver by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            getSqlDriver(context)
        }
        return if (::weatherDb.isInitialized) {
            weatherDb
        } else {
            weatherDb = WeatherDb(sqlDriver)
            weatherDb
        }
    }

    private val apiService: ApiService = retrofit.create(ApiService::class.java)

    private val repository: WeatherRepository =
        WeatherRepositoryImpl(apiService, getWeatherTitleDb())

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

    val storedPlaceViewModelFactory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
        ): T {
            return StoredPlaceViewModel(
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


    fun getDetailScreenViewModelFactory(
        latitude: String,
        longitude: String,
    ): ViewModelProvider.Factory {
        this.latitude = latitude
        this.longitude = longitude
        return detailPlaceViewModelFactory
    }

    private lateinit var renameViewModelLatitude: String
    private lateinit var renameViewModelLongitude: String

    private val renamePlaceViewModelFactory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
        ): T {
            return RenamePlaceViewModel(
                repository = repository,
                routeFlow = router.routeFlow,
                latitude = renameViewModelLatitude,
                longitude = renameViewModelLongitude
            ) as T
        }
    }

    fun getRenameScreenViewModelFactory(
        latitude: String,
        longitude: String,
    ): ViewModelProvider.Factory {
        this.renameViewModelLatitude = latitude
        this.renameViewModelLongitude = longitude
        return renamePlaceViewModelFactory
    }

    fun inject(activity: MainActivity) {
        activity.appRouter = router
    }

    fun setAppRouter(appRouter: AppRouter) {
        this.router = appRouter
    }
}