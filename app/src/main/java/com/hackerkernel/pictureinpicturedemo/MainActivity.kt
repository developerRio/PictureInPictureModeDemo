package com.hackerkernel.pictureinpicturedemo

import android.Manifest

import android.app.PictureInPictureParams
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.util.Rational
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    override fun onMapReady(googleMap: GoogleMap?) {

        // Permissions Check
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val latLng = LatLng(23.252994, 77.457800) // manually setting up coordinates

        googleMap?.uiSettings?.isRotateGesturesEnabled ?: true
        googleMap?.uiSettings?.isMyLocationButtonEnabled = true
        googleMap?.uiSettings?.isRotateGesturesEnabled = true
        googleMap?.uiSettings?.isMyLocationButtonEnabled
        googleMap?.uiSettings?.isTiltGesturesEnabled = true
        googleMap?.isMyLocationEnabled = true


        googleMap?.addMarker(
            MarkerOptions()
                .title("Mizo Digital Pvt. Ltd")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("", 70, 70)))
                .snippet("Business Solutions")
                .position(latLng)
        )

    }

    private var mMapFragment: SupportMapFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mMapFragment = supportFragmentManager.findFragmentById(R.id.map_view) as SupportMapFragment?
        mMapFragment!!.getMapAsync(this)


        val options = GoogleMapOptions()  // Initialising MapOptions
        options.mapType(GoogleMap.MAP_TYPE_SATELLITE)
            .compassEnabled(false)
            .rotateGesturesEnabled(false)
            .tiltGesturesEnabled(false)

        pip_button.setOnClickListener {  // Performing the onClickAction to go into PiP mode
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // condition to check whether current os is oreo or above or what !!!
                val mpipParams = PictureInPictureParams.Builder()
                val display = windowManager.defaultDisplay
                val point = Point() // Pointer captures height & width of the current screen & reduces the aspect ratio as per required
                display.getSize(point)
                mpipParams.setAspectRatio(Rational(point.x, point.y)) // setting up Aspect ratio as rational
                enterPictureInPictureMode(mpipParams.build())
            } else {
                // if ("VERSION.SDK_INT < O")
                Toast.makeText(
                    this, "Picture In Picture Mode not supported !!! Update your phone to Android Oreo(8.0)"
                    , Toast.LENGTH_SHORT
                ).show()
            }
        }


    }// onCreate closes

    // override Listener which detects whether the PiP mode is active or not !
    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean, newConfig: Configuration?) {
        if (isInPictureInPictureMode){
            map_container.visibility = View.VISIBLE
            pip_button.visibility = View.INVISIBLE
            supportActionBar?.hide()
            Toast.makeText(
                this, "You're in PIP mode"
                , Toast.LENGTH_SHORT
            ).show()
        }else{
            map_container.visibility = View.INVISIBLE
            pip_button.visibility = View.VISIBLE
            supportActionBar?.show()
            Toast.makeText(
                this, "You're in Normal mode !!!"
                , Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun resizeMapIcons(iconName: String, width: Int, height: Int): Bitmap { // Just a custom Pointer in MapView
        val imageBitmap = BitmapFactory.decodeResource(resources, R.drawable.pointer)
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false)
    }

}
