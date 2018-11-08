package es.pue.android.asynchronoustasks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    TextView txData;
    static final String WEB_URL = "webUrl";
    BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txData = (TextView)findViewById(R.id.tvCode);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String htmlCode = intent.getStringExtra("html");
                txData.setText(htmlCode);
            }
        };
    }

    public void loadCode(View view) {
//        new DownloadWebpageCode().execute("https://developer.android.com/");
//        Intent i = new Intent(this, MyIntentService.class);
        Intent i = new Intent(this, MyService.class);
        i.putExtra(WEB_URL, "https://developer.android.com/");
        startService(i);
    }

    private class DownloadWebpageCode extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            txData.setText(s);
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

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter("es.pue.android.asynchronoustasks.DATA_OK"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }
}
