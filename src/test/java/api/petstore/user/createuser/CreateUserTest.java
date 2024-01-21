package api.petstore.user.createuser;

import static org.hamcrest.Matchers.*;

import api.petstore.dto.User;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * CreateUserTest
 *
 * @author Alexander Suvorov
 */
@Tag("api")
public class CreateUserTest extends UserBaseTest {

  @Test
  public void checkCreateUser() {
    Response response;
    User user;
    String expectedEmail = "Test@mail.ru";
    String actualType;
    String type = "unknown";
    String errorMessageType = "Incorrect userName";
    String expectedType = "unknown";
    Long id = 101L;

    user = User.builder()
            .email(expectedEmail)
            .firstName("FirstName")
            .id(id)
            .lastName("LastName")
            .password("Password")
            .phone("8-920-920-23-23")
            .username("Ivan")
            .userStatus(10L)
            .build();

    response = userApi.createUser(user);

    response
            .then()
            .log().all()
            .statusCode(HttpStatus.SC_OK)
            .time(lessThan(5000L))
            .body("type", equalTo(expectedType))
            .body("message", comparesEqualTo(id.toString()))
            .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/CreateUser.json"));

    actualType = response.jsonPath().get("type");
    Assertions.assertEquals(type, actualType, errorMessageType);

  }
}
