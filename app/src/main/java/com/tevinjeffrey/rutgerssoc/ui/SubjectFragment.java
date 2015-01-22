package com.tevinjeffrey.rutgerssoc.ui;

/**
 * Created by Tevin on 1/14/2015.
 */

import android.app.Fragment;
import android.os.Bundle;
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
import com.tevinjeffrey.rutgerssoc.Request;
import com.tevinjeffrey.rutgerssoc.model.Subject;
import com.tevinjeffrey.rutgerssoc.adapters.SubjectAdapter;
import com.tevinjeffrey.rutgerssoc.utils.UrlUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class SubjectFragment extends Fragment {

    public SubjectFragment() {
    }

    private MainActivity getParentActivity() {
        return (MainActivity)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        final ListView listView = (ListView) rootView.findViewById(R.id.courses);

        UrlUtils urlUtils = new UrlUtils(getParentActivity());

        String url  = UrlUtils.getSubjectUrl(urlUtils.buildParamUrl(
                (com.tevinjeffrey.rutgerssoc.Request) getArguments().getParcelable("request")));
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

                CourseFragment courseFragment = new CourseFragment();

                Request request = getArguments()
                        .getParcelable("request");

                request.setSubject(String.valueOf(((Subject)parent.getAdapter().getItem(position))
                        .getCode()));

                Bundle bundle = new Bundle();
                bundle.putParcelable("request", request);

                courseFragment.setArguments(bundle);

                getFragmentManager().beginTransaction()
                        .replace(R.id.container, courseFragment).addToBackStack(null)
                        .commit();
            }
        });


        return rootView;
    }



}