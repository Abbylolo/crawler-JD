package jd.dao;

import jd.pojo.Item;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 爬虫数据持久化
 * DAO (DataAccessobjects 数据存取对象)是指位于业务逻辑和持久化数据之间实现对持久化数据的访问。通俗来讲，就是将数据库操作都封装起来。
 */
public interface ItemDao extends JpaRepository<Item,Long> { //JpaRepository<操作哪种POJO,POJO中id的数据类型>

}
