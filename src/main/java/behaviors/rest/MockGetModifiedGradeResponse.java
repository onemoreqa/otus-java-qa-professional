package behaviors.rest;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

import com.consol.citrus.TestActionRunner;
import com.consol.citrus.TestBehavior;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import pojo.http.GradeDto;

/**
 * Класс для GET запросов на измененную заглушку
 * передаем {
 * @pathUri - "/rest/api/example"
 * @newName - "Anton"
 * @newScore - 100
 * }
 **/
public class MockGetModifiedGradeResponse implements TestBehavior {

    private TestContext context;
    public String pathUri;
    public String newName;
    public int newScore;

    public MockGetModifiedGradeResponse(String pathUri, String newName, int newScore, TestContext context) {
        this.context = context;
        this.pathUri = pathUri;
        this.newName = newName;
        this.newScore = newScore;
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
                .body(new ObjectMappingPayloadBuilder(getGradeDto(), "objectMapper"))
        );
    }

    public GradeDto getGradeDto() {
        return getGradeDto(newName, newScore);
    }
    public GradeDto getGradeDto(String someName, int someScore) {
        GradeDto gradeDto = new GradeDto();
        gradeDto.setName(someName);
        gradeDto.setScore(someScore);
        return gradeDto;
    }

}
