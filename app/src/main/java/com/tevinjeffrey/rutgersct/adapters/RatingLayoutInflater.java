package com.tevinjeffrey.rutgersct.adapters;

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

import com.dlazaro66.wheelindicatorview.WheelIndicatorItem;
import com.dlazaro66.wheelindicatorview.WheelIndicatorView;
import com.tevinjeffrey.rmp.common.Professor;
import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Course;
import com.tevinjeffrey.rutgersct.rutgersapi.utils.UrlUtils;

import butterknife.ButterKnife;

public class RatingLayoutInflater {
    public static final int LOW_RATING_LIMIT = 40;
    public static final int MEDIUM_RATING_LIMIT = 60;
    private final Professor mProfessor;
    private final Context mContext;

    public RatingLayoutInflater(@NonNull Activity context, @NonNull Professor professor) {
        this.mProfessor = professor;
        this.mContext = context;
    }

    @MainThread
    public ViewGroup getProfessorLayout() {
        ViewGroup root = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.section_info_rmp_rating, null);
        setOpenInBrowser(root);
        setName(root);
        setSubtitle(root);
        setOverall(root);
        setEasiness(root);
        setClarity(root);
        setHelpfulness(root);
        setAverageGrade(root);
        setRatingCount(root);
        tagView(root);

        return root;
    }

    private void tagView(ViewGroup root) {
        root.setTag("http://www.ratemyprofessors.com" + mProfessor.getRating().getRatingUrl());
    }

    public View getErrorLayout(String professorName, Course.Section s) {
        TextView message = (TextView) ((LayoutInflater) mContext.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.no_professor, null);

        String url = "http://www.google.com/#q=" + UrlUtils.getGoogleUrl(s);
        message.setTag(url);

        message.setText(mContext.getString(R.string.could_not_find_professor) + professorName);
        return message;
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


    private void setName(ViewGroup root) {
        String professorName = mProfessor.getFirstName() + " " + mProfessor.getLastName();
        TextView professorNameText = ButterKnife.findById(root, R.id.rmp_prof_name);
        professorNameText.setText(professorName);
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

    private String append(String str) {
        return " - " + str;
    }

    private void setAverageGrade(ViewGroup root) {
        String averageGrade = mProfessor.getRating().getAverageGrade();
        TextView averageGradeText = ButterKnife.findById(root, R.id.rmp_average_grade_text);
        averageGradeText.setText(averageGrade);
    }

    private void setRatingCount(ViewGroup root) {
        String ratingCount = String.valueOf(mProfessor.getRating().getRatingsCount());
        TextView ratingCountText = ButterKnife.findById(root, R.id.rmp_rating_count_text);
        ratingCountText.setText(ratingCount);
    }

    private void setHelpfulness(ViewGroup root) {
        double rating = mProfessor.getRating().getHelpfulness() / 5;
        double percentage = rating * 100;
        WheelIndicatorView helpfulnessWheel = ButterKnife.findById(root, R.id.wheel_helpfullness_rating);
        helpfulnessWheel.setFilledPercent((int) percentage);
        helpfulnessWheel.addWheelIndicatorItem(getItem(percentage));
        helpfulnessWheel.startItemsAnimation();
    }

    private void setClarity(ViewGroup root) {
        double rating = mProfessor.getRating().getClarity() / 5;
        double percentage = rating * 100;
        WheelIndicatorView clarityWheel = ButterKnife.findById(root, R.id.wheel_clarity_rating);
        clarityWheel.setFilledPercent((int) percentage);
        clarityWheel.addWheelIndicatorItem(getItem(percentage));
        clarityWheel.startItemsAnimation();
    }

    private void setEasiness(ViewGroup root) {
        double rating = mProfessor.getRating().getEasiness() / 5;
        double percentage = rating * 100;
        WheelIndicatorView easinessWheel = ButterKnife.findById(root, R.id.wheel_easiness_rating);
        easinessWheel.setFilledPercent((int) percentage);
        easinessWheel.addWheelIndicatorItem(getItem(percentage));
        easinessWheel.startItemsAnimation();
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

    private int getRatingColor(double rating) {
        if (rating < LOW_RATING_LIMIT) {
            return ContextCompat.getColor(mContext, R.color.rating_low);
        } else if (rating < MEDIUM_RATING_LIMIT) {
            return ContextCompat.getColor(mContext, R.color.rating_medium);
        } else {
            return ContextCompat.getColor(mContext, R.color.rating_high);
        }
    }

    private WheelIndicatorItem getItem(double percentage) {
        return new WheelIndicatorItem(1.8f, getRatingColor(percentage));
    }
}
