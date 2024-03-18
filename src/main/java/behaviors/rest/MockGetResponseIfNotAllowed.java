package behaviors.rest;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

import com.consol.citrus.TestActionRunner;
import com.consol.citrus.TestBehavior;
import com.consol.citrus.context.TestContext;
import org.springframework.http.HttpStatus;

public class MockGetResponseIfNotAllowed implements TestBehavior {

    private TestContext context;
    public String pathUri;

    public MockGetResponseIfNotAllowed(String pathUri, TestContext context) {
        this.context = context;
        this.pathUri = pathUri;
    }

    /**
     * Мок для недопустимого вызова
     **/
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
                .response(HttpStatus.METHOD_NOT_ALLOWED)
        );
    }

}
