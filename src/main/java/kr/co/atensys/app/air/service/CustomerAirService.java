package kr.co.atensys.app.air.service;

import kr.co.atensys.app.air.domain.AirData;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

@Lazy
@Service("gseAirService")
public class CustomerAirService  implements AirService {
    private final static Logger logger = LoggerFactory.getLogger(CustomerAirService.class);

    @Value("${air.url}")
    private String url;

    @Value("${air.serviceKey}")
    private String serviceKey;

    @Value("${air.connectionTimeout}")
    private int connectionTimeout;

    @Value("${air.readTimeout}")
    private int readTimeout;

    @Override
    public AirData fetch() throws Exception {
        BufferedReader bufferedReader = null;

        try {
            URL airUrl = new URL(url + "?serviceKey=" + serviceKey);
            HttpURLConnection con = (HttpURLConnection) airUrl.openConnection();
            con.setConnectTimeout(connectionTimeout);
            con.setReadTimeout(readTimeout);
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(false);

            StringBuilder sb = new StringBuilder();
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                bufferedReader = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "UTF-8"));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                JSONParser parser = new JSONParser();
                Object obj = parser.parse(sb.toString());
                JSONObject jsonObj = (JSONObject) obj;
                String resultMsg = (String) jsonObj.getOrDefault("resultMsg", "fail");
                boolean isSuccess = "success".equals(resultMsg);

                if (isSuccess) {
                    JSONObject outside = (JSONObject) jsonObj.get("outside");
                    JSONObject inside = (JSONObject) jsonObj.get("inside");

                    return AirData.builder()
                                  .date(parseDate(jsonObj.get("date")))
                                  .outsideTemperature(parseLong(outside.get("temp")))
                                  .outsidePrecipitation(parseDouble(outside.get("rainf")))
                                  .outsideWeatherInfo((String) outside.get("wtext"))
                                  .outsideWeatherIconNumber(parseLong(outside.get("icon")))
                                  .outsideRainPrecipitation(parseLong(outside.get("rainp")))
                                  .outsideHumidity(parseLong(outside.get("humi")))
                                  .outsidePm10(AirData.Pm10.parse((String) outside.get("pm10")))
                                  .insideTemperature(parseLong(inside.get("temp")))
                                  .insideO2Level(AirData.O2Level.parse((String) inside.get("o2")))
                                  .insidePm10(AirData.Pm10.parse((String) inside.get("pm10")))
                                  .build();
                } else {
                    logger.warn("Failed to get weather information (resultMsg: " + resultMsg + ")");
                }
            } else {
                logger.warn(con.getResponseMessage() + "(RESPONSE_CODE: " + con.getResponseCode() + ")");
            }
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }

        return null;
    }

    private Date parseDate(Object text) {
        try {
            return new Date((long) text);
        } catch (Exception e) {
            logger.warn("date is empty (" + text + ")");
            return null;
        }
    }

    private Double parseDouble(Object text) {
        return parseDouble(text, null);
    }

    private Double parseDouble(Object text, Double defaultValue) {
        try {
            return Double.parseDouble(text.toString());
        } catch(Exception e) {
            logger.warn("parse error (" + text + ")");
            return defaultValue;
        }
    }

    private Long parseLong(Object text) {
        return parseLong(text, null);
    }

    private Long parseLong(Object text, Long defaultValue) {
        try {
            return Long.parseLong(text.toString());
        } catch(Exception e) {
            logger.warn("parse error (" + text + ")");
            return defaultValue;
        }
    }
}
