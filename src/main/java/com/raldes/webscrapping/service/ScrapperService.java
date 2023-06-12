package com.raldes.webscrapping.service;

import com.raldes.webscrapping.model.JobDTO;

import java.util.Set;


public interface ScrapperService {

    Set<JobDTO> getJobDataByCountry(String jobData);
}
