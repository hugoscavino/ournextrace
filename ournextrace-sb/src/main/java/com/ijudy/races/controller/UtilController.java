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

    @Autowired
    public BuildProperties buildProperties;

    @GetMapping(value ="/env")
    @ResponseBody
    public Environment getEnvironment() {
        return new Environment(profile);
    }

    @RequestMapping("/version")
    public Map<String, String> getCommitId() {
        Map<String, String> result = new HashMap<>();
        result.put("Build Group", buildProperties.getGroup());
        result.put("Build Name", buildProperties.getName());
        result.put("Build Artifact", buildProperties.getArtifact());
        result.put("Build Version", buildProperties.getVersion());
        result.put("Build Time", buildProperties.getTime().toString());
        return result;
    }
}
