package lab.paymentsoapservice;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import lab.types.*;
import org.springframework.stereotype.Service;

@Service
@WebService(serviceName = "PaymentService")
public class PaymentService {

    @WebMethod
    public PaymentResponse processPayment(PaymentRequest request) throws PaymentFault {
        if (request.getCardNumber().startsWith("2")) {
            PaymentFault fault = new PaymentFault();
            fault.setCode(400);
            fault.setText("Wrong card number");
            throw fault;
        }

        PaymentResponse response = new PaymentResponse();
        response.setApproved(true);
        return response;
    }

    @WebMethod
    public RefundResponse processRefund(RefundRequest request) {
        RefundResponse response = new RefundResponse();
        response.setSuccess(true);
        return response;
    }
}
