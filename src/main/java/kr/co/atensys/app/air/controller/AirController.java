package kr.co.atensys.app.air.controller;

import kr.co.atensys.app.air.service.AirService;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Map;

@Controller
public class AirController {

    @Resource
    private BeanFactory beanFactory;

    @Value("${air.provider}")
    private String PROVIDER;

    @ResponseBody
    @RequestMapping(value = "/getAirData.json", method = {RequestMethod.GET})
    public Map getGseAirData() throws Exception {
        AirService airService = beanFactory.getBean(PROVIDER, AirService.class);
        return Collections.singletonMap("result", airService.fetch());
    }
}
