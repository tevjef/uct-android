package com.tevinjeffrey.rutgersct.ui;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.animator.EaseOutQuint;
import com.tevinjeffrey.rutgersct.model.Request;
import com.tevinjeffrey.rutgersct.utils.SemesterUtils;
import com.tevinjeffrey.stringpicker.StringPicker;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ChooserFragment extends MainFragment {

    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.semester_radiogroup)
    RadioGroup mSemesterRadiogroup;
    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.location1)
    CheckBox mLocation1;
    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.location2)
    CheckBox mLocation2;
    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.location3)
    CheckBox mLocation3;
    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.level1)
    CheckBox mLevel1;
    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.level2)
    CheckBox mLevel2;
    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.search_button)
    TextView mSearchButton;
    @InjectView(R.id.primarySemester)
    RadioButton mPrimarySemester;
    @InjectView(R.id.secondarySemester)
    RadioButton mSecondarySemester;
    @InjectView(R.id.otherSemester)
    RadioButton mOtherSemester;

    private Toolbar toolbar;

    public ChooserFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity.setPrimaryWindow(getParentActivity());
        setRetainInstance(true);

        final View rootView = inflater.inflate(R.layout.fragment_chooser, container, false);
        ButterKnife.inject(this, rootView);
        setToolbar(rootView);

        final SemesterUtils su = new SemesterUtils(Calendar.getInstance());

        mPrimarySemester.setText(su.getPrimarySemester());
        mPrimarySemester.setTag(su.resolvePrimarySemester());

        mSecondarySemester.setText(su.getSecondarySemester());
        mSecondarySemester.setTag(su.resolveSecondarySemester());

        mOtherSemester.setOnClickListener(new View.OnClickListener() {

            String currentYear = su.resolveCurrentSemester().getYear();
            String currentSeason = su.resolveCurrentSemester().getSeason().toString();

            View pickerRoot;
            @Override
            public void onClick(View v) {
                pickerRoot = createPicker(su);
                new MaterialDialog.Builder(getParentActivity())
                        .title("Choose a semester")
                        .customView(pickerRoot, false)
                        .positiveText("Done")
                        .negativeText("Cancel")
                        .dismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                if (mOtherSemester.getTag() == null) mSemesterRadiogroup.clearCheck();
                            }
                        })
                        .showListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog) {
                                SemesterUtils.Semester sm =
                                        mOtherSemester.getTag() == null ? su.resolveCurrentSemester() :
                                                (SemesterUtils.Semester) mOtherSemester.getTag();

                                currentSeason = sm.getSeason().toString();
                                currentYear = sm.getYear();

                                StringPicker yearPicker = (StringPicker) pickerRoot.findViewById(R.id.yearPicker);
                                StringPicker seasonPicker = (StringPicker) pickerRoot.findViewById(R.id.seasonPicker);

                                yearPicker.setCurrent(su.getListOfYears().indexOf(currentYear));
                                seasonPicker.setCurrent(sm.getSeason().ordinal());

                            }

                        })
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                StringPicker yearPicker = (StringPicker) pickerRoot.findViewById(R.id.yearPicker);
                                StringPicker seasonPicker = (StringPicker) pickerRoot.findViewById(R.id.seasonPicker);
                                currentYear = yearPicker.getCurrentValue();
                                currentSeason = seasonPicker.getCurrentValue();
                                SemesterUtils.Semester semester = new SemesterUtils.Semester(currentSeason, currentYear);
                                mOtherSemester.setText(semester.toString());
                                mOtherSemester.setTag(semester);

                                super.onPositive(dialog);
                            }
                        })
                        .show();
            }

            private View createPicker(SemesterUtils su) {
                final LinearLayout pickerRoot = (LinearLayout)getParentActivity().getLayoutInflater().inflate(R.layout.picker, null);

                final StringPicker seasonPicker = (StringPicker)pickerRoot.findViewById(R.id.seasonPicker);
                seasonPicker.setValues(su.getListOfSeasons());

                final StringPicker yearPicker = (StringPicker)pickerRoot.findViewById(R.id.yearPicker);
                yearPicker.setValues(su.getListOfYears());

                return pickerRoot;
            }
        });
                mSearchButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isValidInputs()) {
                            changeFragment(createArgs(createRequest()));
                        }
                    }
                });

        return rootView;
    }

    @Override
    public void onResume() {
        mSemesterRadiogroup.clearCheck();
        super.onResume();
    }

    private void setToolbar(View rootView) {
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        getParentActivity().setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentActivity().onBackPressed();
            }
        });
        getParentActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getParentActivity().getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private boolean isValidInputs() {
        int checkedButton = mSemesterRadiogroup.getCheckedRadioButtonId();
        RadioButton selectedButton = (RadioButton) getParentActivity().findViewById(checkedButton);
        if (selectedButton == null) {
            makeToast("Select a semester");
            return false;
        } else if (!mLocation1.isChecked() && !mLocation2.isChecked() && !mLocation3.isChecked()) {
            makeToast("Select a location");
            return false;
        } else if (!mLevel1.isChecked() && !mLevel2.isChecked()) {
            makeToast("Select a level");
            return false;
        } else {
            return true;
        }
    }

    private void makeToast(CharSequence s) {
        Toast.makeText(getParentActivity(), s, Toast.LENGTH_LONG).show();
    }

    void changeFragment(Bundle b) {
        SubjectFragment sf = new SubjectFragment();
        sf.setArguments(b);
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sf.setEnterTransition(new Fade(Fade.IN).excludeTarget(ImageView.class, true));
            sf.setExitTransition(new Fade(Fade.OUT).excludeTarget(ImageView.class, true));
            sf.setReenterTransition(new Fade(Fade.IN).excludeTarget(ImageView.class, true));
            sf.setReturnTransition(new Fade(Fade.OUT).excludeTarget(ImageView.class, true));
            sf.setAllowReturnTransitionOverlap(true);
            sf.setAllowEnterTransitionOverlap(true);
            sf.setSharedElementEnterTransition(new ChangeBounds().setInterpolator(new EaseOutQuint()));
            sf.setSharedElementReturnTransition(new ChangeBounds().setInterpolator(new EaseOutQuint()));
            ft.addSharedElement(toolbar, "toolbar_background");
        } else {
            ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        }
        ft.replace(R.id.container, sf).addToBackStack(this.toString())
                .commit();
    }

    Bundle createArgs(Parcelable p) {
        Bundle args = new Bundle();
        args.putParcelable(MainActivity.REQUEST, p);
        return args;
    }

    private SemesterUtils.Semester getSemester() {
        int checkedButton = mSemesterRadiogroup.getCheckedRadioButtonId();
        RadioButton selectedButton = (RadioButton) getParentActivity().findViewById(checkedButton);
        return (SemesterUtils.Semester) selectedButton.getTag();
    }

    private ArrayList<String> getLocations() {
        ArrayList<String> locations = new ArrayList<>();
        if (mLocation1.isChecked()) {
            locations.add((String) mLocation1.getText());
        }
        if (mLocation2.isChecked()) {
            locations.add((String) mLocation2.getText());
        }
        if (mLocation3.isChecked()) {
            locations.add((String) mLocation3.getText());
        }

        return locations;
    }

    private ArrayList<String> getLevels() {
        ArrayList<String> levels = new ArrayList<>();
        if (mLevel1.isChecked()) {
            levels.add((String) mLevel1.getText());
        }
        if (mLevel2.isChecked()) {
            levels.add((String) mLevel2.getText());
        }

        return levels;
    }

    private Request createRequest() {
        return new Request(null, getSemester(), getLocations(), getLevels());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}