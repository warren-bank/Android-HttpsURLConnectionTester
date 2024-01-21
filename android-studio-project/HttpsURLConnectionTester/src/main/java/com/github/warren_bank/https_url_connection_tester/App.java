package com.github.warren_bank.https_url_connection_tester;

import android.app.Application;

import javax.net.ssl.HttpsURLConnection;

public class App extends Application {
  @Override
  public void onCreate() {
    super.onCreate();

    try {
      SSLSocketFactoryCompat socketFactory = new SSLSocketFactoryCompat();

      HttpsURLConnection.setDefaultSSLSocketFactory(socketFactory);
    }
    catch(Exception e) {}
  }
}
