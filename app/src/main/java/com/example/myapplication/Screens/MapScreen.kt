package com.example.myapplication.Screens

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.VM.MainViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.MapTileProviderBasic
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.util.MapTileIndex.getX
import org.osmdroid.util.MapTileIndex.getY
import org.osmdroid.util.MapTileIndex.getZoom
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.TilesOverlay

@Composable
fun MapScreen(context: Context = LocalContext.current, viewModel: MainViewModel = hiltViewModel()) {
    Configuration.getInstance().load(
        context,
        context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE)
    )

    AndroidView(factory = {
        val map = MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setMultiTouchControls(true)
            controller.setZoom(5.0)
            controller.setCenter(GeoPoint(55.7558, 37.6173)) // Москва
        }

        // Добавим маркер
        val marker = Marker(map).apply {
            position = GeoPoint(55.7558, 37.6173)
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            title = "Москва"
        }
        map.overlays.add(marker)

        // OpenWeatherMap: слой осадков
        val apiKey = viewModel.apiKey

        val weatherTileSource = object : OnlineTileSourceBase(
            "OpenWeatherMapPrecipitation",
            0, 19, 256, ".png",
            arrayOf("https://tile.openweathermap.org/map/precipitation_new/"),
            "© OpenWeatherMap"
        ) {
            override fun getTileURLString(pMapTileIndex: Long): String {
                val zoom = getZoom(pMapTileIndex)
                val x = getX(pMapTileIndex)
                val y = getY(pMapTileIndex)
                return "https://tile.openweathermap.org/map/precipitation_new/$zoom/$x/$y.png?appid=$apiKey"
            }
        }

        val tileProvider = MapTileProviderBasic(context).apply {
            setTileSource(weatherTileSource)
        }

        val weatherOverlay = TilesOverlay(tileProvider, context).apply {
            setLoadingBackgroundColor(Color.TRANSPARENT)
            setColorFilter(PorterDuffColorFilter(Color.argb(150, 0, 0, 255), PorterDuff.Mode.SRC_ATOP))
        }

        map.overlays.add(weatherOverlay)

        map
    }, modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .clip(RoundedCornerShape(4.dp)),)
}
