package com.ganpat.smartagriculture;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

/**
 * Created by anupamchugh on 10/12/15.
 */
public class PumpFragment extends Fragment {

    //ToggleButton toggle;
    Button btnon, btnoff;

    public PumpFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_pump, container, false);
        final Context context = getActivity().getApplicationContext();

//        toggle = (ToggleButton)rootView.findViewById(R.id.toggle);
        btnon = (Button) rootView.findViewById(R.id.btnon);
        btnoff = (Button) rootView.findViewById(R.id.btnoff);
        //Toast.makeText(context, toggle.getText(), Toast.LENGTH_LONG).show();

        btnon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    try {
                        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){
                            public boolean verify(String hostname, SSLSession session) {
                                return true;
                            }});
                        SSLContext contextt = SSLContext.getInstance("TLS");
                        contextt.init(null, new X509TrustManager[]{new X509TrustManager(){
                            public void checkClientTrusted(X509Certificate[] chain,
                                                           String authType) throws CertificateException {}
                            public void checkServerTrusted(X509Certificate[] chain,
                                                           String authType) throws CertificateException {}
                            public X509Certificate[] getAcceptedIssuers() {
                                return new X509Certificate[0];
                            }}}, new SecureRandom());
                        HttpsURLConnection.setDefaultSSLSocketFactory(
                                contextt.getSocketFactory());
                    } catch (Exception e) { // should never happen
                        e.printStackTrace();
                    }
//                    URL url = new URL("https://api.thingspeak.com/update?api_key=K48ADJW7GSXEYQGV&field4=1");
//                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    String mylink = "https://api.thingspeak.com/update?api_key=QFVJ5HB3YC8S46R3&field1=1";

                    HttpAsyncTask hat = new HttpAsyncTask();
                    hat.execute(mylink);

                    NotificationCompat.Builder builder =
                            new NotificationCompat.Builder(context)
                                    .setSmallIcon(R.drawable.message)
                                    .setContentTitle("Water Pump On")   //this is the title of notification
                                    .setContentText("Water pump is on now.");   //this is the message showed in notification

                    Intent intent = new Intent(context, DashboardFragment.class);
                    PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(contentIntent);

                    // Add as notification
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(0, builder.build());

                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage(), e);
                }
            }
        });
        btnoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    try {
                        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){
                            public boolean verify(String hostname, SSLSession session) {
                                return true;
                            }});
                        SSLContext contextt = SSLContext.getInstance("TLS");
                        contextt.init(null, new X509TrustManager[]{new X509TrustManager(){
                            public void checkClientTrusted(X509Certificate[] chain,
                                                           String authType) throws CertificateException {}
                            public void checkServerTrusted(X509Certificate[] chain,
                                                           String authType) throws CertificateException {}
                            public X509Certificate[] getAcceptedIssuers() {
                                return new X509Certificate[0];
                            }}}, new SecureRandom());
                        HttpsURLConnection.setDefaultSSLSocketFactory(
                                contextt.getSocketFactory());
                    } catch (Exception e) { // should never happen
                        e.printStackTrace();
                    }
//                    URL url = new URL("https://api.thingspeak.com/update?api_key=K48ADJW7GSXEYQGV&field4=1");
//                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    String mylink = "https://api.thingspeak.com/update?api_key=QFVJ5HB3YC8S46R3&field1=0";

                    HttpAsyncTask hat = new HttpAsyncTask();
                    hat.execute(mylink);

                    NotificationCompat.Builder builder =
                            new NotificationCompat.Builder(context)
                                    .setSmallIcon(R.drawable.message)
                                    .setContentTitle("Water Pump Off")   //this is the title of notification
                                    .setContentText("Water pump is off now.");   //this is the message showed in notification

                    Intent intent = new Intent(context, DashboardFragment.class);
                    PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(contentIntent);

                    // Add as notification
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(0, builder.build());

                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage(), e);
                }
            }
        });

//        toggle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Context context = getActivity().getApplicationContext();
//                if(toggle.getText() != "") {
//                    Toast.makeText(context, "Water pump is " + toggle.getText() + ".", Toast.LENGTH_SHORT).show();
//                }n
//                if(toggle.isChecked()){
//                    try {
//                        URL url = new URL("https://api.thingspeak.com/update?api_key=K48ADJW7GSXEYQGV&field4=1");
//                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
////                        new SendThingspeakTask().execute();
////                        URL url = new URL("https://api.thingspeak.com/update?api_key=K48ADJW7GSXEYQGV&field4=1");
////                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                    }catch(Exception e) {
//                        Log.e("ERROR", e.getMessage(), e);
//                    }
//
//
//                }
//            }
//        });

        getActivity().setTitle("Pump");
        return rootView;
    }

    class SendThingspeakTask extends AsyncTask<String, Void, String> {
        //protected void onPreExecute() {
        //  t2.setText("Fetching Data from Server.Please Wait...");
        //}
        protected String doInBackground(String... urls) {

            try {
                String message = URLEncoder.encode("my message", "UTF-8");

                URL url = new URL("https://api.thingspeak.com/update?api_key=K48ADJW7GSXEYQGV&field4=1");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(
                        urlConnection.getOutputStream());
                writer.write("message=" + message);
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
            return null;
        }

        protected void onPostExecute(String response) {
            Context context = getActivity().getApplicationContext();

            if (response == null) {
                Toast.makeText(context, "Please check your internet connectivity.", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            return httpRequestResponse(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

        }
    }

    //For HttpAsync Functions: sending requests and receiving responses
    public static String httpRequestResponse(String url) {
        InputStream inputStream = null;
        String result = "";
        try {
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert InputStream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "InputStream did not work";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }
}

