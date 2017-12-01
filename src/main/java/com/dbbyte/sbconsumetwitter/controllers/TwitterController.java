package com.dbbyte.sbconsumetwitter.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.*;

@RequestMapping(value = "/api")
@RestController
public class TwitterController {

    public static final String TWITTER_BASE_URI = "svc/v1/tweets";
    public static final String TIPICODE_BASE_URI = "https://jsonplaceholder.typicode.com";


    @Autowired
    private Twitter twitter;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AsyncRestTemplate asyncRestTemplate;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    @RequestMapping(value = "/twitter/{hashTag}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Tweet> getTweets(@PathVariable final String hashTag) {
        return twitter.searchOperations().search(hashTag, 20).getTweets();
    }

    @RequestMapping(value = "/typicode", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getTypicodeComments() {
        return restTemplate.getForObject(TIPICODE_BASE_URI+"/comments",String.class);
    }

    @RequestMapping(value = "/async/typicode", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Async
    public CompletableFuture<String> getAsyncTypicodeComments() {

        CompletableFuture<String> completableFuture =
                supplyAsync(this::getTypicodeComments);

        return completableFuture;
    }

//    **************************************************************************************************

    @GetMapping(value = "/test")
    public String checkApp(){
        return "App it's working";
    }
}
