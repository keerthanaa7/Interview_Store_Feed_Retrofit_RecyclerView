package com.tps.challenge.dagger

import android.app.Application
import com.tps.challenge.TCApplication
import com.tps.challenge.features.storefeed.StoreFeedFragment
import dagger.Component
import javax.inject.Singleton
import com.tps.challenge.database.AppDatabase
import com.tps.challenge.network.TPSCallService
import com.tps.challenge.network.TPSCoroutineService
import com.tps.challenge.network.TPSRxService

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {
        fun appModule(module: AppModule): Builder
        fun build(): AppComponent
    }

    fun inject(app: TCApplication)
    fun inject(storeFeedFragment: StoreFeedFragment)

    //keer added methods to get data from each module provides function
    fun getappModuleApplication():Application
    fun getappDatabase():AppDatabase
    fun getTPSCallService(): TPSCallService
    fun getTPSCoroutineService(): TPSCoroutineService
    fun getTPSRxService(): TPSRxService

}
