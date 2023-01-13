package idv.module.service;

import idv.module.entity.Product;
import idv.module.repository.ProductDao;

import java.util.List;

/**
 * * ProductService. 2020/3/22 1:33 上午
 * *
 * * @author sero
 * * @version 1.0.0
 *
 **/
public class ProductService {

    ProductDao productDao;

    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    public void save(Product product) {
        productDao.save(product);
    }

    public void update(Product product) {
        productDao.update(product);
    }

    public void delete(Product product) {
        productDao.delete(product);
    }

    public List<Product> findByEnNameLike(String enName) {
        return productDao.findByEnNameLike(enName);
    }

    public Product findById(Integer id) {
        return productDao.findById(id);
    }

    public List<Product> findAll() {
        return productDao.findAll();
    }

}
