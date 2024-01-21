package com.github.warren_bank.https_url_connection_tester;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class MainActivity extends Activity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    TextView urlField = (TextView) findViewById(R.id.urlField);
    Button testGetButton = (Button) findViewById(R.id.testGetButton);
    Button testPostButton = (Button) findViewById(R.id.testPostButton);

    testGetButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View arg0) {
        testGetInThread(
          urlField.getText().toString().trim()
        );
      }
    });

    testPostButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View arg0) {
        testPostInThread(
          urlField.getText().toString().trim()
        );
      }
    });
  }

  private void testGetInThread(String url) {
    Runnable runnable = new Runnable() {
      public void run() {
        testGet(url);
      }
    };
    Thread thread = new Thread(runnable);
    thread.start();
  }

  private void testPostInThread(String url) {
    Runnable runnable = new Runnable() {
      public void run() {
        testPost(url);
      }
    };
    Thread thread = new Thread(runnable);
    thread.start();
  }

  private void testGet(String url) {
    try {
      HttpURLConnection client = (HttpURLConnection) (new URL(url)).openConnection();
      configHttpURLConnection(client);

      client.setRequestMethod("GET");
      client.setDoOutput(false);
      client.setDoInput(false);

      showResponseCode(client);
    }
    catch(Exception ex) {
      makeText(ex.toString());
    }
  }

  private void testPost(String url) {
    try {
      HttpURLConnection client = (HttpURLConnection) (new URL(url)).openConnection();
      configHttpURLConnection(client);

      client.setRequestMethod("POST");
      client.setRequestProperty("Content-Type", "text/plain");
      client.setDoOutput(true);
      client.setDoInput(false);

      OutputStream os = client.getOutputStream();
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
      writer.write("hello, world!");
      writer.flush();
      os.close();

      showResponseCode(client);
    }
    catch(Exception ex) {
      makeText(ex.toString());
    }
  }

  private void showResponseCode(HttpURLConnection client) throws Exception {
    int status_code = client.getResponseCode();

    makeText(
      String.format("response status code: %d", status_code)
    );
  }

  private void configHttpURLConnection(HttpURLConnection client) {
    client.setConnectTimeout(30000); // 30 secs
    client.setRequestProperty("Accept-Language", Locale.getDefault().getLanguage());
    client.setRequestProperty("User-Agent", "HttpsURLConnectionTester/" + BuildConfig.VERSION_NAME + " " + System.getProperty("http.agent"));
  }

  private void makeText(String msg) {
    runOnUiThread(new Runnable() {
      public void run() {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
      }
    });
  }

}
