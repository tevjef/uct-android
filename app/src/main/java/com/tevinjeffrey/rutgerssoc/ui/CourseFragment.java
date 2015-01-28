package com.tevinjeffrey.rutgerssoc.ui;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.Toolbar;
import android.transition.AutoTransition;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.tevinjeffrey.rutgerssoc.R;
import com.tevinjeffrey.rutgerssoc.adapters.CourseAdapter;
import com.tevinjeffrey.rutgerssoc.model.Course;
import com.tevinjeffrey.rutgerssoc.model.Request;
import com.tevinjeffrey.rutgerssoc.model.Subject;
import com.tevinjeffrey.rutgerssoc.utils.CourseUtils;
import com.tevinjeffrey.rutgerssoc.utils.UrlUtils;

import org.apache.commons.lang3.text.WordUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tevin on 1/14/2015.
 */
public class CourseFragment extends Fragment {

    private Request request;

    public CourseFragment() {
    }

    private MainActivity getParentActivity() {
        return (MainActivity)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getParentActivity().setPrimaryWindow();

        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        final ListView listView = (ListView) rootView.findViewById(R.id.courses);

        request = getArguments().getParcelable("request");
        setToolbar(rootView);

        String url = UrlUtils.getCourseUrl(UrlUtils.buildParamUrl(request));

        Log.d("URL" , url);

        Ion.with(this)
                .load(url)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {

                    @Override
                    public void onCompleted(Exception e, JsonArray result) {

                        if(e == null && result.size() > 0) {

                            //if result == 0,no data
                            //UnknownHostException , no internet
                            Log.e("Response", result.toString());

                            Gson gson = new Gson();

                            Type listType = new TypeToken<List<Course>>() {
                            }.getType();

                            getParentActivity().setCourses((ArrayList<Course>) gson.fromJson(result.toString(), listType));

                            Log.d("Response", result.toString());


                            final CourseAdapter subjectAdapter = new CourseAdapter(getActivity(), getParentActivity().getCourses());
                            listView.setAdapter(subjectAdapter);
                        } else {
                            Toast.makeText(getParentActivity(), "No Internet connection", Toast.LENGTH_LONG).show();
                        }

                    }
                });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                createFragment(createArgs(request, position));
            }
        });


        return rootView;
    }

    private void setToolbar(View rootView) {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        getParentActivity().setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentActivity().onBackPressed();
            }
        });
        getParentActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getParentActivity().getSupportActionBar().setDisplayShowHomeEnabled(true);

        setToolbarTitle(toolbar);
    }

    private void setToolbarTitle(Toolbar toolbar) {
        for(Subject s: getParentActivity().getSubjects()) {
            if(CourseUtils.formatNumber(s.getCode()).equals(request.getSubject())) {
                toolbar.setTitle(WordUtils.capitalize(s.getDescription().toLowerCase()));
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void createFragment(Bundle b) {
        Fragment courseInfoFragment = new CourseInfoFragment();

        courseInfoFragment.setEnterTransition(new AutoTransition());
        courseInfoFragment.setExitTransition(new AutoTransition());

        courseInfoFragment.setArguments(b);
        getFragmentManager().beginTransaction()
                .replace(R.id.container, courseInfoFragment).addToBackStack(null)
                .commit();
    }

    private Bundle createArgs(Parcelable parcelable, int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("request", parcelable);
        bundle.putInt("courseIndex", position);
        return bundle;
    }
}