package com.android.demo;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.content.pm.PackageManager;
import android.location.Location;
import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import android.widget.Button;
import android.widget.ListView;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;

public class mainActivity extends AppCompatActivity implements
	GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
	
    private TextView tv;
    private TextView mLatText;
    private TextView mLonText;
	
	private GoogleApiClient gac;
    private Location loc;

    private LocationRequest lr;
    private LocationListener ll;
	private Button btn;
	private Button btn_reset;
    private static final int LOCATION_PERMISSON_RESULT = 17;

	private mydb dba;

	/*
	public SQLiteLocation sql;
	public Cursor sqlCursor;
	public SimpleCursorAdapter sqlCursorAdapter;
	public SQLiteDatabase sqlDB;
	*/

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		//display table
		dba = new mydb(this, (ListView) findViewById(R.id.sql_list_view));
		dba.open();
		dba.populateTable();

		// create textviews
		tv = (TextView) findViewById(R.id.tv0);
		mLatText = (TextView) findViewById(R.id.tv1);
		mLonText = (TextView) findViewById(R.id.tv2);

		// create google api client
		if (gac == null) {
            gac = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

		//create location request
		lr = LocationRequest.create();
		lr.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		lr.setInterval(5000);
		lr.setFastestInterval(5000);

		//create location listener
		ll = new LocationListener() {
			@Override
			public void onLocationChanged(Location loc) {
				if (loc == null) {
					mLonText.setText("No Location Available\n");
					mLatText.setText("No Location Available\n");
				} else {
					mLonText.setText(String.valueOf(loc.getLongitude()));
					mLatText.setText(String.valueOf(loc.getLatitude()));
				}
			}
		};

		/*
		//create database table
		sql = new SQLiteLocation(this);
		sqlDB = sql.getWritableDatabase();
		*/

		btn = (Button) findViewById(R.id.button1);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getPerm();
			}
		});

		btn_reset = (Button) findViewById(R.id.button0);
		btn_reset.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dba.sqlDB.execSQL("DELETE FROM "+ DBContract.LocTable.TABLE_NAME);
				dba.populateTable();
			}
		});

	}

	@Override
	protected void onStart() {
		tv.setText("Activity Started\n");
		gac.connect();
		dba.open();
		super.onStart();
	}

	@Override
	protected void onStop() {
		gac.disconnect();
		dba.close();
		tv.append("Activity stopped\n");
		super.onStop();
	}

	@Override
	public void onConnectionSuspended(int i) {
		tv.append("Activity suspended\n");
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
		tv.append("connection failed\n");
		return;
	}

	@Override
	public void onConnected(@Nullable Bundle bundle)
	{
		tv.append("onConnect\n");
		if (ActivityCompat.checkSelfPermission(this,
					android.Manifest.permission.ACCESS_FINE_LOCATION) 
				!= PackageManager.PERMISSION_GRANTED 
				&&
				ActivityCompat.checkSelfPermission(this, 
					android.Manifest.permission.ACCESS_COARSE_LOCATION) 
				!= PackageManager.PERMISSION_GRANTED)
		{
			/*
			ActivityCompat.requestPermissions(this, 
					new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, 
						android.Manifest.permission.ACCESS_COARSE_LOCATION}, 
						LOCATION_PERMISSON_RESULT);
						*/
			tv.append("Lacking Permissions\n");
			return;
		}
		//getLocation();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, 
			String[] permissions, int[] grantResults){
		if(requestCode == LOCATION_PERMISSON_RESULT){
			if(grantResults.length > 0)
				for (int result : grantResults) {
					if (result != PackageManager.PERMISSION_GRANTED) {
						tv.append("perm denied\n");
					} else {
						tv.append("perm granted\n"); 
					}
				}
		}
		getLocation();
	}

	private void getPerm(){
		ActivityCompat.requestPermissions(this, 
				new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, 
					android.Manifest.permission.ACCESS_COARSE_LOCATION}, 
					LOCATION_PERMISSON_RESULT);
	}

	private void getLocation(){
		if (ActivityCompat.checkSelfPermission(this, 
					android.Manifest.permission.ACCESS_FINE_LOCATION) 
				!= PackageManager.PERMISSION_GRANTED 
				&& 
				ActivityCompat.checkSelfPermission(this, 
					android.Manifest.permission.ACCESS_COARSE_LOCATION) 
				!= PackageManager.PERMISSION_GRANTED)
		{
			mLonText.setText("-123.2");
			mLatText.setText("44.5");
			save();
			return;
		}
		LocationServices.FusedLocationApi.requestLocationUpdates(gac,lr,ll);
		loc = LocationServices.FusedLocationApi.getLastLocation(gac);
		if (loc != null){
			mLonText.setText(String.valueOf(loc.getLongitude()));
			mLatText.setText(String.valueOf(loc.getLatitude()));
		}
		save();
	}

	private void save() {
		dba.insertTable(mLonText.getText().toString(),
				mLatText.getText().toString(), 
				((EditText)findViewById(R.id.input)).getText().toString());
		((EditText)findViewById(R.id.input)).setText("");
	}

}
