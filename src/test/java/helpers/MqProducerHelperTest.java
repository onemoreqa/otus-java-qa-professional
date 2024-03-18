package helpers;

import static com.consol.citrus.actions.SendMessageAction.Builder.send;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.testng.annotations.Test;

public class MqProducerHelperTest extends TestNGCitrusSpringSupport {
    private TestContext context;

    @Test(description = "Test MQ", enabled = false)
    @CitrusTest
    public void getTestActions() {
        this.context = citrus.getCitrusContext().createTestContext();

        send("mqHelperProducer").message().body("payload");

    }
}