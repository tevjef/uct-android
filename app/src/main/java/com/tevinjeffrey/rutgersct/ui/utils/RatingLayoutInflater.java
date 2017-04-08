package com.tevinjeffrey.rutgersct.ui.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.dlazaro66.wheelindicatorview.WheelIndicatorItem;
import com.dlazaro66.wheelindicatorview.WheelIndicatorView;
import com.tevinjeffrey.rmp.common.Professor;
import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.data.rutgersapi.utils.UrlUtils;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Section;

public class RatingLayoutInflater {
  public static final int LOW_RATING_LIMIT = 40;
  public static final int MEDIUM_RATING_LIMIT = 60;
  private final Professor mProfessor;
  private final Context mContext;

  public RatingLayoutInflater(@NonNull Activity context, @NonNull Professor professor) {
    this.mProfessor = professor;
    this.mContext = context;
  }

  public View getErrorLayout(String professorName, Section s) {
    TextView message = (TextView) ((LayoutInflater) mContext.getSystemService
        (Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.no_professor, null);

    String url = "http://www.google.com/#q=" + UrlUtils.getGoogleUrl(s);
    message.setTag(url);

    message.setText(mContext.getString(R.string.could_not_find_professor) + professorName);
    return message;
  }

  @MainThread
  public ViewGroup getProfessorLayout() {
    ViewGroup root =
        (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.section_info_rmp_rating, null);
    setOpenInBrowser(root);
    setName(root);
    setSubtitle(root);
    setOverall(root);
    setEasiness(root);
    tagView(root);

    return root;
  }

  private String append(String str) {
    return " - " + str;
  }

  private WheelIndicatorItem getItem(double percentage) {
    return getItem(1.0f, percentage);
  }

  private WheelIndicatorItem getItem(float weight, double percentage) {
    return new WheelIndicatorItem(weight, getRatingColor(percentage));
  }

  private int getRatingColor(double rating) {
    if (rating < LOW_RATING_LIMIT) {
      return ContextCompat.getColor(mContext, R.color.rating_low);
    } else if (rating < MEDIUM_RATING_LIMIT) {
      return ContextCompat.getColor(mContext, R.color.rating_medium);
    } else {
      return ContextCompat.getColor(mContext, R.color.rating_high);
    }
  }

  private void setEasiness(ViewGroup root) {
    double rating = Math.abs(mProfessor.getRating().getEasiness() - 5) / 5;
    double percentage = rating * 100;
    WheelIndicatorView easinessWheel = ButterKnife.findById(root, R.id.wheel_easiness_rating);
    TextView overallEasinessText = ButterKnife.findById(root, R.id.rmp_easiness_rating_number);
    overallEasinessText.setText(String.valueOf(
        Math.round(Math.abs(mProfessor.getRating().getEasiness() - 5) * 100.0) / 100.0));
    easinessWheel.setFilledPercent((int) percentage);
    easinessWheel.addWheelIndicatorItem(getItem(percentage));
    easinessWheel.startItemsAnimation();
  }

  private void setName(ViewGroup root) {
    String professorName = mProfessor.getFirstName() + " " + mProfessor.getLastName();
    TextView professorNameText = ButterKnife.findById(root, R.id.rmp_prof_name);
    professorNameText.setText(professorName);
  }

  private void setOpenInBrowser(ViewGroup root) {
    View openInBrowser = ButterKnife.findById(root, R.id.open_in_browser);
    openInBrowser.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String url = mProfessor.getRating().getFullRatingUrl();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        mContext.startActivity(i);
      }
    });
  }

  private void setOverall(ViewGroup root) {
    double rating = mProfessor.getRating().getOverall() / 5;
    double percentage = rating * 100;
    WheelIndicatorView overallQualityWheel = ButterKnife.findById(root, R.id.wheel_quality_rating);
    TextView overallQualityText = ButterKnife.findById(root, R.id.rmp_overall_rating_number);
    overallQualityText.setText(String.valueOf(mProfessor.getRating().getOverall()));
    overallQualityWheel.setFilledPercent((int) percentage);
    overallQualityWheel.addWheelIndicatorItem(getItem(percentage));
    overallQualityWheel.startItemsAnimation();
  }

  private void setSubtitle(ViewGroup root) {
    String professorDepartment = mProfessor.getDepartment();
    TextView professorDepartmentText = ButterKnife.findById(root, R.id.rmp_subtitle);
    String str = professorDepartment;
    if (mProfessor.getTitle() != null) {
      str += append(mProfessor.getTitle());
    }
    if (mProfessor.getLocation().getCity() != null) {
      str += append(mProfessor.getLocation().getCity());
    }
    professorDepartmentText.setText(str);
  }

  private void tagView(ViewGroup root) {
    root.setTag("http://www.ratemyprofessors.com" + mProfessor.getRating().getRatingUrl());
  }
}
