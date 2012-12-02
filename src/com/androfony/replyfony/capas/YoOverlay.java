package com.androfony.replyfony.capas;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Location;

import com.androfony.replyfony.R;
import com.androfony.replyfony.R.drawable;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class YoOverlay extends Overlay {

	private Location ubicacion;

	public YoOverlay(Location ubicacion) {
		this.ubicacion = ubicacion;
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		Projection proyeccion = mapView.getProjection();
		int latitud = (int) (ubicacion.getLatitude() * 1E6);
		int longitud = (int) (ubicacion.getLongitude() * 1E6);
		GeoPoint geoPoint = new GeoPoint(latitud, longitud);

		Point puntoCentro = new Point();
		proyeccion.toPixels(geoPoint, puntoCentro);

		Bitmap bitmap = BitmapFactory.decodeResource(mapView.getResources(), R.drawable.ic_action_lugar);

		canvas.drawBitmap(bitmap, puntoCentro.x - bitmap.getWidth(), puntoCentro.y - bitmap.getHeight(), null);

	}

}
