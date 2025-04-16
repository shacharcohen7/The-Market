package DomainLayer.PaymentServices;

import Util.PaymentDTO;

public interface HttpClient {
    boolean checkCreditCard(String url, PaymentDTO paymentDTO);
}

