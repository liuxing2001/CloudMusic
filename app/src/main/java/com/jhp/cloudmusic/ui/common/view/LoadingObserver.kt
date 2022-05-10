package com.jhp.cloudmusic.ui.common.view

import android.app.ProgressDialog
import android.content.Context
import androidx.lifecycle.Observer

class LoadingObserver(context: Context) : Observer<Boolean> {
    private val dialog = ProgressDialog(context)

    override fun onChanged(show: Boolean?) {
        if (show == null) return
        if (show) {
            dialog.show()
        } else {
            dialog.dismiss()
        }
    }
}