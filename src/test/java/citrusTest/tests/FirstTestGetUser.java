package citrusTest.tests;

import behaviors.reqres.CreateUserRequest;
import pojo.http.Data;
import pojo.http.Support;
import pojo.http.User;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

import static com.consol.citrus.actions.EchoAction.Builder.echo;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class FirstTestGetUser extends TestNGCitrusSpringSupport {

    private TestContext context;

    @Test(description = "Получение информации о пользователе")
    @CitrusTest
    public void getTestActions() {
        this.context = citrus.getCitrusContext().createTestContext();

//        context.setVariable("value", "superValue");
//        $(echo("Property \"value\" = " + context.getVariable("value")));
//
//        $(echo("We have userId = "+ context.getVariable("userId")));
//        $(echo("Property \"userId\" = ${userId}"));
//
//        variable("now", "citrus:currentDate()");
//        $(echo("Today is: ${now}"));

        run(applyBehavior(new CreateUserRequest("Mike", "Worker", context)));

        run(http()
                .client("restClientReqres")
                .send()
                .get("users/" + context.getVariable("userId")));

        run(http()
                .client("restClientReqres")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .body(new ClassPathResource("wmstub/json/janet.json"))
        );
    }

}
