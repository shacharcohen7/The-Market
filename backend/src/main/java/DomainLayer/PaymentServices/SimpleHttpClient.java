package DomainLayer.PaymentServices;

import Util.PaymentDTO;

public class SimpleHttpClient implements HttpClient {
    @Override
    public boolean checkCreditCard(String url, PaymentDTO paymentDTO) {
        // Implement the HTTP GET logic here
        // For now, return a dummy response
        return true;
    }
}
