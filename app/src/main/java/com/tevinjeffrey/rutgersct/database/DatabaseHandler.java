package com.tevinjeffrey.rutgersct.database;

import com.tevinjeffrey.rutgersct.data.rutgersapi.model.Request;
import java.util.List;
import rx.Observable;

public interface DatabaseHandler {

  void addSectionToDb(Request request);

  Observable<List<Request>> getObservableSections();

  boolean isSectionTracked(Request request);

  void removeSectionFromDb(Request request);
}
