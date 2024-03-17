package behaviors;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

import com.consol.citrus.TestActionRunner;
import com.consol.citrus.TestBehavior;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.message.MessageType;
import org.springframework.core.io.ClassPathResource;

/**
 * Общий класс для GET запросов на заглушку
 * передаем {
 * @pathUri - "/rest/api/example"
 * @jsonLocation - "/path/to/stubJsonResponse"
 * }
 **/
public class MockGetResponse implements TestBehavior {

    private TestContext context;
    public String pathUri;
    public String jsonLocation;

    public MockGetResponse(String pathUri, String jsonLocation, TestContext context) {
        this.context = context;
        this.pathUri = pathUri;
        this.jsonLocation = jsonLocation;
    }
    @Override
    public void apply(TestActionRunner testActionRunner) {
        testActionRunner.run(http()
                .server("restServer")
                .receive()
                .get(pathUri)
        );

        testActionRunner.run(http()
                .server("restServer")
                .send()
                .response()
                .message()
                .type(MessageType.JSON)
                .body(new ClassPathResource(jsonLocation))
        );
    }

}
