package com.example.test_platform_channel.arcgis

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.esri.arcgisruntime.layers.OpenStreetMapLayer
import com.esri.arcgisruntime.mapping.ArcGISMap
import com.esri.arcgisruntime.mapping.BasemapStyle
import com.esri.arcgisruntime.mapping.view.MapView

@Composable
fun ArcGISMapComposable(modifier: Modifier) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }.apply {
        map = ArcGISMap()
        map.basemap.baseLayers.add(OpenStreetMapLayer())
    }

    AndroidView(
        factory = { mapView },
        modifier = modifier
    )
}