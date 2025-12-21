package lab.types;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PaymentFault extends Exception {
    protected int code;
    protected String text;
}
