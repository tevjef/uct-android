package com.tevinjeffrey.rutgerssoc.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.Toolbar;
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
import com.tevinjeffrey.rutgerssoc.adapters.SubjectAdapter;
import com.tevinjeffrey.rutgerssoc.model.Request;
import com.tevinjeffrey.rutgerssoc.model.Subject;
import com.tevinjeffrey.rutgerssoc.utils.CourseUtils;
import com.tevinjeffrey.rutgerssoc.utils.UrlUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class SubjectFragment extends Fragment {

    private Request request;

    public SubjectFragment() {
    }

    private MainActivity getParentActivity() {
        return (MainActivity)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getParentActivity().setPrimaryWindow();
        setRetainInstance(true);

        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        final ListView listView = (ListView) rootView.findViewById(R.id.courses);

        request = getArguments().getParcelable("request");
        setToolbar(rootView);

        String url = UrlUtils.getSubjectUrl(UrlUtils.buildParamUrl(request));

        Log.d("URL" , url);

        Ion.with(this)
                .load(url)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {

                    @Override
                    public void onCompleted(Exception e, JsonArray result) {


                        //TODO: Handle UnknownHostException for when the there's no internet connection

                        if (e == null && result.size() > 0) {
                            Gson gson = new Gson();

                            Type listType = new TypeToken<List<Subject>>() {
                            }.getType();

                            getParentActivity().setSubjects((ArrayList<Subject>)
                                    gson.fromJson(result.toString(), listType));

                            Log.d("Response", result.toString());


                            final SubjectAdapter subjectAdapter = new SubjectAdapter(getActivity(),
                                    getParentActivity().getSubjects());

                            listView.setAdapter(subjectAdapter);
                        } else {
                            Toast.makeText(getParentActivity(), "No Internet connection", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setSubject(parent, position);
                createFragment(createArgs(request));
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
        toolbar.setTitle(request.getSemester());

        ArrayList<String> al = new ArrayList<>();
        for(String s: request.getLocations()) {
            al.add(UrlUtils.getAbbreviatedLocationName(s));
        }
        toolbar.setSubtitle(Request.toStringList(al) + " - "
                + Request.toStringList(request.getLevels()));
    }

    private void setSubject(AdapterView<?> parent, int position) {
        request.setSubject(CourseUtils.formatNumber(((Subject) parent.getAdapter()
                .getItem(position))
                .getCode()));
    }

    private void createFragment(Bundle b) {
        CourseFragment courseFragment = new CourseFragment();
        courseFragment.setArguments(b);
        getFragmentManager().beginTransaction()
                .replace(R.id.container, courseFragment).addToBackStack(null)
                .commit();
    }

    private Bundle createArgs(Parcelable parcelable) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("request", parcelable);
        return bundle;
    }
}