package com.androfony.replyfony.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androfony.replyfony.R;

public class AdaptadorLista extends BaseAdapter {
	private LayoutInflater inflater;
	private List<String> titulos;
	private Context context;

	public AdaptadorLista(Context contexto) {
		this.context = contexto;
		inflater = LayoutInflater.from(contexto);
		titulos = new ArrayList<String>();
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

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			if (position % 3 == 0) {
				convertView = inflater.inflate(R.layout.item_tiempo, null);
			} else {
				if (position % 2 == 0) {
					convertView = inflater.inflate(R.layout.item_evento, null);
				} else {
					convertView = inflater.inflate(R.layout.item_distancia,
							null);
				}

			}
			holder = new ViewHolder();
			holder.titulo = (TextView) convertView
					.findViewById(R.id.textoTitulo);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Typeface t = Typeface.createFromAsset(context.getAssets(),
				"fonts/Roboto-Light.ttf");
		holder.titulo.setText(titulos.get(position));
		holder.titulo.setTypeface(t);
		return convertView;
	}

	static class ViewHolder {
		TextView titulo;
	}

	public void adicionarItem(int recurso, String titulo, CharSequence subtitulo) {
		titulos.add(titulo);
		notifyDataSetChanged();
	}

	public void adicionarItem(String titulo, CharSequence subtitulo) {
		titulos.add(titulo);
	}

	public void adicionarItem(String titulo) {
		titulos.add(titulo);
	}

	public void eliminarTodo() {
		titulos.clear();
		notifyDataSetChanged();
	}
}
