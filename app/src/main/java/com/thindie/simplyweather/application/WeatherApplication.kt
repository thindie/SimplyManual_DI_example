package com.thindie.simplyweather.application

import android.app.Application
import com.thindie.simplyweather.di.DependenciesProvider

class WeatherApplication : Application(), DependenciesProvider.DependenciesHolder {

    private lateinit var dependenciesProvider: DependenciesProvider

    override fun setDependenciesProvider(dependenciesHolder: DependenciesProvider.DependenciesHolder) {
        if (::dependenciesProvider.isInitialized){
           /*ignore*/
        }
        else DependenciesProvider.getInstance(this)
    }

    override fun getDependenciesProvider(): DependenciesProvider {
       setDependenciesProvider(this)
        return dependenciesProvider
    }

}