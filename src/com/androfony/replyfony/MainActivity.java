package com.androfony.replyfony;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androfony.replyfony.capas.YoOverlay;
import com.androfony.replyfony.util.Util;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MainActivity extends MapActivity {

	// Localización
	private LocationManager locationManager;
	private Handler handler;

	// Mapa
	private MapView mapa;
	private MapController controlMapa;
	
	// Temperatura
	private TextView textoTituloTemperatura;
	private TextView textoNumeroTemperatura;
	private TextView textoEstadoTemperatura;
	private ImageView imagenTemperatura;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Mapa
		mapa = (MapView) findViewById(R.id.mapa);
		controlMapa = mapa.getController();
		
		// Temperatura
		textoTituloTemperatura = (TextView) findViewById(R.id.tituloTemperatura);
		textoNumeroTemperatura = (TextView) findViewById(R.id.textoNumeroTemperatura);
		textoEstadoTemperatura = (TextView) findViewById(R.id.textoEstadoTemperatura);
		imagenTemperatura = (ImageView) findViewById(R.id.imagenTemperatura);

		// ***
		// final SwipeDetector swipeDetector = new SwipeDetector();
		// lista.setOnTouchListener(swipeDetector);

		// ***

		// lista.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View view, int position,
		// long arg3) {
		// if (swipeDetector.swipeDetected()) {
		// // Toast.makeText(getApplicationContext(), "Swipe",
		// // Toast.LENGTH_SHORT).show();
		//
		// TranslateAnimation anim = new TranslateAnimation(0, 0, -40, 0);
		// anim.setDuration(1000);
		// anim.setFillAfter(true);
		// view.setAnimation(anim);
		//
		// adaptadorLista.eliminar(position);
		// } else {
		// Toast.makeText(getApplicationContext(), "Clic Normal",
		// Toast.LENGTH_SHORT).show();
		// }
		// }
		// });

		handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 7:
					Location localizacionEncontrada = (Location) msg.obj;

					int latitud = (int) (localizacionEncontrada.getLatitude() * 1E6);
					int longitud = (int) (localizacionEncontrada.getLongitude() * 1E6);
					GeoPoint geoPoint = new GeoPoint(latitud, longitud);
					controlMapa.animateTo(geoPoint);
					controlMapa.setCenter(geoPoint);
					controlMapa.setZoom(15);
					// Pin
					List<Overlay> capas = mapa.getOverlays();
					capas.clear();
					YoOverlay miCapa = new YoOverlay(localizacionEncontrada);
					capas.add(miCapa);
					mapa.postInvalidate();

					break;
				}
			}
		};

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	}

	@Override
	protected void onStart() {
		super.onStart();

		// Comprobación
		// LocationManager locationManagerExtra = (LocationManager)
		// getSystemService(Context.LOCATION_SERVICE);
		// boolean gpsHabilitado =
		// locationManagerExtra.isProviderEnabled(LocationManager.GPS_PROVIDER);
		// if (!gpsHabilitado) {
		// Util.mostrarAlertaActivarGPS(this);
		// }

		// Iniciar localización
		iniciarLocalizacion();
		//cargarDatosTemperatura();
	}

	private void cargarDatosTemperatura() {
		HttpClient cliente = new DefaultHttpClient();
		HttpGet get = new HttpGet("http://192.168.43.241:8080/.../id");
		get.setHeader("content-type", "application/json");
		try {
			HttpResponse respuesta = cliente.execute(get);
			String res = EntityUtils.toString(respuesta.getEntity());
			JSONObject object = new JSONObject(res);
			int id = object.getInt("id");
			String nombre = object.getString("nombre");
			int telefono = object.getInt("telefono");
			
			// Visualizar
			textoTituloTemperatura.setText("La Paz");
			textoNumeroTemperatura.setText("");
			textoEstadoTemperatura.setText("");
			imagenTemperatura.setImageResource(0);

		} catch (Exception ex) {
		}

	}

	private void iniciarLocalizacion() {
		Location localizacionGPS = null;
		Location localizacionRED = null;
		locationManager.removeUpdates(oyenteUbicaciones);

		localizacionGPS = pedirActualizacionesDeProveedor(LocationManager.GPS_PROVIDER, R.string.gpsNoSoportado);
		localizacionRED = pedirActualizacionesDeProveedor(LocationManager.NETWORK_PROVIDER, R.string.redNoSoportado);

		if (localizacionGPS != null && localizacionRED != null) {
			actualizarIU(Util.getMejorLocalizacion(localizacionGPS, localizacionRED));
		} else if (localizacionGPS != null) {
			actualizarIU(localizacionGPS);
		} else if (localizacionRED != null) {
			actualizarIU(localizacionRED);
		}
	}

	private void actualizarIU(Location localizacion) {
		// El numero 7 solo simbolico
		Message.obtain(handler, 7, localizacion).sendToTarget();
	}

	private Location pedirActualizacionesDeProveedor(String proveedor, int mensajeError) {
		Location localizacion = null;
		if (locationManager.isProviderEnabled(proveedor)) {
			int DIEZ_SEGUNDOS = 10000;
			int DIEZ_METROS = 10;
			locationManager.requestLocationUpdates(proveedor, DIEZ_SEGUNDOS, DIEZ_METROS, oyenteUbicaciones);
			localizacion = locationManager.getLastKnownLocation(proveedor);
		} else {
			Toast.makeText(this, mensajeError, Toast.LENGTH_LONG).show();
		}
		return localizacion;
	}

	// MENU
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(this, ConfiguracionActivity.class);
		startActivity(intent);
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	private final LocationListener oyenteUbicaciones = new LocationListener() {

		public void onLocationChanged(Location location) {
			actualizarIU(location);
		}

		public void onProviderDisabled(String provider) {

		}

		public void onProviderEnabled(String provider) {

		}

		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

	};

	public void creditos(View view) {
		Toast.makeText(this, "Desarrollado por ANDROFONY", Toast.LENGTH_LONG).show();
	}

	public void iniciarCheckIn(View view) {
		Intent intent = new Intent(this, CheckInActivity.class);
		startActivity(intent);
	}

	public void voz(View view) {
		Toast.makeText(this, "Aun no disponible.", Toast.LENGTH_SHORT).show();
	}

}
