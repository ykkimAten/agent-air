# 개요
(주)에이텐시스템 공기질 서비스 Agent 프로젝트

# git 저장소
https://github.com/atensystem/agent-air

# 개발환경

##App 환경 설정
resources/application.yml 에서 개발 환경에 맞게 수정한다.  

# JDK 버전
openJdk 11

# 공기질연동 연동 구현 방법
AirService 인터페이스 구현한다.

```java
@Lazy
@Service("customerAirService")
public class CustomerAirServicre implements AirService {
    @Override
    public AirData fetch() throws Exception {
    }
}
```

application.yml 에 구현체 이름을 적어준다.
```yaml
air.provider: customerAirProvider
```

application.yml 에 smartOfficeUrl 을 셋팅한다.

```yaml
air.url: http://airurl
```

application.yml 에 port 설정을 해준다.

```yaml
server:
  port: 8091
```

# 빌드
```
$ ./gradlew clean
$ ./gradlew bootJar
```

build/libs/agent-air-0.0.1-SNAPSHOT.jar를 운영서버에 업로드한다.
* AGENT_HOME: /home/atensys/app/agent-air
* jar 경로: ${AGENT_HOME}/agent-air.jar

실행에 필요한 shell-script 파일 업로드
script/startAgent.sh와 stopAgent.sh를 ${AGENT_HOME}으로 복사

실행 권한 주기
```shell
$ chmod +x startAgent.sh
$ chmod +x stopAgent.sh
```

shell-script는 bash용으로 작성되어있습니다.
운영 환경의 bash 경로를 확인하여 /bin/bash 경로가 아니면 ```$ which bash``` 명령 수행결과 확인된 경로로 변경되어야 합니다.

예: /usr/bin/bash인 경우
```shell
#!/usr/bin/bash
...
```

application.yml 파일을 /home/atensys/app/agent-air 경로에 업로드 후 환경에 맞게 내용을 수정합니다.

# 실행



## Agent 시작
${AGENT_HOME} 경로 기준

### 시작
```shell
$ ./startAgent.sh
```

### 중지

```shell
$ ./stopAgent.sh
```


# FAQ

## JDBC 연결을 추가하고 싶은 경우?
application.yml 에 JDBC 연결을 아래와 같이 추가합니다.

```yaml
spring:
  datasource:
    atensys:
      customer:
        username: atensys
        password: aten3360
        driver-class-name: org.mariadb.jdbc.Driver
        jdbc-url: jdbc:mariadb://127.0.0.1:13306/lfc?allowMultiQueries=true&amp;autoReconnect=true
```

springboot 고객사 JDBC 설정용 .java 를 작성합니다.

```java
package kr.co.atensys.app.sync.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * 샘플 JDBC 설정
 * 
 * - mapper 클래스는 src/main/java/kr/co/atensys/customer/mapper 경로에 생성
 * - mapper xml 파일은 src/resources/mapper/customer 하위에 생성
 * 
 */
@Configuration
@MapperScan(value = "kr.co.atensys.customer.mapper", sqlSessionFactoryRef = "customerFactory")
public class CustomerDataSourceConfig {

    @Bean(name = "customerDatasource")
    @ConfigurationProperties(prefix = "spring.datasource.customer")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "customerFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("customerDatasource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.setTypeAliasesPackage("kr.co.atensys.customer");
        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/mapper/customer/*.xml"));
        return sqlSessionFactory.getObject();
    }

    @Bean(name = "customerSqlSession")
    public SqlSessionTemplate sqlSession(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}

```


