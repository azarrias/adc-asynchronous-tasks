package es.pue.android.asynchronoustasks;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyService extends Service {

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // prevent anyone from binding to it
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String webUrl = intent.getStringExtra(MainActivity.WEB_URL);
        new DownloadWebpageCode().execute();
        return Service.START_NOT_STICKY;
    }

    private class DownloadWebpageCode extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            //txData.setText(s);
            Log.i("SERVICE", s);
        }

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(strings[0])
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    return response.body().string();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "No code";
        }
    }
}
