package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Before;
import org.junit.Test;
import org.hamcrest.core.Is;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.*;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.*;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class) public class BookKeeperTest {

    private BookKeeper bookKeeper;
    private InvoiceRequest invoiceRequest;
    private RequestItem requestItem;
    @Mock TaxPolicy taxPolicyMock;

    @Before public void initialize() {

        bookKeeper = new BookKeeper(new InvoiceFactory());
        ClientData clientData = new ClientData(Id.generate(), getString());
        invoiceRequest = new InvoiceRequest(clientData);
        Product product = new Product(Id.generate(), Money.ZERO, getString(), ProductType.STANDARD);
        ProductData productData = product.generateSnapshot();
        requestItem = new RequestItem(productData, 1, Money.ZERO);

    }

    @Test public void invoiceWithoutElements() {

        Invoice result = bookKeeper.issuance(invoiceRequest, (productType, net) -> new Tax(new Money(new BigDecimal(0.5)), "description"));
        assertThat(result.getItems().size(), Is.is(0));

    }

    @Test public void invoiceWithOneElement() {

        invoiceRequest.add(requestItem);
        Invoice result = bookKeeper.issuance(invoiceRequest, (productType, net) -> new Tax(new Money(new BigDecimal(0.5)), "description"));
        assertThat(result.getItems().size(), Is.is(1));

    }

    @Test public void amountOfCallMethodCalculateTaxWhenInvoiceHasTwoElements() {

        invoiceRequest.add(requestItem);
        invoiceRequest.add(requestItem);
        when(taxPolicyMock.calculateTax(any(ProductType.class), any(Money.class))).thenReturn(new Tax(Money.ZERO, getString()));

        bookKeeper.issuance(invoiceRequest, taxPolicyMock);

        verify(taxPolicyMock, times(2)).calculateTax(any(ProductType.class), any(Money.class));

    }

    @Test public void amountOfCallMethodCalculateTaxWhenInvoiceHasNotElements() {

        when(taxPolicyMock.calculateTax(any(ProductType.class), any(Money.class))).thenReturn(new Tax(Money.ZERO, getString()));

        bookKeeper.issuance(invoiceRequest, taxPolicyMock);

        verify(taxPolicyMock, times(0)).calculateTax(any(ProductType.class), any(Money.class));

    }

    private String getString() {
        return "string";
    }

}
