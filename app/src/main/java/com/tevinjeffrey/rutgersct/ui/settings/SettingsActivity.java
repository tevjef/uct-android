package com.tevinjeffrey.rutgersct.ui.settings;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tevinjeffrey.rutgersct.BuildConfig;
import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.RutgersCTApp;
import com.tevinjeffrey.rutgersct.ui.utils.AppCompatPreferenceActivity;
import com.tevinjeffrey.rutgersct.utils.PreferenceUtils;
import com.tevinjeffrey.rutgersct.utils.Utils;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import javax.inject.Inject;

public class SettingsActivity extends AppCompatPreferenceActivity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.base_preference);
    Utils.setWindowColor(ContextCompat.getColor(this, R.color.primary_dark), this);
    setToolbar();
    getFragmentManager()
        .beginTransaction()
        .replace(R.id.content_frame, new SettingsFragment())
        .commit();
  }

  private void setToolbar() {
    Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setNavigationOnClickListener(v -> onBackPressed());

    ActionBar actionBar = getSupportActionBar();

    if (actionBar != null) {
      actionBar.setTitle(getString(R.string.settings));
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setDisplayShowHomeEnabled(true);
    }
    toolbar.setTitleTextAppearance(this, R.style.ToolbarTitleStyle);
    toolbar.setSubtitleTextAppearance(this, R.style.ToolbarSubtitleStyle_TextApperance);
  }

  public static class SettingsFragment extends PreferenceFragment {
    @Inject
    PreferenceUtils mPreferenceUtils;

    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      RutgersCTApp.getObjectGraph(getParentActivity()).inject(this);

      addPreferencesFromResource(R.xml.settings);
    }

    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState) {
      setupAboutPref();
      setupLicensesPref();

      return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
      ListView list = (ListView) getView().findViewById(android.R.id.list);
      list.setDivider(new ColorDrawable(Color.parseColor("#f5f5f5")));
      list.setDividerHeight((int) getParentActivity().getResources().getDisplayMetrics().density);

      super.onActivityCreated(savedInstanceState);
    }

    private int getInterval() {
      return mPreferenceUtils.getSyncInterval();
    }

    private void setInterval(int intervalIndex) {
      mPreferenceUtils.setSyncInterval(intervalIndex);
    }

    private SettingsActivity getParentActivity() {
      return (SettingsActivity) getActivity();
    }

    private void setSummary(Preference preference, CharSequence summary) {
      preference.setSummary(summary);
    }

    private void setupAboutPref() {
      final Preference about = findPreference("about");
      about.setSummary("v" + BuildConfig.VERSION_NAME);
      about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {

          TextView tv = new TextView(getParentActivity());
          tv.setMovementMethod(new LinkMovementMethod());
          tv.setLinkTextColor(ContextCompat.getColor(getParentActivity(), R.color.accent));
          tv.setTextSize(18);
          tv.setTextColor(ContextCompat.getColor(getParentActivity(), R.color.secondary_text));
          tv.setText(Html.fromHtml("Designed and developed by <b> Tevin Jeffrey</b> <br><br>" +
              "<a href=\"http://tevinjeffrey.com/\">Website</a>"
              + "       <a href=\"mailto:tev.jeffrey@gmail.com\">Email</a>"));

          new MaterialDialog.Builder(getParentActivity())
              .title(getResources().getString(R.string.application_name))
              .iconRes(R.mipmap.ic_launcher)
              .positiveColorRes(R.color.accent)
              .customView(tv, true)
              .positiveText("OK")
              .show();
          return true;
        }
      });
    }

    private void setupLicensesPref() {
      Preference licenses = findPreference("licenses");
      licenses.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
          Gson gson = new GsonBuilder()
              .setPrettyPrinting()
              .disableHtmlEscaping()
              .create();

          String licenses = null;
          try {
            licenses = Utils.parseResource(getParentActivity(), R.raw.open_source_licenses);
          } catch (IOException e) {
            e.printStackTrace();
          }
          LinearLayout linearLayout = new LinearLayout(getParentActivity());
          linearLayout.setOrientation(LinearLayout.VERTICAL);
          linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
              ViewGroup.LayoutParams.MATCH_PARENT,
              ViewGroup.LayoutParams.WRAP_CONTENT
          ));
          Type listType = new TypeToken<List<License>>() {
          }.getType();
          List<License> licenseList = gson.fromJson(licenses, listType);
          for (final License license : licenseList) {
            View licenseView =
                LayoutInflater.from(getParentActivity()).inflate(R.layout.license, null);
            TextView name = ButterKnife.findById(licenseView, R.id.name);
            TextView author = ButterKnife.findById(licenseView, R.id.author);
            TextView content = ButterKnife.findById(licenseView, R.id.content);
            View openInBrowser = ButterKnife.findById(licenseView, R.id.open_in_browser);
            openInBrowser.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                Utils.openLink(getParentActivity(), license.getWebsite());
              }
            });
            name.setText(license.getName());
            author.setText(license.getAuthor());
            content.setText(license.getContent());
            linearLayout.addView(licenseView);
          }

          new MaterialDialog.Builder(getParentActivity())
              .title("Open Source Licenses")
              .titleColor(ContextCompat.getColor(getParentActivity(), R.color.primary))
              .positiveText("Ok")
              .positiveColor(ContextCompat.getColor(getParentActivity(), R.color.primary))
              .customView(linearLayout, true)
              .show();

          return true;
        }
      });
    }
  }
}
