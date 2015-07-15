package com.tevinjeffrey.rutgersct.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dlazaro66.wheelindicatorview.WheelIndicatorItem;
import com.dlazaro66.wheelindicatorview.WheelIndicatorView;
import com.tevinjeffrey.rmp.professor.Professor;
import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Course;
import com.tevinjeffrey.rutgersct.rutgersapi.utils.UrlUtils;

import butterknife.ButterKnife;

public class RatingLayoutInflater {
    Professor mProfessor;
    Activity mContext;

    public RatingLayoutInflater(@NonNull Activity context, @NonNull Professor professor) {
        this.mProfessor = professor;
        this.mContext = context;
    }

    @MainThread
    public ViewGroup getLayout() {
        ViewGroup root = (ViewGroup) mContext.getLayoutInflater().inflate(R.layout.section_info_rmp_rating, null);

        setName(root);

        setDepartment(root);

        setOverall(root);

        setEasiness(root);

        setClarity(root);

        setHelpfulness(root);

        setAverageGrade(root);

        tagView(root);

        return root;
    }

    private void tagView(ViewGroup root) {
        root.setTag("http://www.ratemyprofessors.com" + mProfessor.getUrl());
    }

    public static ViewGroup getErrorLayout(Context context, String professorName, Course.Sections s) {
       ViewGroup root = (ViewGroup) ((LayoutInflater)context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.no_professor, null);

        String url = "http://www.google.com/#q=" + UrlUtils.getGoogleUrl(s);
        root.setTag(url);

        TextView message = ButterKnife.findById(root, R.id.message);
        message.setText("Could not find professor: " + professorName);
        return root;
    }

    public void setName(ViewGroup root) {
        String professorName = mProfessor.getFullName().toString();
        TextView professorNameText = ButterKnife.findById(root, R.id.rmp_prof_name);
        professorNameText.setText(professorName);
    }

    public void setDepartment(ViewGroup root) {
        String professorDepartment = mProfessor.getDepartment();
        TextView professorDepartmentText = ButterKnife.findById(root, R.id.rmp_department);
        professorDepartmentText.setText(professorDepartment + " - " + mProfessor.getLocation());
    }

    public void setAverageGrade(ViewGroup root) {
        String averageGrade = mProfessor.getRatings().getAverageGrade().toString();
        TextView averageGradeText = ButterKnife.findById(root, R.id.rmp_average_grade_text);
        averageGradeText.setText(averageGrade);
    }

    public void setHelpfulness(ViewGroup root) {
        String helpfulnessRating = mProfessor.getRatings().getHelpfulness().getRating();
        double rating = Double.valueOf(helpfulnessRating)/5;
        double percentage = rating * 100;
        WheelIndicatorView helpfulnessWheel = ButterKnife.findById(root, R.id.wheel_helpfullness_rating);
        helpfulnessWheel.setFilledPercent((int) percentage);
        helpfulnessWheel.addWheelIndicatorItem(getItem(percentage));
        helpfulnessWheel.startItemsAnimation();
    }

    public void setClarity(ViewGroup root) {
        String clarityRating = mProfessor.getRatings().getClarity().getRating();
        double rating = Double.valueOf(clarityRating)/5;
        double percentage = rating * 100;
        WheelIndicatorView clarityWheel = ButterKnife.findById(root, R.id.wheel_clarity_rating);
        clarityWheel.setFilledPercent((int) percentage);
        clarityWheel.addWheelIndicatorItem(getItem(percentage));
        clarityWheel.startItemsAnimation();
    }

    public void setEasiness(ViewGroup root) {
        String easinessRating = mProfessor.getRatings().getEasiness().getRating();
        double rating = Double.valueOf(easinessRating)/5;
        double percentage = rating * 100;
        WheelIndicatorView easinessWheel = ButterKnife.findById(root, R.id.wheel_easiness_rating);
        easinessWheel.setFilledPercent((int) percentage);
        easinessWheel.addWheelIndicatorItem(getItem(percentage));
        easinessWheel.startItemsAnimation();
    }

    public void setOverall(ViewGroup root) {
        String overallRating = mProfessor.getRatings().getOverallQuality().getRating();
        double rating = Double.valueOf(overallRating)/5;
        double percentage = rating * 100;
        WheelIndicatorView overallQualityWheel = ButterKnife.findById(root, R.id.wheel_quality_rating);
        TextView overallQualityText = ButterKnife.findById(root, R.id.rmp_overall_rating_number);
        overallQualityText.setText(overallRating);
        overallQualityWheel.setFilledPercent((int) percentage);
        overallQualityWheel.addWheelIndicatorItem(getItem(percentage));
        overallQualityWheel.startItemsAnimation();
    }

    private int getRatingColor(double rating) {
        if (rating < 40) {
            return mContext.getResources().getColor(R.color.rating_low);
        } else if (rating < 60) {
            return mContext.getResources().getColor(R.color.rating_medium);
        } else {
            return mContext.getResources().getColor(R.color.rating_high);
        }
    }

    private WheelIndicatorItem getItem(double percentage) {
        return new WheelIndicatorItem(1.8f, getRatingColor(percentage));
    }
}
