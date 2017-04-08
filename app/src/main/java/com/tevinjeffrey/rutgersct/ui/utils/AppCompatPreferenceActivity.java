package com.tevinjeffrey.rutgersct.ui.utils;

import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class AppCompatPreferenceActivity extends PreferenceActivity {
  private AppCompatDelegate mDelegate;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    getDelegate().installViewFactory();
    getDelegate().onCreate(savedInstanceState);
    super.onCreate(savedInstanceState);
  }

  @Override
  protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    getDelegate().onPostCreate(savedInstanceState);
  }

  @Override
  protected void onPostResume() {
    super.onPostResume();
    getDelegate().onPostResume();
  }

  @Override
  protected void onStop() {
    super.onStop();
    getDelegate().onStop();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    getDelegate().onDestroy();
  }

  @Override
  public void addContentView(View view, ViewGroup.LayoutParams params) {
    getDelegate().addContentView(view, params);
  }

  @Override
  public MenuInflater getMenuInflater() {
    return getDelegate().getMenuInflater();
  }

  public void invalidateOptionsMenu() {
    getDelegate().invalidateOptionsMenu();
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    getDelegate().onConfigurationChanged(newConfig);
  }

  @Override
  protected void onTitleChanged(CharSequence title, int color) {
    super.onTitleChanged(title, color);
    getDelegate().setTitle(title);
  }

  @Override
  public void setContentView(View view) {
    getDelegate().setContentView(view);
  }

  @Override
  public void setContentView(View view, ViewGroup.LayoutParams params) {
    getDelegate().setContentView(view, params);
  }

  @Override
  public void setContentView(@LayoutRes int layoutResID) {
    getDelegate().setContentView(layoutResID);
  }

  public ActionBar getSupportActionBar() {
    return getDelegate().getSupportActionBar();
  }

  public void setSupportActionBar(@Nullable Toolbar toolbar) {
    getDelegate().setSupportActionBar(toolbar);
  }

  private AppCompatDelegate getDelegate() {
    if (mDelegate == null) {
      mDelegate = AppCompatDelegate.create(this, null);
    }
    return mDelegate;
  }
}
