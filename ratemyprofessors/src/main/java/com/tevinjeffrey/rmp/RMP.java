package com.tevinjeffrey.rmp;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RMP {

    OkHttpClient client = new OkHttpClient();

    protected String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public Professor findBestProfessor(String first, String last, String school, String department, String location) throws IOException {

        String base = "http://www.ratemyprofessors.com";

        //search for professors
        String response = run(base + "/search.jsp?query=" + last + "+rutgers");

        List<Professor> professors = getProfessors(base, response);

        Decider.Parameter params = new Decider.Parameter(school, department, location, new Professor.Name(first, last));
        professors = Decider.determineProfessor(professors, params);
        return professors.get(0);
    }

    public List<Professor> searchProfessors(String last) throws IOException{
        String base = "http://www.ratemyprofessors.com";

        //search for professors
        String response = run(base + "/search.jsp?query=" + last + "+rutgers");

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
            String response2 = run(base + l.getUrl());
            Professor p = RatingParser.findProfessor(l, response2);
            if(p != null) {
                professors.add(p);
            }
        }

        return professors;
    }
}