package com.example.naucnacentrala.utils;


import com.example.naucnacentrala.model.Lokacija;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleCoords {


    @Autowired
    RestTemplate restTemplate;

    public Lokacija getKoordinate(String grad) throws JSONException {
        grad = grad.replaceAll(" ", "+");
        ResponseEntity responseEntity = restTemplate.getForEntity("https://maps.googleapis.com/maps/api/geocode/json?address=" + grad +
                "&key=AIzaSyDAhIfbemeaLl0uSU4Ob29nTGEHKbso6Eo", String.class);
        JSONObject jsonObject = new JSONObject(responseEntity.getBody().toString());
        JSONArray results = jsonObject.getJSONArray("results");
        JSONObject resultsArray = results.getJSONObject(0);
        JSONObject geometry = resultsArray.getJSONObject("geometry");
        JSONObject location = geometry.getJSONObject("location");

        Lokacija lokacija = new Lokacija();
        lokacija.setLatitude(Double.parseDouble(location.get("lat").toString()));
        lokacija.setLongitude(Double.parseDouble(location.get("lng").toString()));
        System.out.println("lat: " + lokacija.getLatitude());
        System.out.println("lon: " + lokacija.getLongitude());

        return lokacija;
    }

}
