package com.androfony.replyfony.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.androfony.replyfony.R;
import com.androfony.replyfony.util.Constants;

public class AdaptadorLista extends BaseAdapter {

	private ArrayList<String> titulos = new ArrayList<String>();
	private ArrayList<Integer> tipos = new ArrayList<Integer>();

	private LayoutInflater inflater;

	public AdaptadorLista(Context context) {
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return titulos.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		return tipos.get(position);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
//		 Typeface t = Typeface.createFromAsset(context.getAssets(),
//		 "fonts/Roboto-Light.ttf");
		// holder.titulo.setTypeface(t);
		ViewHolder holder = null;
		int type = getItemViewType(position);
		if (convertView == null) {
			holder = new ViewHolder();
			switch (type) {
			case Constants.ITEM_MAPA:
				convertView = inflater.inflate(R.layout.item_distancia, null);
				holder.textoTitulo = (TextView) convertView.findViewById(R.id.textoTitulo);
				break;
			case Constants.ITEM_INFO:
				convertView = inflater.inflate(R.layout.item_evento, null);
				holder.textoTitulo = (TextView) convertView.findViewById(R.id.textoTitulo);
				break;
			case Constants.ITEM_RESULTADO:

				break;

			}
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.textoTitulo.setText(titulos.get(position));
		return convertView;
	}

	static class ViewHolder {
		public TextView textoTitulo;
	}

	public void adicionarItem(String titulo, int tipo) {
		titulos.add(titulo);
		tipos.add(tipo);
		notifyDataSetChanged();
	}

	public void eliminarTodo() {
		titulos.clear();
		tipos.clear();
		notifyDataSetChanged();
	}

	public void eliminar(int posicion) {
		titulos.remove(posicion);
		tipos.remove(posicion);
		notifyDataSetChanged();
	}
}
