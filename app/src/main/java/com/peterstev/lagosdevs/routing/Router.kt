package com.peterstev.lagosdevs.routing

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import javax.inject.Inject

class Router (
    private val hostActivity: Activity,
    @IdRes private val navHostId: Int,
) {

    private val navController: NavController
        get() = hostActivity.findNavController(navHostId)

    fun navigate(directions: NavDirections) {
        navController.navigate(directions)
    }

    fun toBrowser(url: String) {
        hostActivity.let {
            try {
                it.startActivity(
                    Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(url)
                    }
                )
            } catch (e: ActivityNotFoundException) {
            }
        }
    }
}
