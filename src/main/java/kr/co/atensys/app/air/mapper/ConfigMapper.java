package kr.co.atensys.app.air.mapper;

import kr.co.atensys.app.air.domain.ConfigInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ConfigMapper {
    List<ConfigInfo> configList();
    int configInsert(ConfigInfo info);
    int configUpdate(ConfigInfo info);
    int configDelete(ConfigInfo info);
    ConfigInfo getConfig(@Param("path") String codePath);
}
