package idv.module.repository;

import idv.module.entity.Product;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.List;

/**
 * * ProductDao. 2020/3/22 1:11 上午
 * *
 * * @author sero
 * * @version 1.0.0
 **/
public class ProductDao extends HibernateDaoSupport {

    public void save(Product product) {
        getHibernateTemplate().save(product);
    }

    public void update(Product product) {
        getHibernateTemplate().update(product);
    }

    public void delete(Product product) {
        getHibernateTemplate().delete(product);
    }

    // from的來源是model的類別名，而非資料table的名子
    public List<Product> findByEnNameLike(String enName) {
        // <?>泛型功能，為不確定傳入的物件型態，使用時需轉型。(回傳就一定是個Object物件，所以回傳可以使用Object型態的變數來儲存)
        // 直接寫List<Product>也行
        List<?> list = getHibernateTemplate().find("from Product where enName like ?", "%" + enName + "%");
        return (List<Product>) list;
    }

    public Product findById(Integer id) {
        List<Product> list = getHibernateTemplate().find("from Product where id = ?", id);
        return list.get(0);
    }

    public List<Product> findAll() {
        return (List<Product>) getHibernateTemplate().find("from Product");
    }

}
