package com.androfony.replyfony;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;

import com.androfony.replyfony.adapters.AdaptadorLista;
import com.androfony.replyfony.util.Util;
import com.google.android.maps.MapActivity;

public class MainActivity extends MapActivity {

	// Localización
	private LocationManager locationManager;
	private Handler handler;

	// IU
	private ListView lista;
	private AdaptadorLista adaptadorLista;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		lista = (ListView) findViewById(R.id.lista);

		adaptadorLista = new AdaptadorLista(this);
		lista.setAdapter(adaptadorLista);

		adaptadorLista.adicionarItem("Fútbol");
		adaptadorLista.adicionarItem("Basquetbol");
		adaptadorLista.adicionarItem("Valleybol");
		adaptadorLista.adicionarItem("Fútbol");
		adaptadorLista.adicionarItem("Basquetbol");
		adaptadorLista.adicionarItem("Valleybol");
		adaptadorLista.adicionarItem("Fútbol");
		adaptadorLista.adicionarItem("Basquetbol");
		adaptadorLista.adicionarItem("Valleybol");
		adaptadorLista.adicionarItem("Fútbol");
		adaptadorLista.adicionarItem("Basquetbol");
		adaptadorLista.adicionarItem("Valleybol");
		adaptadorLista.adicionarItem("Fútbol");
		adaptadorLista.adicionarItem("Basquetbol");
		adaptadorLista.adicionarItem("Valleybol");

		handler = new Handler() {
			public void handleMessage(Message msg) {
				// switch (msg.what) {
				// case ACTUALIZAR_LATITUD:
				// textoLatitud.setText((String) msg.obj);
				// break;
				// case ACTUALIZAR_LONGITUD:
				// textoLongitud.setText((String) msg.obj);
				// break;
				// }
			}
		};
	}

	@Override
	protected void onStart() {
		super.onStart();

		// Comprobación
		LocationManager locationManagerExtra = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		boolean gpsHabilitado = locationManagerExtra.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (!gpsHabilitado) {
			Util.mostrarAlertaActivarGPS(this);
		}

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
			// actualizarIU(localizacionGPS);
		} else if (localizacionRED != null) {
			// actualizarIU(localizacionRED);
		}
	}

	private void actualizarIU(Location localizacion) {
		Message.obtain(handler, 0, localizacion.getLatitude() + "").sendToTarget();
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	private final LocationListener oyenteUbicaciones = new LocationListener() {

		public void onLocationChanged(Location location) {
			// actualizarIU(location);
		}

		public void onProviderDisabled(String provider) {

		}

		public void onProviderEnabled(String provider) {

		}

		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

	};

}
