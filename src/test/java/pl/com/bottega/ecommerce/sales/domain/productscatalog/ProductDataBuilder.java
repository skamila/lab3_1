package pl.com.bottega.ecommerce.sales.domain.productscatalog;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Date;

public class ProductDataBuilder {

    private Id productId;
    private Money price;
    private String name;
    private Date snapshotDate;
    private ProductType type;

    public ProductDataBuilder setProductId(Id productId) {
        this.productId = productId;
        return this;
    }

    public ProductDataBuilder setPrice(Money price) {
        this.price = price;
        return this;
    }

    public ProductDataBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ProductDataBuilder setSnapshotDate(Date snapshotDate) {
        this.snapshotDate = snapshotDate;
        return this;
    }

    public ProductDataBuilder setType(ProductType type) {
        this.type = type;
        return this;
    }

    public ProductData build(){
        return new ProductData(productId, price, name, type, snapshotDate);
    }

}
