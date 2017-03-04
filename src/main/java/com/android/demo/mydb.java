package com.android.demo;
import android.util.Log;

import android.widget.ListView;
import android.widget.TextView;
import android.view.View;

import android.view.LayoutInflater;
import android.support.v7.app.AppCompatActivity;

import android.database.Cursor;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteException;
import android.provider.BaseColumns;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;


public class mydb {

	public final SQLiteLocation sql;
	public SQLiteDatabase sqlDB;
	public Cursor sqlCursor;
	public SimpleCursorAdapter sqlCursorAdapter;
	private Context context;
	private LayoutInflater inflater;
	private View v;
	private ListView sqlListView;

	public mydb(Context c, ListView lv) {
		sqlListView = lv;
		context = c;
		sql = new SQLiteLocation(context);

		/*
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = inflater.inflate(R.layout.main, null);
		sqlListView = (ListView) v.findViewById(R.id.sql_list_view);
		*/
	}

	public void close()
	{
		sqlDB.close();
	}

	public void open() throws SQLiteException
	{
		try {
			sqlDB = sql.getWritableDatabase();
		} catch (SQLiteException ex) {
			sqlDB = sql.getReadableDatabase();
		}
	}

	public void insertTable(String lon, String lat, String input){
				if(sqlDB != null){
					ContentValues vals = new ContentValues();
					vals.put(DBContract.LocTable.COL_LON_STRING, lon);
					vals.put(DBContract.LocTable.COL_LAT_STRING, lat);
					vals.put(DBContract.LocTable.COL_INPUT_STRING, input);

					sqlDB.insert(DBContract.LocTable.TABLE_NAME,null,vals);
					populateTable();
				} else {
					Log.d("*****\n", "Unable to access database for writing.");
					//tv.append("unable to access database");
				}
	}

	public void populateTable(){
		if(sqlDB != null) {
			try {
				/*
				if(sqlCursorAdapter != null 
						&& sqlCursorAdapter.getCursor() != null){
					if(!sqlCursorAdapter.getCursor().isClosed()){
						sqlCursorAdapter.getCursor().close();
					}
				}
				*/
				sqlCursor = sqlDB.query(DBContract.LocTable.TABLE_NAME,
						new String[]{
							DBContract.LocTable._ID,
							DBContract.LocTable.COL_LON_STRING,
							DBContract.LocTable.COL_LAT_STRING,
							DBContract.LocTable.COL_INPUT_STRING},
							//DBContract.DemoTable.COLUMN_NAME_DEMO_INT}, 
						null,null, null, null, null);


			//inflater = ((AppCompatActivity) context).getLayoutInflater();
			//View v = inflater.inflate(R.layout.main, null, false); // such as R.layout.activity_main

			sqlCursorAdapter = new SimpleCursorAdapter(context,
					R.layout.sql_item,
					sqlCursor,
					new String[]{DBContract.LocTable.COL_LON_STRING,
						DBContract.LocTable.COL_LAT_STRING,
						DBContract.LocTable.COL_INPUT_STRING},
						new int[]{R.id.sql_lon, R.id.sql_lat, R.id.sql_string},
						0);

				sqlListView.setAdapter(sqlCursorAdapter);
			} catch (Exception e) {
				Log.d("*****\n", "Error loading data from database");
				//tv.append("Error loading data");
			}
		}
	}

}
