package com.tevinjeffrey.rutgerssoc.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tevinjeffrey.rutgerssoc.R;
import com.tevinjeffrey.rutgerssoc.Request;

import java.util.ArrayList;

/**
 * Created by Tevin on 1/14/2015.
 */
public class ChooserFragment extends Fragment {

    public ChooserFragment() {
    }

    private MainActivity getParentActivity() {
        return (MainActivity)getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    //Semester
    RadioGroup radioGroup;
    //Location
    CheckBox firstLocation;
    CheckBox secondLocation;
    CheckBox thirdLocation;

    //Level
    CheckBox firstLevel;
    CheckBox secondLevel;

    TextView searchBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getParentActivity().setPrimaryWindow();

        final View rootView = inflater.inflate(R.layout.fragment_chooser, container, false);

        //TODO: Abstract to loop through a parent viewgroup to obtain all data.

        //Semester
        radioGroup = (RadioGroup) rootView.findViewById(R.id.semester_radiogroup);

        //Location

        firstLocation = (CheckBox) rootView.findViewById(R.id.location1);
        secondLocation = (CheckBox) rootView.findViewById(R.id.location2);
        thirdLocation = (CheckBox) rootView.findViewById(R.id.location3);

        //Level
        firstLevel = (CheckBox) rootView.findViewById(R.id.level1);
        secondLevel = (CheckBox) rootView.findViewById(R.id.level2);

        //Search Button
        searchBtn = (TextView) rootView.findViewById(R.id.search_button);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(createArgs(createRequest()));
            }
        });

        return rootView;
    }

    private void changeFragment(Bundle b){
        SubjectFragment sf = new SubjectFragment();

        sf.setArguments(b);

        getFragmentManager().beginTransaction()
                .replace(R.id.container, sf).addToBackStack(null)
                .commit();
    }

    private Bundle createArgs(Parcelable p) {
        Bundle args= new Bundle();
        args.putParcelable("request", p);

        return args;
    }



    private ArrayList<String> getLevels() {
        ArrayList<String> levels = new ArrayList<>();
        if(firstLevel.isChecked()) {
            levels.add((String) firstLevel.getText());
        }
        if(secondLevel.isChecked()) {
            levels.add((String) secondLevel.getText());
        }

        return levels;
    }

    private String getSemester() {
        int checkedButton = radioGroup.getCheckedRadioButtonId();
        RadioButton selectedButton = (RadioButton) getParentActivity().findViewById(checkedButton);
        return (String) selectedButton.getText();
    }

    private ArrayList<String> getLocations() {
        ArrayList<String> locations = new ArrayList<>();
        if(firstLocation.isChecked()) {
            locations.add((String) firstLocation.getText());
        }
        if(secondLocation.isChecked()) {
            locations.add((String) secondLocation.getText());
        }
        if(thirdLocation.isChecked()) {
            locations.add((String) thirdLocation.getText());
        }

        return locations;
    }

    private Request createRequest() {
        return  new Request(null, getSemester(), getLocations(), getLevels());
    }






}