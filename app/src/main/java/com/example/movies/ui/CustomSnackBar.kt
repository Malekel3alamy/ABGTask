package com.example.movies.ui

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.movies.R
import com.google.android.material.snackbar.Snackbar

//fun Snackbar.withRetryAction(view: View, action: () -> Unit): Snackbar {
//    val snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG)
//
//    // Inflate custom layout
//     val customView = layoutInflater.inflate(R.layout.custom_snackbar, null)
//    val snackbarLayout = snackbarView as Snackbar.SnackbarLayout
//    val retryButton = snackbarLayout.findViewById<Button>(R.id.snackbar_retry_button)
//
//    retryButton.setOnClickListener {
//        action()
//        this.dismiss()
//    }
//
//    return this
//}

fun Context.showCustomSnackBar(container: View?,action: () -> Unit) {
    container?.let {
        val snackView = View.inflate(this, R.layout.snack_bar_layout, null)
        //val binding = FragmentBaseBinding.bind(snackView)
        val snackBar = Snackbar.make(container, "", Snackbar.LENGTH_LONG)
        val button = snackView.findViewById<Button>(R.id.snackbar_retry_button)
        button.setOnClickListener{
            action()

            snackBar.dismiss()
        }
        snackBar.apply {
            (view as ViewGroup).addView(snackView)
            show()
        }
    }
}
