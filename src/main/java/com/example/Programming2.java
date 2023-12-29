package com.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;

public class Programming2 {
    private static final String XNCI = "클라이언트 ID";
    private static final String XNCS = "클라이언트 시크릿";
    private static final String API_URL = "https://openapi.naver.com/v1/search/news.json";

    public static void main(String[] args) throws Exception {
        init();
        String apiData = apiCaller();
        List<String> titleList = converter(apiData);
        printer(titleList);
    }

    private static void init(){
        System.out.println("지금부터 최신 뉴스 알려드립니다.");
    };
    private static String apiCaller() throws Exception {
        String text = URLEncoder.encode("이선균","UTF-8");
        String apiUrl = API_URL + "?query=" + text + "&display=10&start=1&sort=date";
        
        URL url = new URL(apiUrl);
        
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("X-Naver-Client-Id", XNCI);
        con.setRequestProperty("X-Naver-Client-Secret", XNCS);

        int responseCode = con.getResponseCode();
        BufferedReader br;
        if (responseCode == 200) {
            br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        } else {
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = br.readLine()) != null) {
            response.append(inputLine);
        }
        br.close();

        return response.toString();
    };
    private static List<String> converter(String apiData){
        String jsonData = apiData;

        Gson gson = new Gson();

        JsonObject jsonObject = gson.fromJson(jsonData, JsonObject.class);

        JsonArray itemsArray = jsonObject.getAsJsonArray("items");

        List<String> titlesList = new ArrayList<>();
        for (JsonElement element : itemsArray) {
            JsonObject item = element.getAsJsonObject();
            String title = item.get("title").getAsString();
            titlesList.add(title);
        }

        return titlesList;
    };

    private static void printer(List<String> titlesList) {
        // 가져온 title 목록 출력
        for (String title : titlesList) {
            System.out.println(title);
        }
    };
}









