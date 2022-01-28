package com.peterstev.lagosdevs.inversion

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.n2aa.mobile.inversion.HostActivityProvider
import javax.inject.Inject

class HostActivityProviderImpl @Inject constructor(application: Application) :
    HostActivityProvider {

    var hostActivity: Activity? = null

    init {
        application.registerActivityLifecycleCallbacks(object :
            Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                hostActivity = activity
            }

            override fun onActivityStarted(activity: Activity) {
                hostActivity = activity
            }

            override fun onActivityResumed(activity: Activity) {
                hostActivity = activity
            }

            override fun onActivityPaused(activity: Activity) {}

            override fun onActivityStopped(activity: Activity) {}

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

            override fun onActivityDestroyed(activity: Activity) {
                if (hostActivity == activity) hostActivity = null
            }

        })
    }

    override fun getActivity() = hostActivity

}
