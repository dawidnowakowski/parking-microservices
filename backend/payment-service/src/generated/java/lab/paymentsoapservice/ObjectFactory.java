
package lab.paymentsoapservice;

import javax.xml.namespace.QName;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the lab.paymentsoapservice package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ProcessPayment_QNAME = new QName("http://paymentsoapservice.lab/", "processPayment");
    private final static QName _ProcessPaymentResponse_QNAME = new QName("http://paymentsoapservice.lab/", "processPaymentResponse");
    private final static QName _PaymentFault_QNAME = new QName("http://paymentsoapservice.lab/", "PaymentFault");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: lab.paymentsoapservice
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ProcessPayment }
     * 
     */
    public ProcessPayment createProcessPayment() {
        return new ProcessPayment();
    }

    /**
     * Create an instance of {@link ProcessPaymentResponse }
     * 
     */
    public ProcessPaymentResponse createProcessPaymentResponse() {
        return new ProcessPaymentResponse();
    }

    /**
     * Create an instance of {@link PaymentFault }
     * 
     */
    public PaymentFault createPaymentFault() {
        return new PaymentFault();
    }

    /**
     * Create an instance of {@link PaymentRequest }
     * 
     */
    public PaymentRequest createPaymentRequest() {
        return new PaymentRequest();
    }

    /**
     * Create an instance of {@link PaymentResponse }
     * 
     */
    public PaymentResponse createPaymentResponse() {
        return new PaymentResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProcessPayment }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ProcessPayment }{@code >}
     */
    @XmlElementDecl(namespace = "http://paymentsoapservice.lab/", name = "processPayment")
    public JAXBElement<ProcessPayment> createProcessPayment(ProcessPayment value) {
        return new JAXBElement<ProcessPayment>(_ProcessPayment_QNAME, ProcessPayment.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProcessPaymentResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ProcessPaymentResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://paymentsoapservice.lab/", name = "processPaymentResponse")
    public JAXBElement<ProcessPaymentResponse> createProcessPaymentResponse(ProcessPaymentResponse value) {
        return new JAXBElement<ProcessPaymentResponse>(_ProcessPaymentResponse_QNAME, ProcessPaymentResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PaymentFault }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link PaymentFault }{@code >}
     */
    @XmlElementDecl(namespace = "http://paymentsoapservice.lab/", name = "PaymentFault")
    public JAXBElement<PaymentFault> createPaymentFault(PaymentFault value) {
        return new JAXBElement<PaymentFault>(_PaymentFault_QNAME, PaymentFault.class, null, value);
    }

}
