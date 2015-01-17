package com.tevinjeffrey.rutgerssoc.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tevinjeffrey.rutgerssoc.R;

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

    Semester semester = new Semester();
    Location locations = new Location();
    Level level = new Level();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
                buildLevels();
                buildLocations();
                buildSemesters();

                changeFragment();
            }
        });

        return rootView;
    }

    private void changeFragment(){
        SubjectFragment sf = new SubjectFragment();
        sf.setArguments(createArgs());

        getFragmentManager().beginTransaction()
                .replace(R.id.container, sf).addToBackStack(null)
                .commit();
    }

    private Bundle createArgs() {
        Bundle args= new Bundle();
        args.putString("args", buildUrl());
        return args;
    }

    private String buildUrl() {
            StringBuilder sb = new StringBuilder();
            sb.append("semester=");
            sb.append(semester.getSemesters());
            sb.append("&");
            sb.append("campus=");
            sb.append(locations.getLocations());
            sb.append("&");
            sb.append("level=");
            sb.append(level.getLevels());
        return sb.toString();
    }

    private void buildLevels() {
        if(firstLevel.isChecked()) {
            level.addLevel(firstLevel.getText());
        }
        if(secondLevel.isChecked()) {
            level.addLevel(secondLevel.getText());
        }
    }

    private void buildSemesters() {
        int checkedButton = radioGroup.getCheckedRadioButtonId();
        RadioButton selectedButton = (RadioButton) getParentActivity().findViewById(checkedButton);
        semester.parseSemester(selectedButton.getText());
    }

    private void buildLocations() {
        if(firstLocation.isChecked()) {
            locations.addLocation(firstLocation.getText());
        }
        if(secondLocation.isChecked()) {
            locations.addLocation(secondLocation.getText());
        }
        if(thirdLocation.isChecked()) {
            locations.addLocation(thirdLocation.getText());
        }
    }

    private class Semester {
        String semesterId;
        public Semester() {
            
        }

        private void parseSemester(CharSequence rb) {
            //Abstracted list of semesters with a limit of 3 selections
            //TODO: Abstract this class to handle an unlimited number of selections

            final String FIRST_SEMESTER = getResources().getString(R.string.firstSemester);
            final String SECOND_SEMESTER = getResources().getString(R.string.secondSemester);
            final String THIRD_SEMESTER = getResources().getString(R.string.thirdSemester);
            final String FIRST_SEMESTER_ID = getResources().getString(R.string.firstSemester_id);
            final String SECOND_SEMESTER_ID = getResources().getString(R.string.secondSemester_id);
            final String THIRD_SEMESTER_ID = getResources().getString(R.string.thirdSemester_id);

            if (FIRST_SEMESTER.equals(rb)) {
                semesterId = FIRST_SEMESTER_ID;
            } else if (SECOND_SEMESTER.equals(rb)) {
                semesterId = SECOND_SEMESTER_ID;
            } else if(THIRD_SEMESTER.equals(rb)) {
                semesterId = THIRD_SEMESTER_ID;
            } else {
                semesterId = FIRST_SEMESTER_ID;
            }
        }
        String getSemesters() {
            return semesterId;
        }
    }
    private class Location {
        StringBuilder location = new StringBuilder();

        public Location() {
        }

        public void addLocation(CharSequence cb) {
            //TODO: Abstract this class to support more location from a server
            final String FIRST_LOCATION = getResources().getString(R.string.firstLocation);
            final String SECOND_LOCATION = getResources().getString(R.string.secondLocation);
            final String THIRD_LOCATION = getResources().getString(R.string.thirdLocation);

            final String FIRST_LOCATION_ID = getResources().getString(R.string.firstLocation_id);
            final String SECOND_LOCATION_ID = getResources().getString(R.string.secondLocation_id);
            final String THIRD_LOCATION_ID = getResources().getString(R.string.thirdLocation_id);

            if (FIRST_LOCATION.equals(cb)) {
                append(location, FIRST_LOCATION_ID);
            } else if (SECOND_LOCATION.equals(cb)) {
                append(location, SECOND_LOCATION_ID);
            } else if (THIRD_LOCATION.equals(cb)) {
                append(location, THIRD_LOCATION_ID);
            } else {
                append(location, FIRST_LOCATION_ID);
                append(location, SECOND_LOCATION_ID);
                append(location, THIRD_LOCATION_ID);
            }
        }
        public String getLocations() {
            return location.toString();
        }
    }

    private class Level {
        StringBuilder level= new StringBuilder();

        public Level() {
        }

        public void addLevel(CharSequence cb) {
            //TODO: Abstract this class to support more location from a server
            final String FIRST_LEVEL = getResources().getString(R.string.firstLevel);
            final String SECOND_LEVEL = getResources().getString(R.string.secondLevel);
            final String FIRST_LEVEL_ID = getResources().getString(R.string.firstLevel_id);
            final String SECOND_LEVEL_ID = getResources().getString(R.string.secondLevel_id);

            if (FIRST_LEVEL.equals(cb)) {
                append(level, FIRST_LEVEL_ID);
            } else if (SECOND_LEVEL.equals(cb)) {
                append(level, SECOND_LEVEL_ID);
            }else {
                append(level, FIRST_LEVEL_ID);
                append(level, SECOND_LEVEL_ID);
            }
        }
        public String getLevels() {
            return level.toString();
        }
    }
    public static void appendComma(StringBuilder sb) {
        sb.append("%2C");
    }

    public static void append(StringBuilder sb, String loc) {
        if(sb.length() != 0)
            appendComma(sb);
        sb.append(loc);
    }
    public static String trim(StringBuilder sb){
       return sb.substring(0, sb.length() -3);
    }



}