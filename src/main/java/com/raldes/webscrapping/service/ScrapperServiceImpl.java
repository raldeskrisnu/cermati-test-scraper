package com.raldes.webscrapping.service;

import com.raldes.webscrapping.model.JobDTO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ScrapperServiceImpl implements ScrapperService {

    @Value("https://www.cermati.com/karir")
    String urls;

    @Override
    public Set<JobDTO> getJobDataByCountry(String jobData) {
        Set<JobDTO> responseData = new HashSet<>();

        if(urls.contains("cermati")) {
            extractData(responseData, urls);
        }

        return responseData;
    }

    private void extractData(Set<JobDTO> jobDTOS, String urls) {
        try {
            Document document = Jsoup.connect(urls).get();
            Element element = document.getElementById("initials");
            JSONObject jsonObject = new JSONObject(element.data());

            JSONArray jobsArray = jsonObject.getJSONObject("smartRecruiterResult")
                    .getJSONObject("all")
                    .getJSONArray("content");

            for(int i=0; i<jobsArray.length(); i++) {
                String[] jobDeptURLPoster = new String[3];
                JSONObject currJob = jobsArray.getJSONObject(i);
                if(currJob.getJSONObject("department") !=null
                        && currJob.getJSONObject("department").has("label")) {
                    jobDeptURLPoster[0] = currJob.getJSONObject("department").getString("label");
                }

                jobDeptURLPoster[1] = currJob.getString("ref");

                try {
                    // get poster name if present
                    jobDeptURLPoster[2] = currJob.getJSONObject("creator").getString("name");
                } catch (Exception e) {
                    jobDeptURLPoster[2] = "N/A";
                }

                System.out.println(jobDeptURLPoster);
            }

        } catch (IOException exception) {
            System.out.println(exception);
        }
    }
}
