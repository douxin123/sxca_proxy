package cn.com.trade365.sxca_proxy_exchange.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

/**
 * @author :lhl
 * @create :2018-11-06 13:59
 */
@Configuration
@ConfigurationProperties(prefix = "spring.datasource.druid")
public class DruidDataSourceConfig {


    private static String url;

    private static String username;

    private static String password;

    @Value("spring.datasource.druid.driver-class-name")
    private static String driverClassName;

    private static int initialSize;

    private static int minIdle;

    private static int maxActive;

    private static int maxWait;


    /**
     * 配置DataSource
     * @return
     * @throws SQLException
     */
    @Bean
    public DruidDataSource druidDataSource() throws SQLException {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        druidDataSource.setUrl(url);
        druidDataSource.setFilters("stat,wall");
//        druidDataSource.setInitialSize(initialSize);
//        druidDataSource.setMinIdle(minIdle);
//        druidDataSource.setMaxActive(maxActive);
//        druidDataSource.setMaxWait(maxWait);
//        druidDataSource.setUseGlobalDataSourceStat(true);
        druidDataSource.setDriverClassName(driverClassName);
        return druidDataSource;
    }

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        DruidDataSourceConfig.url = url;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        DruidDataSourceConfig.username = username;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        DruidDataSourceConfig.password = password;
    }

    public static int getInitialSize() {
        return initialSize;
    }

    public static void setInitialSize(int initialSize) {
        DruidDataSourceConfig.initialSize = initialSize;
    }

    public static int getMinIdle() {
        return minIdle;
    }

    public static void setMinIdle(int minIdle) {
        DruidDataSourceConfig.minIdle = minIdle;
    }

    public static int getMaxActive() {
        return maxActive;
    }

    public static void setMaxActive(int maxActive) {
        DruidDataSourceConfig.maxActive = maxActive;
    }

    public static int getMaxWait() {
        return maxWait;
    }

    public static void setMaxWait(int maxWait) {
        DruidDataSourceConfig.maxWait = maxWait;
    }
}
