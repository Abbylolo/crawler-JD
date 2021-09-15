package jd.task;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jd.pojo.Item;
import jd.service.ItemService;
import jd.util.HttpUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Component
public class ItemTask {

    @Autowired
    private HttpUtils httpUtils;

    @Autowired
    private ItemService itemService;

    private static final ObjectMapper MAPPER = new ObjectMapper(); //解析json的工具类

    //当下载任务完成后，间隔多长时间进行下一次任务
    @Scheduled(fixedDelay = 100*1000 )
    public void itemTask() throws Exception{
        //声明需要解析的初始地址
        for(int i = 1; i <= 10; i++) {
            String url = "https://search.jd.com/Search?keyword=%E6%89%8B%E6%9C%BA&wq=%E6%89%8B%E6%9C%BA&s=116&click=0&page=";

            //按照页面对手机的搜索结果进行遍历解析
            String html = httpUtils.doGetHtml(url+1);

            //解析页面，获取商品数据并存储
            this.parse(html);
        }




        System.out.println("手机数据抓取完成！");
    }

    //解析页面，获取商品数据并存储
    private void parse(String html) throws IOException {
        //解析html，获取dom对象
        Document doc = Jsoup.parse(html);

        //获取spu
        Elements spuEles = doc.select("div#J_goodsList>ul>li");

        for (Element spuEle : spuEles) {
            //获取spu
            String spu = spuEle.attr("data-spu");

            //获取sku
            Elements skuEles = spuEle.select("li.ps-item");
            for (Element skuEle : skuEles) {
                String sku = skuEle.select("[data-sku]").attr("data-sku");

                //根据sku查询商品数据
                Item item = new Item();
                item.setSku(sku);
                List<Item> list = this.itemService.findAll(item);

                if(list.size()>0) {
                    //如果商品存在则进行下一个循环，不保存该商品，因为已存在
                    continue;
                }

                //设置商品的spu
                item.setSpu(spu);

                //获取商品的详情的url【分析网页得url为固定地址+商品spu组合而成】
                String itemUrl = "https://item.jd.com/"+spu+".html";
                item.setUrl(itemUrl);

                //获取商品图片
                String picUrl = "https:"+skuEle.select("img[data-sku]").first().attr("data-lazy-img");
                picUrl = picUrl.replace("/n9/","/n1/"); //图片大小从n9改到n1，进行放大
                String picName = this.httpUtils.doGetImage(picUrl);
                item.setPic(picName);

                //获取商品价格
                String priceJsom = this.httpUtils.doGetHtml("https://p.3.cn/prices/mgets?skuIds=J_"+sku); //分析网页知它的内容是json格式
                double price = MAPPER.readTree(priceJsom).get(0).get("p").asDouble();
                item.setPrice(price);

                //获取商品标题
                String itemInfo = httpUtils.doGetHtml(item.getUrl());
                Document itemDoc = Jsoup.parse(itemUrl);//解析该html页面
                String title = itemDoc.select("div.sku-name").text();
                item.setTitle(title);

                //创建时间
                item.setCreated(new Date());

                //更新时间(即创建时间)
                item.setUpdated(item.getCreated());

                //保存商品数据到数据库中
                this.itemService.save(item);

            }
        }

    }

}
