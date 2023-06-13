package com.raldes.webscrapping.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.raldes.webscrapping.model.JobDTO;
import com.raldes.webscrapping.utils.DtoNullKeySerializer;
import com.raldes.webscrapping.utils.JsonRunnable;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class ScrapperServiceImpl implements ScrapperService {

    @Value("https://www.cermati.com/karir")
    String urls;

    private static Map<String, List<JobDTO>> departmentJobs = new HashMap<>();

    @Override
    public Map<String, List<JobDTO>> getJobDataByCountry(String jobData) {

        if(urls.contains("cermati")) {
            extractData(urls);
        }

        return departmentJobs;
    }

    private void extractData(String urls) {
        List<String[]> jobList = new ArrayList<>();

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
                    jobDeptURLPoster[2] = currJob.getJSONObject("creator").getString("name");
                } catch (Exception e) {
                    jobDeptURLPoster[2] = "N/A";
                }

                jobList.add(jobDeptURLPoster);
            }

            ExecutorService executor = Executors.newFixedThreadPool(4); // 4 threads
            for (String[] job: jobList) {
                executor.submit(new JsonRunnable(job));
            }
            executor.shutdown();

            try {
                executor.awaitTermination(1, TimeUnit.DAYS);
            } catch (InterruptedException ignored) {
                ignored.printStackTrace();
            }

            ObjectMapper mapper = new ObjectMapper();
            mapper.getSerializerProvider().setNullKeySerializer(new DtoNullKeySerializer());

            try {
                mapper.writeValue(new File("solution.json"), departmentJobs);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException exception) {
            System.out.println(exception);
        }
    }

    public static synchronized void addJobToDepartment(JobDTO job, String department) {
        if(department != null && job != null) {
            departmentJobs.computeIfAbsent(department, k -> new ArrayList<>()).add(job);
        }

    }
}