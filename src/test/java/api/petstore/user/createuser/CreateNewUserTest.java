package api.petstore.user.createuser;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import api.petstore.dto.UserResponseDTO;
import api.petstore.dto.UserDTO;
import api.petstore.services.ServiceApi;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.ValidatableResponse;

@Tag("api")
public class CreateNewUserTest {
  ServiceApi userApi = new ServiceApi();

  /***
   * Проверяю создание пользователя, без обязательных поле
   */
  @Test
  public void createUser() {
    UserDTO userDTO = UserDTO.builder()
            .id(4069l)
            .userStatus(505l)
            .username("Ivan")
            .email("mail@google.com")
            .build();

    userApi.createUser(userDTO)
            .statusCode(HttpStatus.SC_OK)
            .body("code", equalTo(200))
            .body("type", equalTo("unknown"))
            .body("message", equalTo("4069"))
            .time(lessThan(2000l))
            .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/CreateUser.json"));

    ValidatableResponse response = userApi.createUser(userDTO);

    String actualMessage = response.extract().jsonPath().get("message").toString();
    Assertions.assertEquals("4069", actualMessage, "Incorrect message");

    UserResponseDTO actualUser = response.extract().body().as(UserResponseDTO.class);

    Assertions.assertAll("Check create user response",
        () -> assertEquals(userDTO.getId().toString(), actualUser.getMessage(), "Incorrect message"),
        () -> assertEquals(200, actualUser.getCode(), "IncorrectCode"),
        () -> assertEquals("unknown", actualUser.getType(), "IncorrectType")
    );
  }



}
