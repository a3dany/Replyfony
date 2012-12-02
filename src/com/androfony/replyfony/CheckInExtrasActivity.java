package com.androfony.replyfony;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

public class CheckInExtrasActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkinextras);
	}

	public void enviarDatos(View view) {

	}

	public void abrirCamara(View view) {
		Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(i, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_CANCELED) {
			Toast.makeText(this, "Se cancelo la captura por cámara.", Toast.LENGTH_SHORT).show();
		} else if (resultCode == Activity.RESULT_OK) {
			Bitmap bitmapCamera = (Bitmap) data.getExtras().get("data");
			if (bitmapCamera != null) {
				try {
					// guardarImagen(bitmapCamera);
				} catch (Exception e) {
				}
			}
		}
	}

}
