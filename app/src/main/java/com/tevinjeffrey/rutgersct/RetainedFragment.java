package com.tevinjeffrey.rutgersct;

import android.app.Fragment;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;


public class RetainedFragment extends Fragment {

    private Map<String, Observable<?>> retainedObservables;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        retainedObservables = new HashMap<>();

    }

    public void putObservable(String key, Observable<?> observable) {
        retainedObservables.put(key, observable);
    }

    public Observable<?> getObservable(String key) {
        return retainedObservables.get(key);
    }
}
