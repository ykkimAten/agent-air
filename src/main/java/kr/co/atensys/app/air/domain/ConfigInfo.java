package kr.co.atensys.app.air.domain;

import lombok.Data;

@Data
public class ConfigInfo {
    private String config_id;
    private String parent_id;
    private String code;
    private String code_type;
    private String value;
    private String description;
    private String path;
    private String mod_date;
    private String reg_date;
}
