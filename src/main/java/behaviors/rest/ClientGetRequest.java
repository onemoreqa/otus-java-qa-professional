package behaviors.rest;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

import com.consol.citrus.TestActionRunner;
import com.consol.citrus.TestBehavior;
import com.consol.citrus.context.TestContext;

/**
 * Общий класс для GET запросов вызова клиентом {
 * @pathUri - "/rest/api/example"
 * }
 **/
public class ClientGetRequest implements TestBehavior {

    private TestContext context;
    public String pathUri;

    public ClientGetRequest(String pathUri, TestContext context) {
        this.context = context;
        this.pathUri = pathUri;
    }
    @Override
    public void apply(TestActionRunner testActionRunner) {
        testActionRunner.run(http()
                .client("restClientNewMock")
                .send()
                .get(pathUri)
                .fork(true)
        );
    }

}
