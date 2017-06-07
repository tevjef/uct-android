package com.tevinjeffrey.rutgersct.ui.chooser;

import com.tevinjeffrey.rutgersct.data.uctapi.RetroUCT;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Semester;
import com.tevinjeffrey.rutgersct.data.uctapi.model.University;
import com.tevinjeffrey.rutgersct.ui.base.BasePresenter;
import com.tevinjeffrey.rutgersct.utils.AndroidMainThread;
import com.tevinjeffrey.rutgersct.utils.BackgroundThread;
import com.tevinjeffrey.rutgersct.utils.RxUtils;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import java.util.List;
import javax.inject.Inject;
import org.reactivestreams.Subscription;

public class ChooserPresenterImpl extends BasePresenter implements ChooserPresenter {

  private static final String TAG = ChooserPresenterImpl.class.getSimpleName();

  @Inject
  RetroUCT mRetroUCT;
  @Inject
  @AndroidMainThread
  Scheduler mMainThread;
  @Inject
  @BackgroundThread
  Scheduler mBackgroundThread;

  @Inject
  RetroUCT retroUCT;

  private Disposable disposable;
  private boolean isLoading;

  public ChooserPresenterImpl() {

  }

  @Override
  public Semester getDefaultSemester() {
    return retroUCT.getDefaultSemester();
  }

  @Override
  public University getDefaultUniversity() {
    return retroUCT.getDefaultUniversity();
  }

  public ChooserView getView() {
    return (ChooserView) super.getView();
  }

  @Override
  public boolean isLoading() {
    return isLoading;
  }

  @Override
  public void loadAvailableSemesters(String universityTopicName) {
    cancePreviousSubscription();
    disposable = mRetroUCT.getUniversity(universityTopicName)
        .map(university -> university.available_semesters)
        .observeOn(mMainThread)
        .subscribe(semesters -> {
          if (getView() != null) {
            getView().setAvailableSemesters(semesters);
          }
        }, throwable -> {
          if (getView() != null) {
            //Show error in view getView().showError(e);
            throwable.printStackTrace();
          }
        });
  }

  @Override
  public void loadUniversities() {
    mRetroUCT.getUniversities()
        .observeOn(mMainThread)
        .subscribe(universities -> {
          if (getView() != null) {
            getView().setUniversities(universities);
          }
        }, Throwable::printStackTrace);
  }

  @Override
  public void updateDefaultUniversity(University university) {
    retroUCT.setDefaultUniversity(university);
  }

  @Override
  public void updateSemester(Semester semester) {
    retroUCT.setDefaultSemester(semester);
  }

  private void cancePreviousSubscription() {
    RxUtils.disposeIfNotNull(disposable);
  }
}
