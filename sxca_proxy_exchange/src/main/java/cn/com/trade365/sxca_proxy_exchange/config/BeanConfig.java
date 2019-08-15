package cn.com.trade365.sxca_proxy_exchange.config;

import cn.com.trade365.store.Client;
import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

/**
 * @author :lhl
 * @create :2018-11-06 10:50
 */
@Configuration
public class BeanConfig {

	@Value("${oss.file.url}")
    private String fileUrl;

    @Value("${oss.system.id}")
    private String systemId;

    @Value("${file.path}")
    private String filePath;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(Charset.forName("utf-8")));
        return restTemplate;
    }

    @Bean
    public ApplicationContext applicationContext() {
        return new StaticApplicationContext();
    }


    @Bean
    public DruidDataSource druidDataSource() {
        return new DruidDataSource();
    }

    @Bean
    public Client fileStoreClient() {
        return new Client(fileUrl, systemId);
    }

    public String getFileUrl() {
		return fileUrl;
	}

	public String getSystemId() {
		return systemId;
	}

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
