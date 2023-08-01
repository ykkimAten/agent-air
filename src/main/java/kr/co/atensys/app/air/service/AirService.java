package kr.co.atensys.app.air.service;

import kr.co.atensys.app.air.domain.AirData;

public interface AirService {
    AirData fetch() throws Exception;
}
