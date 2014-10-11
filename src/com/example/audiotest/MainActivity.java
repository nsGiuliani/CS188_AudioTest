package com.example.audiotest;

import java.io.IOException;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private MediaRecorder myRecorder;
	private MediaPlayer myPlayer;
	private String outputFile = null;
	private Button startBtn;
	private Button stopBtn;
	private Button playBtn;
	private Button stopPlayBtn;
	private TextView text;
	private int mInterval = 500; 
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		text = (TextView) findViewById(R.id.text1);
		// store it to sd card
		outputFile = Environment.getExternalStorageDirectory().
				getAbsolutePath() + "/javacodegeeksRecording.3gpp";

		myRecorder = new MediaRecorder();
		myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		myRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		myRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
		myRecorder.setOutputFile(outputFile);
		mHandler = new Handler();

		startBtn = (Button)findViewById(R.id.start);
		startBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				start(v);
			}
		});

		stopBtn = (Button)findViewById(R.id.stop);
		stopBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				stop(v);
			}
		});

	}

	public void start(View view){
		try {
			myRecorder.prepare();
			myRecorder.start();
		} catch (IllegalStateException e) {
			// start:it is called before prepare()
			// prepare: it is called after start() or before setOutputFormat() 
			e.printStackTrace();
		} catch (IOException e) {
			// prepare() fails
			e.printStackTrace();
		}

		startBtn.setEnabled(false);
		stopBtn.setEnabled(true);

		text.setText("Recording Point: Recording, Amp: "+ getAmplitude());
		startRepeatingTask();
		Toast.makeText(getApplicationContext(), "Start recording...", 
				Toast.LENGTH_SHORT).show();
	}

	public void stop(View view){
		try {
			myRecorder.stop();
			myRecorder.release();
			myRecorder  = null;

			stopBtn.setEnabled(false);
			startBtn.setEnabled(true);
			text.setText("Recording Point: Stop recording");
			stopRepeatingTask();
			Toast.makeText(getApplicationContext(), "Stop recording...",
					Toast.LENGTH_SHORT).show();
		} catch (IllegalStateException e) {
			//  it is called before start()
			e.printStackTrace();
		} catch (RuntimeException e) {
			// no valid audio/video data has been received
			e.printStackTrace();
		}
		outputFile = Environment.getExternalStorageDirectory().
				getAbsolutePath() + "/javacodegeeksRecording.3gpp";

		myRecorder = new MediaRecorder();
		myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		myRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		myRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
		myRecorder.setOutputFile(outputFile);
		mHandler = new Handler();
	}


	private double getAmplitude() {
		if (myRecorder != null) {
			double m = myRecorder.getMaxAmplitude();
			return (m);
		} else {

			return 0;
		}
	}
	public void GetAmp(View view) {
		text.setText("Recording Point: Recording, Amp: "+ getAmplitude());
	}
	Runnable mStatusChecker = new Runnable() {
		@Override 
		public void run() {
			mHandler.postDelayed(mStatusChecker, mInterval);
			text.setText("Recording Point: Recording, Amp: "+ getAmplitude());
		}
	};

	void startRepeatingTask() {
		mStatusChecker.run(); 
	}

	void stopRepeatingTask() {
		mHandler.removeCallbacks(mStatusChecker);
	}

}