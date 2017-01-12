package com.example.ctlr;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by jjenkins on 1/11/2017.
 */
@RestController
public class HomeController {
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public GreetingService greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new GreetingService(counter.incrementAndGet(),
                String.format(template, name));
    }


    //todo turn the code into a service to setup auth requests
    @RequestMapping("/getServlet")
    public ResponseEntity<Hello> getTridiumData(){
        //get data from a remote server
        //create your request header
        HttpHeaders requestHeaders = new HttpHeaders();
        //set the username and password to a string
        String username="jjenkins:jjenkins";
        //get the bytes of the string and Base64Encode them
        byte[] bytesEncoded = Base64.encodeBase64(username.getBytes());
        //create a new string with the encoded bytes values
        String encondedUserCreds = new String(bytesEncoded);
        //create a Authorization request header and blast it with the  encondedUserCreds
        requestHeaders.set("Authorization","Basic "+encondedUserCreds);
        //create an  Http rest entity to set my Auth header
        HttpEntity<?> httpEntity = new HttpEntity<Object>(requestHeaders);
        //get an instance of the rest template
        RestTemplate restTemplate = new RestTemplate();
        //if performing a post
        // Create the request body as a MultiValueMap
        //MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        //body.add("field", "value");
        // Note the body object as first parameter!
        //HttpEntity<?> httpEntity = new HttpEntity<Object>(body, requestHeaders);
        return  restTemplate.exchange("http://localhost/test", HttpMethod.GET, httpEntity, Hello.class);
    }
}
