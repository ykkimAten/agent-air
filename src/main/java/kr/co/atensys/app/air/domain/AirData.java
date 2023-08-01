package kr.co.atensys.app.air.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class AirData {
    public enum O2Level {
        GOOD("좋음"), NORMAL("보통"), BAD("나쁨"), UNKNOWN("");

        private String name;

        O2Level(String name) {
            this.name = name;
        }

        public static O2Level parse(String text) {
            switch(text) {
                case "좋음": return GOOD;
                case "보통": return NORMAL;
                case "나쁨": return BAD;
                default: return UNKNOWN;
            }
        }
    }

    public enum Pm10 {
        GOOD("좋음"), NORMAL("보통"), BAD("나쁨"), VERY_BAD("매우나쁨"), UNKNOWN("");

        private String name;

        Pm10(String name) {
            this.name = name;
        }

        public static Pm10 parse(String text) {
            switch(text) {
                case "좋음": return GOOD;
                case "보통": return NORMAL;
                case "나쁨": return BAD;
                case "매우나쁨": return VERY_BAD;
                default: return UNKNOWN;
            }
        }
    }

    /** 기준 날짜 */
    private Date date;

    /** 외부 온도 */
    private Long outsideTemperature;

    /** 외부 강수량 */
    private Double outsidePrecipitation;

    /** 외부 날씨 상태 */
    private String outsideWeatherInfo;

    /** 외부 날씨 아이콘 번호 */
    private Long outsideWeatherIconNumber;

    /** 외부 강수 확률 */
    private Long outsideRainPrecipitation;

    /** 외부 습도(%) */
    private Long outsideHumidity;

    /** 외부 pm10 미세먼지 문자열 */
    private Pm10 outsidePm10;

    /** 외부 pm2.5 초미세먼지 문자열 */
    private String outsidePm25;

    /** 내부 온도 */
    private Long insideTemperature;

    /** 내부 산소등급 */
    private O2Level insideO2Level;

    /** 내부 습도(%) */
    private Long insideHumidity;

    /** 내부 pm10 미세먼지 문자열 */
    private Pm10 insidePm10;

    /** 내부 pm2.5 초미세먼지 문자열 */
    private String insidePm25;
}
