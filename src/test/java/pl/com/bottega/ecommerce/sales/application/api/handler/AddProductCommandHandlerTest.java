package pl.com.bottega.ecommerce.sales.application.api.handler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.*;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.domain.client.*;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.*;
import pl.com.bottega.ecommerce.sales.domain.reservation.*;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import pl.com.bottega.ecommerce.system.application.SystemContext;

import java.util.Date;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class AddProductCommandHandlerTest {

    private AddProductCommandHandler handler;
    private AddProductCommand command;
    @Mock private ReservationRepository reservationRepositoryMock;
    @Mock private ProductRepository productRepositoryMock;
    @Mock private SuggestionService suggestionServiceMock;
    @Mock private ClientRepository clientRepositoryMock;

    @Before public void init() {

        MockitoAnnotations.initMocks(this);

        when(reservationRepositoryMock.load(any(Id.class))).thenReturn(new Reservation(Id.generate(),
                Reservation.ReservationStatus.OPENED, new ClientData(Id.generate(), getExemplaryString()), new Date()));
        when(productRepositoryMock.load(any(Id.class))).thenReturn(new Product(Id.generate(),
                new Money(10), getExemplaryString(), ProductType.STANDARD));
        when(suggestionServiceMock.suggestEquivalent(any(Product.class), any(Client.class)))
                .thenReturn(new Product(Id.generate(), new Money(10),
                        getExemplaryString(), ProductType.STANDARD));
        when(clientRepositoryMock.load(any(Id.class))).thenReturn(new Client());

        handler = new AddProductCommandHandler(reservationRepositoryMock, productRepositoryMock,
                suggestionServiceMock, clientRepositoryMock, new SystemContext());
        command = new AddProductCommand(Id.generate(), Id.generate(), 0);

    }

    @Test public void methodHandleShouldCallMethodSuggestEquivalentIfProductIsNotAvailable() {

        Product product = new Product(Id.generate(), new Money(10),
                getExemplaryString(), ProductType.STANDARD);
        product.markAsRemoved();
        when(productRepositoryMock.load(any(Id.class))).thenReturn(product);

        handler.handle(command);

        verify(suggestionServiceMock, times(1)).suggestEquivalent(any(Product.class), any(Client.class));

    }

    @Test public void methodHandleShouldNotCallMethodSuggestEquivalentIfProductIsAvailable() {

        handler.handle(command);

        verify(suggestionServiceMock, times(0)).suggestEquivalent(any(Product.class), any(Client.class));

    }

    @Test public void methodHandleShouldSaveReservation() {

        handler.handle(command);

        verify(reservationRepositoryMock, times(1)).save(any(Reservation.class));

    }

    private String getExemplaryString() {
        return "Ala ma kota.";
    }

}