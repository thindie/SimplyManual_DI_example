package com.thindie.simplyweather.application

import android.app.Application
import com.thindie.simplyweather.di.DependenciesProvider

class WeatherApplication : Application(), DependenciesProvider.DependenciesHolder {

    private lateinit var dependenciesProvider: DependenciesProvider

    override fun setDependenciesProvider(dependenciesProvider: DependenciesProvider) {
        if (!::dependenciesProvider.isInitialized) {
            this.dependenciesProvider = dependenciesProvider
        } else {
            /*ignore*/
        }
    }

    override fun getDependenciesProvider(): DependenciesProvider {
        if (!::dependenciesProvider.isInitialized) {
            DependenciesProvider.getInstance(this)
        }
        return dependenciesProvider
    }

}