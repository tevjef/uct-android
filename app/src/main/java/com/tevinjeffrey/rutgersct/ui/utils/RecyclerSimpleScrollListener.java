package com.tevinjeffrey.rutgersct.ui.utils;

import android.support.v7.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class RecyclerSimpleScrollListener extends RecyclerView.OnScrollListener {
  private static final int SCROLL_TOLERANCE = 0;
  private final PublishSubject<Direction> scrollBus;

  public RecyclerSimpleScrollListener() {
    scrollBus = PublishSubject.create();
  }

  @Override
  public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
    super.onScrolled(recyclerView, dx, dy);
    if (2 < dy) {
      scrollBus.onNext(Direction.DOWN);
    } else if (-2 > dy) {
      scrollBus.onNext(Direction.UP);
    } else if (0 == dy) {
      scrollBus.onNext(Direction.NEUTRAL);
    }
  }

  @Override
  public String toString() {
    return "RecyclerSimpleScrollListener";
  }

  public Observable<Direction> getDirectionObservable() {
    return scrollBus.distinctUntilChanged();
  }

  public enum Direction {
    UP,
    DOWN,
    NEUTRAL
  }
}