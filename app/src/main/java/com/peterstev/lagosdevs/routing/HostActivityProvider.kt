package com.n2aa.mobile.inversion

import android.app.Activity

interface HostActivityProvider {

    fun getActivity(): Activity?
}
