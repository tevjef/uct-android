package com.tevinjeffrey.rutgersct.ui;

import android.app.FragmentTransaction;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.animator.EaseOutQuint;
import com.tevinjeffrey.rutgersct.model.Request;

import java.util.ArrayList;

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

    private void makeToast(String s) {
        Toast.makeText(getParentActivity(), s, Toast.LENGTH_LONG).show();
    }

    private void changeFragment(Bundle b) {
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

    private Bundle createArgs(Parcelable p) {
        Bundle args = new Bundle();
        args.putParcelable(MainActivity.REQUEST, p);
        return args;
    }

    private String getSemester() {
        int checkedButton = mSemesterRadiogroup.getCheckedRadioButtonId();
        RadioButton selectedButton = (RadioButton) getParentActivity().findViewById(checkedButton);
        return selectedButton.getText().toString();
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
}