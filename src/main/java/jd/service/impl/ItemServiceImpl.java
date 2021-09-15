package jd.service.impl;

import jd.dao.ItemDao;
import jd.pojo.Item;
import jd.service.ItemService;
import org.hibernate.criterion.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
//由Spring来创建它的实例
public class ItemServiceImpl implements ItemService {

    //用DAO保存数据
    @Autowired
    private ItemDao itemDao;

    @Override
    @Transactional  //开启提交事务
    public void save(Item item){
        this.itemDao.save(item);
    }

    @Override
    public List<Item> findAll(Item item) {
        //声明查询条件
        org.springframework.data.domain.Example<Item> example = org.springframework.data.domain.Example.of(item);

        //根据查询条件进行查询数据
        List<Item> list = this.itemDao.findAll(example);

        return list;
    }
}
