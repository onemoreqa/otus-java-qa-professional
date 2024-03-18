package tests;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;

import behaviors.rest.ClientGetRequest;
import behaviors.rest.MockGetModifiedGradeResponse;
import behaviors.rest.MockGetResponse;
import behaviors.rest.MockGetResponseIfNotAllowed;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.testng.TestNGCitrusSupport;
import org.springframework.http.HttpStatus;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class RestUserTest extends TestNGCitrusSupport {

    public TestContext context;

    @CitrusTest
    @Test(testName = "Mock. Проверка получения пользователей")
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
    @Test(testName = "Mock. Проверка получения курсов")
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

    @CitrusTest
    @Test(testName = "Mock. Негативная проверка курсов")
    public void testGetCoursesNegativeMock() {

        run(applyBehavior(new ClientGetRequest("course/get/wrong", context)));
        run(applyBehavior(new MockGetResponseIfNotAllowed("/course/get/wrong", context)));

        run(http()
                .client("restClientNewMock")
                .receive()
                .response(HttpStatus.METHOD_NOT_ALLOWED)
        );
    }

    @DataProvider(name = "personGrades")
    public Object[][] cardTypeProvider() {
        return new Object[][]{
            new Object[]{"Alex", 90},
            new Object[]{"Ivan", 70},
            new Object[]{"Oleg", 40},
        };
    }

    @CitrusTest
    @Test(testName = "Mock. Проверка получения оценки", dataProvider = "personGrades")
    public void testGetGradeByNameMock(String personName, int score) {

        run(applyBehavior(new ClientGetRequest("user/get/" + personName, context)));
        run(applyBehavior(new MockGetResponse("/user/get/" + personName, "wmstub/json/" + personName + "-grade.json", context)));

        run(http()
                .client("restClientNewMock")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .name("msg")
                .validate(jsonPath()
                        .expression("$.size()", "2")
                        .expression("$[?(@.name == '" + personName + "')]['score']", score)
                        .expression("$.score", "@isNumber()@"))
        );
    }

    @CitrusTest
    @Test(testName = "Mock. Проверка получения измененной оценки")
    public void testGetGradeByNameMockModified() {

        String name = "anton";
        int score = 100;

        run(applyBehavior(new ClientGetRequest("user/get/" + name, context)));
        run(applyBehavior(new MockGetModifiedGradeResponse("/user/get/" + name, name, score, context)));

        run(http()
                .client("restClientNewMock")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .name("msg")
                .validate(jsonPath()
                        .expression("$.size()", "2")
                        .expression("$[?(@.name == '" + name + "')]['score']", 100)
                        .expression("$.score", "@isNumber()@"))
        );
    }

}
