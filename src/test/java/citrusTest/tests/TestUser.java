package citrusTest.tests;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;

import behaviors.ClientGetRequest;
import behaviors.MockGetResponse;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.testng.TestNGCitrusSupport;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

public class TestUser extends TestNGCitrusSupport {

    public TestContext context;

    @CitrusTest
    @Test
    public void testGetUsersMock() {

        run(applyBehavior(new ClientGetRequest("user/get/all", context)));
        run(applyBehavior(new MockGetResponse("/user/get/all", "wmstub/json/users.json", context)));

        run(http()
                .client("restClientNewMock")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .name("msg")
                .validate(jsonPath()
                        .expression("$[0].name", "Oleg")
                        .expression("$[1].age", "@greaterThan(20)@")
                        .expression("$[2].age", "@isNumber()@")
                        .expression("$[0].email", "@matches('^\\S+@\\S+\\.\\S+$')@")
                        .expression("$.size()", "3")
                        .expression("$[?(@.name == 'Ivan')]['course']", "QA middle"))
        );

    }

    @CitrusTest
    @Test
    public void testGetCoursesMock() {

        run(applyBehavior(new ClientGetRequest("course/get/all", context)));

        run(applyBehavior(new MockGetResponse("/course/get/all", "wmstub/json/courses.json", context)));

        run(http()
                .client("restClientNewMock")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .name("msg")
                .validate(jsonPath()
                        .expression("$.size()", "2")
                        .expression("$[?(@.name == 'Java')]['price']", 12000)
                        .expression("$[0].price", "@isNumber()@"))
        );

    }
}
