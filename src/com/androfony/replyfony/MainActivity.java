package com.androfony.replyfony;

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
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.androfony.replyfony.adapters.AdaptadorLista;
import com.androfony.replyfony.util.Constants;
import com.androfony.replyfony.util.SwipeDetector;
import com.androfony.replyfony.util.Util;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class MainActivity extends MapActivity {

	// Localización
	private LocationManager locationManager;
	private Handler handler;

	// IU
	private ListView lista;
	private AdaptadorLista adaptadorLista;

	// Mapa
	private MapView mapa;
	private MapController controlMapa;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		lista = (ListView) findViewById(R.id.lista);
		adaptadorLista = new AdaptadorLista(this);
		lista.setAdapter(adaptadorLista);

		// ***
		final SwipeDetector swipeDetector = new SwipeDetector();
		lista.setOnTouchListener(swipeDetector);

		// ***

		lista.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
				if (swipeDetector.swipeDetected()) {
					// Toast.makeText(getApplicationContext(), "Swipe",
					// Toast.LENGTH_SHORT).show();

					TranslateAnimation anim = new TranslateAnimation(0, 0, -40, 0);
					anim.setDuration(1000);
					anim.setFillAfter(true);
					view.setAnimation(anim);

					adaptadorLista.eliminar(position);
				} else {
					Toast.makeText(getApplicationContext(), "Clic Normal", Toast.LENGTH_SHORT).show();
				}
			}
		});

		handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 7:
					Location localizacionEncontrada = (Location) msg.obj;

					// int latitud = (int) (localizacionEncontrada.getLatitude()
					// * 1E6);
					// int longitud = (int)
					// (localizacionEncontrada.getLongitude() * 1E6);
					// GeoPoint geoPoint = new GeoPoint(latitud, longitud);
					// controlMapa.animateTo(geoPoint); //
					// controlMapa.setCenter(geoPoint);
					// controlMapa.setZoom(18);
					//
					Toast.makeText(getApplicationContext(),
							localizacionEncontrada.getLatitude() + ", " + localizacionEncontrada.getLongitude(),
							Toast.LENGTH_LONG).show();
					break;
				}
			}
		};

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// Tarjetas
		adaptadorLista.adicionarItem("Distancia a casa", Constants.ITEM_MAPA);
		adaptadorLista.adicionarItem("Cosas", Constants.ITEM_INFO);

		LinearLayout layout = (LinearLayout) adaptadorLista.getItem(0);
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
