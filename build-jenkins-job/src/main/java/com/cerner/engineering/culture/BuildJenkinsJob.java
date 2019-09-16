package com.cerner.engineering.culture;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cdancy.jenkins.rest.JenkinsClient;
import com.cdancy.jenkins.rest.domain.common.IntegerResponse;
import com.cdancy.jenkins.rest.features.JobsApi;

/**
 * Builds a Jenkins job.
 * cerner_2^5_2019
 */
public class BuildJenkinsJob {
    public static void main(String[] args) {
        if( args.length < 4 ) {
            throw new IllegalArgumentException("Invalid arguments: <endPoint> <username> <password> <job> <parameters> (optional) must be provided.");
        }
        String job = args[3];
        System.out.println(MessageFormat.format("{0} is building...", job));
        JenkinsClient client = JenkinsClient.builder().credentials(MessageFormat.format("{0}:{1}", args[1], args[2])).endPoint(args[0]).build();
        JobsApi jobs = client.api().jobsApi();
        Map<String, List<String>> paramaters = new HashMap<String, List<String>>();
        for(int param = 4; param < args.length; param++) {
            String[] paramater = args[param].split(":");
            paramaters.put(paramater[0], Collections.singletonList(paramater[1]));
        }
        IntegerResponse response = paramaters.isEmpty() ? jobs.build(null, job) : jobs.buildWithParameters(null, job, paramaters);
        System.out.println(MessageFormat.format("Job: {0} Status:{1}", job, response != null && response.value() > 0 ? "Success": "Failure"));
    }
}