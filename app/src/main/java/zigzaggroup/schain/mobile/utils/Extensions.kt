package zigzaggroup.schain.mobile.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    GlobalScope.launch(Dispatchers.Main) {
        Toast.makeText(this@toast, message, duration).show()
    }
}

fun Activity.title(title: String) {
    (this as AppCompatActivity).supportActionBar?.title = title
}

fun View.show() {
    GlobalScope.launch(Dispatchers.Main) {
        this@show.isVisible = true
    }
}

fun View.hide() {
    GlobalScope.launch(Dispatchers.Main) {
        this@hide.isVisible = false
    }
}

fun Date.toStandardFormat(): String {
    val df = SimpleDateFormat("dd.MM.yyyy, HH:mm:ss", Locale.US)
    return df.format(this)
}

fun <T : RecyclerView.ViewHolder?> RecyclerView.adapter(adapter: RecyclerView.Adapter<T>) {
    val manager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)

    this.layoutManager = manager
    this.adapter = adapter
}

fun String.toToken(): String {
    return "Bearer $this"
}
