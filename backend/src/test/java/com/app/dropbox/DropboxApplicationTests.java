package com.app.dropbox;

import static org.junit.Assert.*;

import java.util.*;

import com.app.dropbox.dto.AssetDto;
import com.app.dropbox.dto.GroupDto;
import com.app.dropbox.dto.ResponseDto;
import com.app.dropbox.dto.UserDto;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=DropboxApplicationTests.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DropboxApplicationTests {

    static Random num = new Random();
    static int number = num.nextInt(3000);
    static String email = "testuser"+Integer.toString(number)+"@gmail.com";
    static String password = Integer.toString(number);
    static String firstName = "Test";
    static String lastName = "User";

    static String session = "";

    RestTemplate rest = new RestTemplate();


    @Test
    public void test_0() {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Content-Type", "application/json");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", email);
            jsonObject.put("password", password);
            jsonObject.put("firstName", firstName);
            HttpEntity<String> httpEntity = new HttpEntity <String> (jsonObject.toString(), httpHeaders);
            ResponseEntity<ResponseDto> res = rest.postForEntity("http://localhost:3001/api/signup", httpEntity, ResponseDto.class);
        } catch (JSONException e){

        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        }
    }

    @Test
    public void test_1() {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Content-Type", "application/json");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", email);
            jsonObject.put("password", password);
            jsonObject.put("firstName", firstName);
            jsonObject.put("lastName", lastName);
            HttpEntity<String> httpEntity = new HttpEntity <String> (jsonObject.toString(), httpHeaders);
            ResponseEntity<ResponseDto> res = rest.postForEntity("http://localhost:3001/api/signup", httpEntity, ResponseDto.class);
            assertEquals(res.getStatusCode().value(), 200);
        } catch (JSONException e){

        }
    }

    @Test
    public void test_2() {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Content-Type", "application/json");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", email);
            jsonObject.put("password", password);
            jsonObject.put("firstName", firstName);
            jsonObject.put("lastName", lastName);
            HttpEntity<String> httpEntity = new HttpEntity <String> (jsonObject.toString(), httpHeaders);
            ResponseEntity<ResponseDto> res = rest.postForEntity("http://localhost:3001/api/signup", httpEntity, ResponseDto.class);
        } catch (JSONException e){

        } catch (HttpClientErrorException e) {
            assertEquals(409, e.getStatusCode().value());
        }
    }

    @Test
    public void test_3() {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Content-Type", "application/json");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", email);
            jsonObject.put("password", "sss");
            HttpEntity<String> httpEntity = new HttpEntity <String> (jsonObject.toString(), httpHeaders);
            ResponseEntity<ResponseDto> res = rest.postForEntity("http://localhost:3001/api/signin", httpEntity, ResponseDto.class);
        } catch (JSONException e){

        } catch (HttpClientErrorException e) {
            assertEquals(401, e.getStatusCode().value());
        }
    }

    @Test
    public void test_4() {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Content-Type", "application/json");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", email);
            jsonObject.put("password", password);
            HttpEntity<String> httpEntity = new HttpEntity <String> (jsonObject.toString(), httpHeaders);
            ResponseEntity<ResponseDto> res = rest.postForEntity("http://localhost:3001/api/signin", httpEntity, ResponseDto.class);
            assertEquals(res.getStatusCode().value(), 200);
            //System.out.println(res.getHeaders().get("Cookie"));
            HttpHeaders headers = res.getHeaders();
            Set<String> keys = headers.keySet();
            String cookie = "";
            for (String header : keys) {
                if (header.equals("Set-Cookie")) {
                    cookie = headers.get(header).get(0);
                }
            }
            String jsessionid = cookie.split(";")[0];
            this.session = jsessionid.split("=", 2)[1];
        } catch (JSONException e){

        }
    }

    @Test
    public void test_5() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "JSESSIONID=" + this.session);
        ResponseEntity<ResponseDto> res = rest.exchange("http://localhost:3001/api/get_groups", HttpMethod.GET,new HttpEntity<Void>(headers), ResponseDto.class);
        assertEquals(res.getStatusCode().value(), 200);
        assertTrue(((ArrayList)res.getBody().getData()).size() == 0);
    }

    @Test
    public void test_6() {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Cookie", "JSESSIONID=" + this.session);
            httpHeaders.set("Content-Type", "application/json");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", "test");
            HttpEntity<String> httpEntity = new HttpEntity <String> (jsonObject.toString(), httpHeaders);
            ResponseEntity<ResponseDto> res = rest.postForEntity("http://localhost:3001/api/create_group", httpEntity, ResponseDto.class);
            assertEquals(res.getStatusCode().value(), 200);
        } catch (JSONException e){

        }
    }

    @Test
    public void test_7() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "JSESSIONID=" + this.session);
        ResponseEntity<ResponseDto> res = rest.exchange("http://localhost:3001/api/get_groups", HttpMethod.GET,new HttpEntity<Void>(headers), ResponseDto.class);
        assertEquals(res.getStatusCode().value(), 200);
        assertTrue(((ArrayList)res.getBody().getData()).size() == 1);
    }

    @Test
    public void test_8() {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Cookie", "JSESSIONID=" + this.session);
            httpHeaders.set("Content-Type", "application/json");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("firstName", firstName);
            jsonObject.put("lastName", lastName);
            jsonObject.put("about", "This is test about");
            HttpEntity<String> httpEntity = new HttpEntity <String> (jsonObject.toString(), httpHeaders);
            ResponseEntity<ResponseDto> res = rest.postForEntity("http://localhost:3001/api/user_profile", httpEntity, ResponseDto.class);
            assertEquals(res.getStatusCode().value(), 200);
        } catch (JSONException e){

        }
    }

    @Test
    public void test_9() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "JSESSIONID=" + this.session);
        ResponseEntity<ResponseDto> res = rest.exchange("http://localhost:3001/api/user_profile", HttpMethod.GET,new HttpEntity<Void>(headers), ResponseDto.class);
        assertEquals(res.getStatusCode().value(), 200);
        assertTrue(((HashMap)res.getBody().getData()).get("about").equals("This is test about"));
    }

}