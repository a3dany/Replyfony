package com.androfony.replyfony;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.androfony.replyfony.adapters.AdaptadorListaConfiguraciones;

public class ConfiguracionActivity extends Activity implements OnItemClickListener {

	// private MapView mapa;

	private ListView lista;
	private AdaptadorListaConfiguraciones adaptadorLista;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configuracion);

		lista = (ListView) findViewById(R.id.lista);

		adaptadorLista = new AdaptadorListaConfiguraciones(this);

		lista.setAdapter(adaptadorLista);
		lista.setOnItemClickListener(this);

		// mapa = (MapView) findViewById(R.id.mapa);
		//
		// mapa.setClickable(true);
		//
		// List<Overlay> capas = mapa.getOverlays();
		// CentroOverlay miCapa = new CentroOverlay();
		// capas.add(miCapa);
		// mapa.postInvalidate();

	}

	@Override
	protected void onStart() {
		super.onStart();
		SharedPreferences settings = getSharedPreferences("MisPreferencias", 0);
		int latitudCasa = settings.getInt("latitudCasa", 0);
		int longitudCasa = settings.getInt("longitudCasa", 0);
		int latitudEducacion = settings.getInt("latitudEducacion", 0);
		int longitudEducacion = settings.getInt("longitudEducacion", 0);
		int latitudTrabajo = settings.getInt("latitudTrabajo", 0);
		int longitudTrabajo = settings.getInt("longitudTrabajo", 0);

		adaptadorLista.eliminarTodo();
		adaptadorLista.adicionarItem(R.drawable.ic_action_persona, "Sexo", "Masculino");
		adaptadorLista.adicionarItem(R.drawable.ic_action_persona, "Casa", latitudCasa + ", " + longitudCasa);
		adaptadorLista.adicionarItem(R.drawable.ic_action_persona, "Lugar de Estudio", latitudEducacion + ", "
				+ longitudEducacion);
		adaptadorLista.adicionarItem(R.drawable.ic_action_persona, "Lugar de Trabajo", latitudTrabajo + ", "
				+ longitudTrabajo);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		switch (position) {
		case 0:

			break;
		case 1:
			Intent intentCasa = new Intent(this, LocalizarActivity.class);
			intentCasa.putExtra("tipo", "casa");
			startActivity(intentCasa);
			finish();
			break;
		case 2:
			Intent intentEducacion = new Intent(this, LocalizarActivity.class);
			intentEducacion.putExtra("tipo", "educacion");
			startActivity(intentEducacion);
			finish();
			break;
		case 3:
			Intent intentTrabajo = new Intent(this, LocalizarActivity.class);
			intentTrabajo.putExtra("tipo", "trabajo");
			startActivity(intentTrabajo);
			finish();
			break;
		case 4:

			break;

		}
	}

}
