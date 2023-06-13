package com.raldes.webscrapping.utils;

import com.raldes.webscrapping.model.JobDTO;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import static com.raldes.webscrapping.service.ScrapperServiceImpl.addDepartment;

public class JsonRunnable implements Runnable {

    private String[] arrayJobs;

    private String jobCountry;

    public JsonRunnable(String[] arrayJobs) {
        this.arrayJobs = arrayJobs;
    }

    public JsonRunnable(String[] arrayJobs, String jobCountry) {
        this.arrayJobs = arrayJobs;
        this.jobCountry = jobCountry;
    }

    @Override
    public void run() {
        String department = arrayJobs[0];
        String url = arrayJobs[1];
        String urlPoster = arrayJobs[2];
        String jobDescription = "";
        String jobQualification = "";

        try {
            InputStream inputStream = new URL(url).openStream();

            BufferedReader bufferedReader
                    = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

            StringBuilder stringBuilder = new StringBuilder();

            String str;
            while ((str = bufferedReader.readLine()) != null) {
                stringBuilder.append(str);
            }

            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            System.out.println(jsonObject);

            jobDescription = jsonObject.getJSONObject("jobAd")
                    .getJSONObject("sections")
                    .getJSONObject("jobDescription")
                    .getString("text");
            jobQualification = jsonObject.getJSONObject("jobAd")
                    .getJSONObject("sections")
                    .getJSONObject("qualifications")
                    .getString("text");
            String country = jsonObject.getJSONObject("location").getString("country");
            country = (country.equals("in")) ? ", India": (country.equals("id")?", Indonesia":"");
            if(jobCountry.equalsIgnoreCase(country.replace(", ", ""))) {
                JobDTO jobObject = new JobDTO();
                jobObject.setDescription(TextUtils.getTexts(jobDescription));
                jobObject.setQualification(TextUtils.getTexts(jobQualification));
                jobObject.setTitle(jsonObject.getString("name"));
                jobObject.setPoster(urlPoster);
                jobObject.setLocation(jsonObject.getJSONObject("location").getString("city") + country);
                addDepartment(jobObject, department);
                inputStream.close();
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
