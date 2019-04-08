package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Test;
import org.hamcrest.core.Is;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BookKeeperTest {

    @Test
    public void invoiceWithOneElement() {

        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        ClientData clientData = new ClientData(new Id("1"), "Kowalski");
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);
        invoiceRequest.add(new RequestItem((mock(ProductData.class)), 1, new Money(new BigDecimal(10.5))));

        Invoice result = bookKeeper.issuance(invoiceRequest, (productType, net) -> new Tax(new Money(new BigDecimal(0.5)), "description"));

        assertThat(result.getItems().size(), Is.is(1));

    }

}
