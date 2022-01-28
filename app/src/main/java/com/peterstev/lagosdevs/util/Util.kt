package com.peterstev.lagosdevs.util

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

fun printLog(message: Any) {
    println("XXXX $message")
}

fun <T> Single<T>.thread(schedulers: RxSchedulers): Single<T> {
    return this
        .subscribeOn(schedulers.IO)
        .observeOn(schedulers.MAIN)
}

fun <T> Observable<T>.thread(schedulers: RxSchedulers): Observable<T> {
    return this
        .subscribeOn(schedulers.IO)
        .observeOn(schedulers.MAIN)
}

fun <T> Flowable<T>.thread(schedulers: RxSchedulers): Flowable<T> {
    return this
        .subscribeOn(schedulers.IO)
        .observeOn(schedulers.MAIN)
}

fun Completable.thread(schedulers: RxSchedulers): Completable {
    return this
        .subscribeOn(schedulers.IO)
        .observeOn(schedulers.MAIN)
}
