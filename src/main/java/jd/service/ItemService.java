package jd.service;

import jd.pojo.Item;

import java.util.List;

//商品接口
public interface ItemService {

    /**
     * 保存商品
     * @param item
     */
    public void save(Item item);

    /**
     * 根据条件查询商品（看是否爬取并下载）
     * @param item
     * @return
     */
    public List<Item> findAll(Item item);
}

