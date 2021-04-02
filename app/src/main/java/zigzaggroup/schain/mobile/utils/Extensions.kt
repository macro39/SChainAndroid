package zigzaggroup.schain.mobile.utils

import android.app.Activity
import android.content.Context
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    GlobalScope.launch(Dispatchers.Main) {
        Toast.makeText(this@toast, message, duration).show()
    }
}

fun Activity.title(title: String) {
    (this as AppCompatActivity).supportActionBar?.title = title
}

fun ProgressBar.show() {
    GlobalScope.launch(Dispatchers.Main) {
        this@show.isVisible = true
    }
}

fun ProgressBar.hide() {
    GlobalScope.launch(Dispatchers.Main) {
        this@hide.isVisible = false
    }
}