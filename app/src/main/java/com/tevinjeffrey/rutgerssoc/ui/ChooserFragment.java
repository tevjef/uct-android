package com.tevinjeffrey.rutgerssoc.ui;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tevinjeffrey.rutgerssoc.R;
import com.tevinjeffrey.rutgerssoc.model.Request;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Tevin on 1/14/2015.
 */
public class ChooserFragment extends Fragment {

    @InjectView(R.id.header_text)
    TextView mHeaderText;
    @InjectView(R.id.subtitle)
    TextView mSubtext;
    @InjectView(R.id.toolbar_header_chooser)
    Toolbar mToolbarHeaderChooser;
    @InjectView(R.id.semester_title)
    TextView mSemesterTitle;
    @InjectView(R.id.semester1)
    RadioButton mSemester1;
    @InjectView(R.id.semester2)
    RadioButton mSemester2;
    @InjectView(R.id.semester3)
    RadioButton mSemester3;
    @InjectView(R.id.semester_radiogroup)
    RadioGroup mSemesterRadiogroup;
    @InjectView(R.id.location_title)
    TextView mLocationTitle;
    @InjectView(R.id.location1)
    CheckBox mLocation1;
    @InjectView(R.id.location2)
    CheckBox mLocation2;
    @InjectView(R.id.location3)
    CheckBox mLocation3;
    @InjectView(R.id.level_title)
    TextView mLevelTitle;
    @InjectView(R.id.level1)
    CheckBox mLevel1;
    @InjectView(R.id.level2)
    CheckBox mLevel2;
    @InjectView(R.id.search_button)
    TextView mSearchButton;

    public ChooserFragment() {
    }

    private MainActivity getParentActivity() {
        return (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getParentActivity().setPrimaryWindow();
        setRetainInstance(true);

        final View rootView = inflater.inflate(R.layout.fragment_chooser, container, false);
        ButterKnife.inject(this, rootView);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidInputs()) {
                    changeFragment(createArgs(createRequest()));
                }
            }
        });

        return rootView;
    }

    private boolean isValidInputs() {
        int checkedButton = mSemesterRadiogroup.getCheckedRadioButtonId();
        RadioButton selectedButton = (RadioButton) getParentActivity().findViewById(checkedButton);
        if(selectedButton == null) {
            makeToast("Select a semester");
            return false;
        } else if(!mLocation1.isChecked() && !mLocation2.isChecked() && !mLocation3.isChecked()) {
            makeToast("Select a location");
            return false;
        } else if(!mLevel1.isChecked() && !mLevel2.isChecked()) {
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

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sf.setEnterTransition(new Fade());
            sf.setExitTransition(new Fade());
        }

        sf.setArguments(b);

        getFragmentManager().beginTransaction()
                .replace(R.id.container, sf).addToBackStack(null)
                .commit();
    }

    private Bundle createArgs(Parcelable p) {
        Bundle args = new Bundle();
        args.putParcelable("request", p);

        return args;
    }

    private String getSemester() {
        int checkedButton = mSemesterRadiogroup.getCheckedRadioButtonId();
        RadioButton selectedButton = (RadioButton) getParentActivity().findViewById(checkedButton);
        return (String) selectedButton.getText();
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