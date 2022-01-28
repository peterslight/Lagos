package com.peterstev.lagosdevs.util

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

class BaseSchedulerProvider : RxSchedulers {
    override val IO: Scheduler
        get() = Schedulers.io()
    override val MAIN: Scheduler
        get() = AndroidSchedulers.mainThread()
}
