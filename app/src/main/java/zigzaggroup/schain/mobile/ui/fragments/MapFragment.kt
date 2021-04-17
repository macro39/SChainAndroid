package zigzaggroup.schain.mobile.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import zigzaggroup.schain.mobile.R
import zigzaggroup.schain.mobile.utils.toStandardFormat


class MapFragment : Fragment(R.layout.fragment_map) {

    private val args: MapFragmentArgs by navArgs()

    private val callback = OnMapReadyCallback { googleMap ->
        val history = args.itemHistory

        history.asReversed().forEachIndexed { i, it ->
            val point = LatLng(it.coordinates.latitude, it.coordinates.longitude)

            val icon = when (i) {
                0 -> {
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
                }
                history.lastIndex -> {
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                }
                else -> {
                    BitmapDescriptorFactory.defaultMarker()
                }
            }

            googleMap.addMarker(
                MarkerOptions().position(point).title("${i + 1}: ${it.info}")
                    .snippet(it.date.toStandardFormat())
                    .icon(icon)
            )

            val position = CameraUpdateFactory.newLatLngZoom(point, 8F)
            googleMap.moveCamera(position)
        }

        val points =
            history.map { LatLng(it.coordinates.latitude, it.coordinates.longitude) }

        points.zipWithNext { a, b ->
            googleMap.addPolyline(
                PolylineOptions()
                    .add(a, b)
                    .width(5F)
                    .color(Color.RED)
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}