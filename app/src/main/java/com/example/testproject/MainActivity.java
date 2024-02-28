package com.example.testproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;

import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.module.http.HttpRequestUtil;

import android.view.View;

public class MainActivity extends AppCompatActivity {
    private MapView mapView;
    private MapboxMap map = null;
    private final String apiKey = BuildConfig.MAPIR_API_KEY;

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(getApplicationContext());
        LayoutInflater inflater = LayoutInflater.from(this);
        View rootView = inflater.inflate(R.layout.activity_main, null);
        setContentView(rootView);
        mapView = rootView.findViewById(R.id.mapView);
        runOnUiThread(() -> init(mapView, MapirStyle.VERNA, MainActivity.this, apiKey));
    }

    public void init(MapView mapView, String style, Context context, String apiKey) {
        HttpRequestUtil.setOkHttpClient(new NetworkUtils(context).getOkHttpClient(apiKey));
        mapView.getMapAsync(mapboxMap -> {
            map = mapboxMap;
            map.getUiSettings().setLogoMargins(10000, 0, 0, 0);
            map.getUiSettings().setAttributionMargins(10000, 0, 0, 0);
            mapboxMap.setStyle(new Style.Builder().fromUri(style));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(35.690975, 51.433868))
                    .zoom(6.0)
                    .build();
            mapboxMap.setCameraPosition(cameraPosition);
        });
    }
}