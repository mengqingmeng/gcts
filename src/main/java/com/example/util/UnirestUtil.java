package com.example.util;

import com.example.result.Result;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


/**
 * Created by mqm on 2017/5/12.
 */
public class UnirestUtil {

    public static Result<JSONObject> ajaxPost(String url,JSONObject params) throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.post(url)
                .header("content-type", "application/x-www-form-urlencoded")
                .header("cache-control", "no-cache")
                .body(params)
                .asJson();
        Result<JSONObject> result = new Result();
        result.setCode(response.getStatus());
        result.setMessage(response.getStatusText());
        result.setData(response.getBody().getObject());
        return result;
    }


    public static Result<JSONObject> ajaxPost(String url,String params) throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.post(url)
                .header("content-type", "application/x-www-form-urlencoded")
                .header("cache-control", "no-cache")
                .body(params)
                .asJson();
        Result<JSONObject> result = new Result();
        result.setCode(response.getStatus());
        result.setMessage(response.getStatusText());
        result.setData(response.getBody().getObject());
        return result;
    }

    public static HttpResponse<String> get(String url) throws UnirestException {
        HttpResponse<String>  response = Unirest.get(url)
                .header("content-type", "application/x-www-form-urlencoded")
                .header("cache-control", "no-cache")
                .asString();
        return response;
    }
    public static Result<String> ajaxPostStr(String url,String params) throws UnirestException, InterruptedException {
        HttpResponse<String> response = Unirest.post(url)
                .header("content-type", "application/x-www-form-urlencoded")
                .header("cache-control", "no-cache")
                .body(params)
                .asString();
        Result<String> result = new Result();
        result.setCode(response.getStatus());
        result.setMessage(response.getStatusText());
        result.setData(response.getBody().toString());
        return result;
    }

    public static Result<String> post(String url) throws UnirestException {
        HttpResponse<String> response = Unirest.post(url)
                .header("content-type", "application/x-www-form-urlencoded")
                .header("cache-control", "no-cache")
                .asString();
        Result<String> result = new Result();
        result.setCode(response.getStatus());
        result.setMessage(response.getStatusText());
        result.setData(response.getBody().toString());
        return result;
    }

}
