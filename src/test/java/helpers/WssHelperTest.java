package helpers;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.testng.annotations.Test;
import pojo.wss.ErrorResponse;
import pojo.wss.WssError;

import static com.consol.citrus.actions.ReceiveMessageAction.Builder.receive;
import static com.consol.citrus.actions.SendMessageAction.Builder.send;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class WssHelperTest extends TestNGCitrusSpringSupport {

    private TestContext context;

    @Test(description = "Description")
    @CitrusTest
    public void getTestActionsWSS(){
        this.context = citrus.getCitrusContext().createTestContext();
        context.setVariable("payload", "none");

//        send("wssHelperClient").fork(true);
//
//        receive("wssHelperClient")
//                .validate(MessageType.JSON)
//                        .validate()
//
//
//        receive(action -> action
//                        .endpoint("wssHelperClient")
//                .messageType(MessageType.JSON)
//                .extractFromPayload("$.*", "payload")
//                .payload(getJsonDataResponseWSS(), "objectMapper")
//        );
//        echo("############: ${payload}");
    }

    public ErrorResponse getJsonDataResponseWSS(){
        ErrorResponse errorResponse = new ErrorResponse();

        WssError error = new WssError();
        error.setCode(3);
        error.setMsg("Invalid JSON: EOF while parsing a value at line 1 column 0");
        errorResponse.setError(error);

        return errorResponse;
    }


}
