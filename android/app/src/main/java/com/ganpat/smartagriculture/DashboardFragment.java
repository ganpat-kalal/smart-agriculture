package com.ganpat.smartagriculture;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private static final String TAG = "UsingThingspeakAPI";
    private static final String THINGSPEAK_CHANNEL_ID = "552505";
    private static final String THINGSPEAK_API_KEY = "M602T7UOPF8HLCRA"; //GARBAGE KEY
    private static final String THINGSPEAK_API_KEY_STRING = "M602T7UOPF8HLCRA";
    /* Be sure to use the correct fields for your own app*/
    private static final String THINGSPEAK_FIELD1 = "field1";
    private static final String THINGSPEAK_FIELD2 = "field2";
    private static final String THINGSPEAK_FIELD3 = "field3";
    private static final String THINGSPEAK_FIELD4 = "field4";
    private static final String THINGSPEAK_UPDATE_URL = "https://api.thingspeak.com/update?";
    private static final String THINGSPEAK_CHANNEL_URL = "https://api.thingspeak.com/channels/";
    private static final String THINGSPEAK_FEEDS_LAST = "/feeds/last?";

     List<Data> dataList;

     RecyclerView recyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;

    public DashboardFragment() {
        try {
            new FetchThingspeakTask().execute();
        }
        catch(Exception e){
            Log.e("ERROR", e.getMessage(), e);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Context context = getActivity().getApplicationContext();

        final View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        try {
            new FetchThingspeakTask().execute();

        }
        catch(Exception e){
            Log.e("ERROR", e.getMessage(), e);
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    new FetchThingspeakTask().execute();
                    NotificationCompat.Builder builder =
                            new NotificationCompat.Builder(context)
                                    .setSmallIcon(R.drawable.message)
                                    .setContentTitle("Data Refreshed.")   //this is the title of notification
                                    .setContentText("You have an latest data.");   //this is the message showed in notification

                    Intent intent = new Intent(context, DashboardFragment.class);
                    PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(contentIntent);

                    // Add as notification
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(0, builder.build());

                }
                catch(Exception e){
                    Log.e("ERROR", e.getMessage(), e);
                }
            }
        });

        return rootView;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Dashboard");
    }

    class FetchThingspeakTask extends AsyncTask<String, Void, String> {
        //protected void onPreExecute() {
        //  t2.setText("Fetching Data from Server.Please Wait...");
        //}
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL("http://184.106.153.149/channels/" + THINGSPEAK_CHANNEL_ID +
                        THINGSPEAK_FEEDS_LAST + THINGSPEAK_API_KEY_STRING + "=" +
                        THINGSPEAK_API_KEY + "");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }
        protected void onPostExecute(String response) {

            Context context = getActivity().getApplicationContext();

            if(response == null) {
                Toast.makeText(context, "Please check your internet connectivity.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                JSONObject channel = (JSONObject) new JSONTokener(response).nextValue();
                double v1 = channel.getDouble(THINGSPEAK_FIELD1);
                String f1 = String.valueOf(v1);
                double v2 = channel.getDouble(THINGSPEAK_FIELD2);
                String f2 = String.valueOf(v2);
                double v3 = channel.getDouble(THINGSPEAK_FIELD3);
                String f3 = String.valueOf(v3);
//                Integer v4 = channel.getInt("field4");
//                String f4 = String.valueOf(v4);
                //t1.setText(f1);
                //txthumidity.setText(f2);
                //txtmoisture.setText(f3);

                //initializing the productlist
                dataList = new ArrayList<>();


                //adding some items to our list
                dataList.add(
                        new Data(
                                1,
                                "Temperature ",
                                "Temperature Sensor Data",
                                f1,
                                R.mipmap.ic_tem
                        ));

                dataList.add(
                        new Data(
                                1,
                                "Humidity",
                                "Humidity Sensor Data",
                                f2,
                                R.mipmap.ic_humidity
                        ));

                dataList.add(
                        new Data(
                                1,
                                "Soil Moisture",
                                "Soil Moisture Data",
                                f3,
                                R.mipmap.ic_tem
                        ));

//                dataList.add(
//                        new Data(
//                                1,
//                                "Soil Moisture 2",
//                                "Before soil moisture data",
//                                "30",
//                                R.mipmap.ic_leaf));

                dataList.add(
                        new Data(
                                1,
                                "Water Pump",
                                "Water pump On/Off",
                                "Off",
                                R.mipmap.ic_water));

                //creating recyclerview adapter
                DataAdapter adapter = new DataAdapter(context, dataList);

                //setting adapter to recyclerview
                recyclerView.setAdapter(adapter);
                mSwipeRefreshLayout.setRefreshing(false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
