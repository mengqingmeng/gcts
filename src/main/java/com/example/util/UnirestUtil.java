package com.example.util;

import com.example.result.Result;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;


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

}
