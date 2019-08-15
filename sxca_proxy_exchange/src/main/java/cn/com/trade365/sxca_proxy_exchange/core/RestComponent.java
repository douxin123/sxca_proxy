package cn.com.trade365.sxca_proxy_exchange.core;

import cn.com.trade365.sxca_proxy_exchange.entity.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


/**
 * http rest请求
 * @author :lhl
 * @create :2018-11-06 08:34
 */
@Component
public class RestComponent {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * http 请求
     *
     * @param url    请求地址
     * @param requestObject   请求数据
     * @param method 请求方法
     * @return 结果
     * @throws Exception
     */
    public <T> ResultData<T> request(String url, HttpMethod method, Object requestObject, ParameterizedTypeReference<ResultData<T>> parameterizedTypeReference)
            throws Exception {
        return restTemplate.exchange(url, method, new HttpEntity<Object>(requestObject), parameterizedTypeReference).getBody();
    }


    /**
     * post请求
     * @param url    请求地址
     * @param requestObject   请求数据
     * @return 结果
     * @throws Exception
     */
    public <T> ResultData<T> post(String url,Object requestObject,ParameterizedTypeReference<ResultData<T>> parameterizedTypeReference) throws Exception {
        return request(url, HttpMethod.POST,requestObject,parameterizedTypeReference);
    }

    /**
     * get请求
     * @param url    请求地址
     * @param requestObject   请求数据
     * @return 结果
     * @throws Exception
     */
    public <T> ResultData<T> get(String url,Object requestObject,ParameterizedTypeReference<ResultData<T>> parameterizedTypeReference) throws Exception {
        return request(url, HttpMethod.GET,requestObject,parameterizedTypeReference);
    }

    /**
     * delete请求
     * @param url    请求地址
     * @param requestObject   请求数据
     * @return 结果
     * @throws Exception
     */
    public <T> ResultData<T> delete(String url,Object requestObject,ParameterizedTypeReference<ResultData<T>> parameterizedTypeReference) throws Exception {
        return request(url, HttpMethod.DELETE,requestObject,parameterizedTypeReference);
    }

    /**
     * put请求
     * @param url    请求地址
     * @param requestObject   请求数据
     * @return 结果
     * @throws Exception
     */
    public <T> ResultData<T> put(String url,Object requestObject,ParameterizedTypeReference<ResultData<T>> parameterizedTypeReference) throws Exception {
        return request(url, HttpMethod.PUT,requestObject,parameterizedTypeReference);
    }


}
