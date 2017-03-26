package com.tevinjeffrey.rutgersct.ui.base;

import android.os.Bundle;
import android.support.annotation.NonNull;

public interface StatefulPresenter {

  void onActivityCreated(Bundle savedInstanceState);

  void onPause();

  void onResume();

  void onSaveInstanceState(@NonNull Bundle bundle);
}