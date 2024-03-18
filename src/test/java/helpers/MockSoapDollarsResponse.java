package helpers;

import static com.consol.citrus.ws.actions.SoapActionBuilder.soap;

import com.consol.citrus.TestActionRunner;
import com.consol.citrus.TestBehavior;
import com.consol.citrus.context.TestContext;
import feature.CustomMarshaller;
import pojo.xml.com.dataaccess.webservicesserver.NumberToDollars;
import pojo.xml.com.dataaccess.webservicesserver.NumberToDollarsResponse;

import java.math.BigDecimal;

public class MockSoapDollarsResponse implements TestBehavior {

    private TestContext context;
    public String namespaceUri;
    public String dollarCount;
    public String dollarString;

    public MockSoapDollarsResponse(String namespaceUri, String dollarCount, String dollarString, TestContext context) {
        this.context = context;
        this.namespaceUri = namespaceUri;
        this.dollarCount = dollarCount;
        this.dollarString = dollarString;
    }

    @Override
    public void apply(TestActionRunner testActionRunner) {

        CustomMarshaller<Class<NumberToDollars>> ptxRq = new CustomMarshaller<>();
        CustomMarshaller<Class<NumberToDollarsResponse>> ptxRs = new CustomMarshaller<>();
        testActionRunner.run(soap()
                .client("soapClient")
                .send()
                .message()
                .body(ptxRq.convert(NumberToDollars.class, getNumberToDollarsRequest(),
                        namespaceUri, "NumberToDollars")));

        testActionRunner.run(soap()
                .client("soapClient")
                .receive()
                .message()
                .body(ptxRs.convert(NumberToDollarsResponse.class, getNumberToDollarsResponse(),
                        namespaceUri, "NumberToDollarsResponse")));
    }


    public NumberToDollars getNumberToDollarsRequest() {
        NumberToDollars numberToDollars = new NumberToDollars();
        numberToDollars.setDNum(new BigDecimal(dollarCount));
        return numberToDollars;
    }

    public NumberToDollarsResponse getNumberToDollarsResponse() {
        NumberToDollarsResponse numberToDollarsResponse = new NumberToDollarsResponse();
        numberToDollarsResponse.setNumberToDollarsResult(dollarString);
        return numberToDollarsResponse;
    }

}