package com.tevinjeffrey.rutgersct.ui.chooser;

import com.tevinjeffrey.rutgersct.data.UCTApi;
import com.tevinjeffrey.rutgersct.data.model.Semester;
import com.tevinjeffrey.rutgersct.data.model.University;
import com.tevinjeffrey.rutgersct.ui.base.BasePresenter;
import com.tevinjeffrey.rutgersct.utils.AndroidMainThread;
import com.tevinjeffrey.rutgersct.utils.BackgroundThread;
import com.tevinjeffrey.rutgersct.utils.RxUtils;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import javax.inject.Inject;

public class ChooserPresenterImpl extends BasePresenter implements ChooserPresenter {

  private static final String TAG = ChooserPresenterImpl.class.getSimpleName();

  @Inject UCTApi mUCTApi;
  @Inject @AndroidMainThread Scheduler mMainThread;
  @Inject @BackgroundThread Scheduler mBackgroundThread;
  @Inject UCTApi UCTApi;

  private Disposable disposable;
  private boolean isLoading;

  public ChooserPresenterImpl() { }

  @Override
  public Semester getDefaultSemester() {
    return UCTApi.getDefaultSemester();
  }

  @Override
  public University getDefaultUniversity() {
    return UCTApi.getDefaultUniversity();
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
    disposable = mUCTApi.getUniversity(universityTopicName)
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
    mUCTApi.getUniversities()
        .observeOn(mMainThread)
        .subscribe(universities -> {
          if (getView() != null) {
            getView().setUniversities(universities);
          }
        }, Throwable::printStackTrace);
  }

  @Override
  public void updateDefaultUniversity(University university) {
    UCTApi.setDefaultUniversity(university);
  }

  @Override
  public void updateSemester(Semester semester) {
    UCTApi.setDefaultSemester(semester);
  }

  private void cancePreviousSubscription() {
    RxUtils.disposeIfNotNull(disposable);
  }
}
