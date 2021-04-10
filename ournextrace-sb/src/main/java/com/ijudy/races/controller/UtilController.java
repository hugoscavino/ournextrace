package com.ijudy.races.controller;

import com.ijudy.races.pojo.Environment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value="/api/v2")
@Slf4j
public class UtilController extends BaseController {

    @Value( "${spring.profiles.active}" )
    private String profile;

    @GetMapping(value ="/env")
    @ResponseBody
    public Environment getEnvironment() {
        return new Environment(profile);
    }

    @RequestMapping("/version")
    public Map<String, String> getCommitId() {
        Map<String, String> result = new HashMap<>();
        result.put("Build Group", "OurNextRace");
        result.put("Build Name", "OurNextRace");
        result.put("Build Artifact", "OurNextRace");
        result.put("Build Version", "3.0.0");
        result.put("Build Time", "April 9, 2021");
        return result;
    }
}
