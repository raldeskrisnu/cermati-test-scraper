package com.raldes.webscrapping.controller;

import com.raldes.webscrapping.model.JobDTO;
import com.raldes.webscrapping.service.ScrapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(path = "/jobs")
public class ScrapperController {

    @Autowired
    ScrapperService scrapperService;

    @GetMapping(path = "/{country}")
    public Map<String, List<JobDTO>> getJobsByCountry(@PathVariable String country) {
        return  scrapperService.getJobDataByCountry(country);
    }

//    @GetMapping(path = "/allJobs")
//    public Map<String, List<JobDTO>> getJobs(@PathVariable String country) {
//        return  scrapperService.getJobDataByCountry(country);
//    }
}
