package kr.co.atensys.app.air.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.atensys.app.air.domain.ConfigInfo;
import kr.co.atensys.app.air.mapper.ConfigMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class ConfigService {
    @Resource(name = "configMapper")
    private ConfigMapper configMapper;

    public String getValue(String codePath) {
        return this.getValue(codePath, null);
    }


    public String getValue(String codePath, String defaultValue) {
        ConfigInfo config = configMapper.getConfig(codePath);
        String value = config != null ? config.getValue() : defaultValue;

        if (value == null) return null;

        value = value.startsWith("\"") ? value.substring(1) : value;
        value = value.endsWith("\"") ? value.substring(0, value.length() - 1) : value;

        return value;
    }

    public Map<String, Object> convertMap(String value) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(value, Map.class);
        } catch(Exception e) {
            return new HashMap<>();
        }
    }
}
