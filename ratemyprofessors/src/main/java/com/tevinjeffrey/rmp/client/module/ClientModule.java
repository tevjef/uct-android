package com.tevinjeffrey.rmp.client.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.tevinjeffrey.rmp.client.ClientService;
import com.tevinjeffrey.rmp.client.RMPClient;

import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

@Module(    complete = false,
        library = true )

public class ClientModule {

    private static final long CONNECT_TIMEOUT_MILLIS = 6000;
    private static final long READ_TIMEOUT_MILLIS = 7000;

    @Provides
    @Singleton
    public RMPClient providesRMPClient(OkHttpClient client, Gson gson) {
        OkHttpClient okClient = client.clone();

        okClient.setConnectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        okClient.setReadTimeout(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://rutgersapp.tevindev.me:8080/")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gson))
                .setClient(new OkClient(okClient))
                .build();

        return new RMPClient(restAdapter.create(ClientService.class));
    }
}
