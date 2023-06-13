package com.raldes.webscrapping.model;

import lombok.Data;

import java.util.List;

@Data
public class JobDTO {

    List<String> description;

    List<String> qualification;

    String title;

    String location;

    String poster;
}
