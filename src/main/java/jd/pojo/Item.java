package jd.pojo;

//POJO（Plain Ordinary Java Object）简单的Java对象，实际就是普通JavaBeans
import javax.persistence.*;
import java.util.Date;

/*
@Entity注解和@Table注解都是Java Persistence API中定义的一种注解。。
*/

@Entity
/*
@Entity注解指明这是一个实体Bean。任何Hibernate映射对象都要有这个注释。
@Entity说明这个class是实体类，并且使用默认的orm规则，即class名就是数据库表中表名，class字段名即表中字段名。可以用@Entity(name = "jd_item")指定表名称。
*/


@Table(name = "jd_item")
/*
声明此对象映射到数据库的数据表，通过它可以为实体指定表(talbe),目录(Catalog)和schema的名字。该注释不是必须的，如果没有则系统使用默认值(实体的短类名)。
*/

/*
如果同时使用了@Entity(name="student")和@Table(name="book")，最终对应的表名是book，这说明优先级：@Table>@Entity
*/

public class Item {
    //主键
    @Id    //所有的实体类都要有主键，@Id注解表示该属性是一个主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) //IDENTITY：主键由数据库自动生成（主要是自动增长型）||@GeneratedValue注解表示注解自动生成，strategy则表示主键的生成策略
    private  Long id;
    //标准产品单位（商品集合）
    private String spu;
    //库存量单位（最小品类单元）
    @Column(name = "sku") //默认情况下，生成的表中字段的名称就是实体类中属性的名称，通过@Column注解可以定制生成的字段属性，name表示该属性对应的数据表中字段的名称，nullable表示该字段非空
    private String sku;
    //商品标题
    private String title;
    //商品价格
    private  Double price;
    //商品图片
    private  String pic;
    //商品详情地址
    private  String url;
    //创建时间
    private Date created;
    //更新时间
    private  Date updated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpu() {
        return spu;
    }

    public void setSpu(String spu) {
        this.spu = spu;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}
