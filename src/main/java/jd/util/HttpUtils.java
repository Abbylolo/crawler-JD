package jd.util;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

//该注解用于创建Spring实例
@Component
public class HttpUtils {

    private PoolingHttpClientConnectionManager cm;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public HttpUtils() {
        this.cm = new PoolingHttpClientConnectionManager(); //创建HttpUtils时就会创建连接池

        //设置最大连接数
        this.cm.setMaxTotal(100);

        //设置每个主机的最大连接数
        this.cm.setDefaultMaxPerRoute(10);

    }

    /**
     * 根据请求地址下载页面数据
     * @param url
     * @return 页面数据
     */
    public String doGetHtml(String url){
        //获取httpClient对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(this.cm).build();

        //创建httpGet请求对象，设置url地址
        HttpGet httpGet = new HttpGet(url);

        //httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");

        //设置头部信息进行模拟登录（添加登录后的Cookie 存在客户端的（session存在服务端） 模拟用户登录）

        /**
         * User-Agent反爬
         */

        httpGet.addHeader("User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.87 Safari/537.36");
        // Request配置（比如请求超时如何处理。连接数有限，不可能无节制等待）

        logger.info("请求地址:{}", httpGet);

        //设置请求信息
        httpGet.setConfig(this.getConfig());

        CloseableHttpResponse response = null;

        try {
            //使用httpClient发起请求，获取响应
            response = httpClient.execute(httpGet);

            //解析响应，返回结果
            if(response.getStatusLine().getStatusCode() == 200) {
                //判断响应体Entity是否为空，如果不为空可以使用EntityUtils
                if(response.getEntity() != null){
                    String content = EntityUtils.toString(response.getEntity());
                    logger.info("响应的数据:{}", content);
                    return content;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭response(httpClient由连接池管理，我们不必关闭）
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //无数据时返回空串
        return "";
    }


    /**
     * 下载图片
     * @param url
     * @return 图片名称
     */
    public String doGetImage(String url){
        //获取httpClient对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(this.cm).build();

        //创建httpGet请求对象，设置url地址
        HttpGet httpGet = new HttpGet(url);

        //设置请求信息
        httpGet.setConfig(this.getConfig());

        CloseableHttpResponse response = null;

        try {
            //使用httpClient发起请求，获取响应
            response = httpClient.execute(httpGet);

            //解析响应，返回结果
            if(response.getStatusLine().getStatusCode() == 200) {
                //判断响应体Entity是否为空，如果不为空可以使用EntityUtils
                if(response.getEntity() != null){
                    //下载图片

                    //获取图片的后缀
                    String extName = url.substring(url.lastIndexOf("."));

                    //创建图片名，重命名图片
                    String picName = UUID.randomUUID().toString()+extName; //UUID.randomUUID()-生成唯一识别码

                    //下载图片
                    //声明OutPutStream
                    OutputStream outPutStream = new FileOutputStream(new File("E:\\Project\\Crawl\\outcome\\jingdong\\"+picName));
                    response.getEntity().writeTo(outPutStream);

                    //返回图片名称
                    return picName;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭response(httpClient由连接池管理，我们不必关闭）
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //下载失败时返回空串
        return "";
    }

    //设置请求信息
    private RequestConfig getConfig() {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(1000)  //创建连接的最长时间
                .setConnectionRequestTimeout(500) //获取连接的最长时间
                .setSocketTimeout(10000)  //数据传输的最长时间
                .build();
        return config;
    }
}
