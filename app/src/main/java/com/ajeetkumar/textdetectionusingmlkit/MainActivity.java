package com.ajeetkumar.textdetectionusingmlkit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ajeetkumar.textdetectionusingmlkit.camera.CameraSource;
import com.ajeetkumar.textdetectionusingmlkit.camera.CameraSourcePreview;
import com.ajeetkumar.textdetectionusingmlkit.others.GraphicOverlay;
import com.ajeetkumar.textdetectionusingmlkit.text_detection.TextRecognitionProcessor;
import com.google.firebase.FirebaseApp;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

	//region ----- Instance Variables -----

	private CameraSource cameraSource = null;
	private CameraSourcePreview preview;
	private GraphicOverlay graphicOverlay;

	private static String TAG = MainActivity.class.getSimpleName().toString().trim();

	//endregion

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//FirebaseApp.initializeApp(this);

		preview = (CameraSourcePreview) findViewById(R.id.camera_source_preview);
		if (preview == null) {
			Log.d(TAG, "Preview is null");
		}
		graphicOverlay = (GraphicOverlay) findViewById(R.id.graphics_overlay);
		if (graphicOverlay == null) {
			Log.d(TAG, "graphicOverlay is null");
		}

		createCameraSource();
		startCameraSource();
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
		startCameraSource();
	}

	/** Stops the camera. */
	@Override
	protected void onPause() {
		super.onPause();
		preview.stop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (cameraSource != null) {
			cameraSource.release();
		}
	}

	private void createCameraSource() {

		if (cameraSource == null) {
			cameraSource = new CameraSource(this, graphicOverlay);
			cameraSource.setFacing(CameraSource.CAMERA_FACING_BACK);
		}

		cameraSource.setMachineLearningFrameProcessor(new TextRecognitionProcessor());
	}

	private void startCameraSource() {
		if (cameraSource != null) {
			try {
				if (preview == null) {
					Log.d(TAG, "resume: Preview is null");
				}
				if (graphicOverlay == null) {
					Log.d(TAG, "resume: graphOverlay is null");
				}
				preview.start(cameraSource, graphicOverlay);
			} catch (IOException e) {
				Log.e(TAG, "Unable to start camera source.", e);
				cameraSource.release();
				cameraSource = null;
			}
		}
	}
}
