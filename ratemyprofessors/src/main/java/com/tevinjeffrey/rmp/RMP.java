package com.tevinjeffrey.rmp;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tevinjeffrey.rmp.professor.Professor;
import com.tevinjeffrey.rmp.ratings.RatingParser;
import com.tevinjeffrey.rmp.search.Decider;
import com.tevinjeffrey.rmp.search.Listing;
import com.tevinjeffrey.rmp.search.SearchParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;


public class RMP {

    OkHttpClient client;

    public RMP(OkHttpClient client) {
        this.client = client;
    }

    public String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public Observable<List<Professor>> findBestProfessor(final Decider.Parameter params) {
        return Observable.create(new Observable.OnSubscribe<List<Professor>>() {
            @Override
            public void call(Subscriber<? super List<Professor>> sub) {
                String base = "http://www.ratemyprofessors.com";

                //search for professors
                String response = null;
                try {
                    response = get(base + "/search.jsp?query=" + params.name.getLast() + "+rutgers");
                } catch (IOException e) {
                    sub.onError(e);
                }

                List<Professor> professors = null;
                try {
                    professors = getProfessors(base, response);
                } catch (IOException e) {
                    sub.onError(e);
                }

                professors = Decider.determineProfessor(professors, params);
                sub.onNext(professors);
                sub.onCompleted();
            }
        });
    }

    public List<Professor> searchProfessors(String last) throws IOException{
        String base = "http://www.ratemyprofessors.com";

        //search for professors
        String response = get(base + "/search.jsp?query=" + last + "+rutgers");

        return getProfessors(base, response);
    }

    public List<Professor> findBestMatch(String first, String last, String school, String department, String location, List<Professor> professors) {
        Decider.Parameter params = new Decider.Parameter(school, department, location, new Professor.Name(first, last));
        professors = Decider.determineProfessor(professors, params);
        return professors;
    }

    private List<Professor> getProfessors(String base, String response) throws IOException {
        List<Professor> professors = new ArrayList<Professor>();
        for(Listing l : SearchParser.getSearchResults(response)) {
            String response2 = get(base + l.getUrl());
            Professor p = RatingParser.findProfessor(l, response2);
            if(p != null) {
                professors.add(p);
            }
        }

        return professors;
    }

    public interface RMPCallback {
        void onComplete(List<Professor> l);
        void onError(Exception e);
    }
}