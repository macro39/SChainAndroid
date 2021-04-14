package zigzaggroup.schain.mobile.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import zigzaggroup.schain.mobile.R

class MapFragment : Fragment(R.layout.fragment_map) {

    private val args: MapFragmentArgs by navArgs()

    private val callback = OnMapReadyCallback { googleMap ->
        args.itemHistory.asReversed().forEachIndexed { i, it ->
            val point = LatLng(it.coordinates.latitude, it.coordinates.longitude)
            googleMap.addMarker(MarkerOptions().position(point).title("${i + 1}: ${it.info}"))

            val position = CameraUpdateFactory.newLatLngZoom(point, 8F)
            googleMap.moveCamera(position)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}