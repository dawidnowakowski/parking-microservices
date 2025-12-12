package lab.types;

import lombok.Data;

@Data
public class PaymentRequest {
    String cardNumber;
    String cvv;
    Double amount;
}
