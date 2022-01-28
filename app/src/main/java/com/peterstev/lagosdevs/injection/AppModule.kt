package com.peterstev.lagosdevs.injection

import android.app.Activity
import android.app.Application
import com.n2aa.mobile.inversion.HostActivityProvider
import com.peterstev.lagosdevs.R
import com.peterstev.lagosdevs.inversion.HostActivityProviderImpl
import com.peterstev.lagosdevs.routing.Router
import com.peterstev.lagosdevs.util.BaseSchedulerProvider
import com.peterstev.lagosdevs.util.RxSchedulers
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun providesRxScheduler(): RxSchedulers {
        return BaseSchedulerProvider()
    }

    @Provides
    fun providesApplication(activity: Activity): Application {
        return activity.application
    }

    @Provides
    fun providesCurrentActivity(application: Application): HostActivityProvider {
        return HostActivityProviderImpl(application)
    }

    @Provides
    fun providesRouter(hostActivity: Activity): Router {
        return Router(hostActivity, R.id.main_nav_host)
    }
}
