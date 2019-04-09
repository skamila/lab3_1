package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Before;
import org.junit.Test;
import org.hamcrest.core.Is;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.*;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.*;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class) public class BookKeeperTest {

    private BookKeeper bookKeeper;
    private InvoiceRequest invoiceRequest;
    private RequestItem requestItem;
    private ClientData clientData;
    @Mock TaxPolicy taxPolicyMock;

    @Before public void initialize() {

        ProductData productData = (new Product(Id.generate(), Money.ZERO, getString(), ProductType.STANDARD)).generateSnapshot();
        bookKeeper = new BookKeeper(new InvoiceFactory());
        clientData = new ClientData(Id.generate(), getString());
        invoiceRequest = new InvoiceRequest(clientData);
        requestItem = new RequestItem(productData, 1, Money.ZERO);
        when(taxPolicyMock.calculateTax(any(ProductType.class), any(Money.class))).thenReturn(new Tax(Money.ZERO, getString()));

    }

    @Test public void invoiceWithoutElements() {

        Invoice result = bookKeeper.issuance(invoiceRequest, taxPolicyMock);

        assertThat(result.getItems().size(), Is.is(0));

    }

    @Test public void invoiceWithOneElement() {

        invoiceRequest.add(requestItem);

        Invoice result = bookKeeper.issuance(invoiceRequest, taxPolicyMock);

        assertThat(result.getItems().size(), Is.is(1));

    }

    @Test public void amountOfCallMethodCalculateTaxWhenInvoiceHasTwoElements() {

        invoiceRequest.add(requestItem);
        invoiceRequest.add(requestItem);

        bookKeeper.issuance(invoiceRequest, taxPolicyMock);

        verify(taxPolicyMock, times(2)).calculateTax(any(ProductType.class), any(Money.class));

    }

    @Test public void amountOfCallMethodCalculateTaxWhenInvoiceHasNotElements() {

        bookKeeper.issuance(invoiceRequest, taxPolicyMock);

        verify(taxPolicyMock, times(0)).calculateTax(any(ProductType.class), any(Money.class));

    }

    @Test public void clientOnInvoice() {

        invoiceRequest.add(requestItem);

        Invoice result = bookKeeper.issuance(invoiceRequest, taxPolicyMock);

        assertThat(result.getClient(), Is.is(clientData));

    }

    @Test public void calculateTaxParameters() {

        invoiceRequest.add(requestItem);
        ArgumentCaptor<ProductType> productTypeCaptor = ArgumentCaptor.forClass(ProductType.class);

        bookKeeper.issuance(invoiceRequest, taxPolicyMock);

        verify(taxPolicyMock).calculateTax(productTypeCaptor.capture(), Matchers.anyObject());
        assertThat(productTypeCaptor.getValue(), Is.is(ProductType.STANDARD));

    }

    private String getString() {
        return "string";
    }

}
