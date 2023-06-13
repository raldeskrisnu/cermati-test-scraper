package com.raldes.webscrapping.service;

import com.raldes.webscrapping.model.JobDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;


public interface ScrapperService {

    Map<String, List<JobDTO>> getJobDataByCountry(String jobData);
}
