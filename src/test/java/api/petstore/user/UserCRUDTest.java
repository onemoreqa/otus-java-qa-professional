package api.petstore.user;

import static org.hamcrest.Matchers.equalTo;

import api.petstore.dto.UserResponseDTO;
import api.petstore.services.UserApi;
import api.petstore.utils.FakeUserGenerate;
import com.google.gson.Gson;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Map;


@Tag("api")
public class UserCRUDTest {

  Map<String,Object> userJson;
  protected UserApi userApi;

  @BeforeEach
  void init() {
    userJson = FakeUserGenerate.getRandomValidUserDTO();
    userApi = new UserApi();
  }

  /**
   * Создание валидного пользователя
   * Шаги:
   * 1. Генерим ДТО
   * 2. POST https://petstore.swagger.io/v2/user -> 200
   */
  @Test
  void createValidUser() {
    String payload = new Gson().toJson(userJson);
    String id = String.valueOf(userJson.get("id").toString());

    Response response = userApi.addUserRequest(payload);
    response.then().log().all();

    Assertions.assertAll("Check created user by fields",
        () -> Assertions.assertEquals(id, response.jsonPath().get("message"), "Incorrect message"),
        () -> Assertions.assertEquals(HttpStatus.SC_OK, response.getStatusCode(), "IncorrectCode")
    );
  }

  /**
   * Проверка созданного пользователя по username
   * Шаги:
   * 1. Генерим ДТО
   * 2. POST https://petstore.swagger.io/v2/user
   * 3. GET https://petstore.swagger.io/v2/user/randomUserName
   * Ожидаемый результат:
   * Ответ = 200,
   * getResponse.id == postRequest.id
   */
  @Test
  void getUserByNameAfterCreation() {
    String randomUserName = userJson.get("username").toString();

    String payload = new Gson().toJson(userJson);
    Response postResponse = userApi.addUserRequest(payload);
    postResponse.then().log().all();

    Response getResponse = userApi.getUserByName(randomUserName);
    getResponse.then().log().all()
            .statusCode(HttpStatus.SC_OK)
            .body("username", equalTo(userJson.get("username").toString()))
            .body("userStatus", equalTo(userJson.get("userStatus")))
            .body("id", equalTo(userJson.get("id")))
            .body("firstName", equalTo(userJson.get("firstName").toString()));
  }

  /**
   * Проверка получения не созданного пользователя
   * Шаги:
   * 1. GET https://petstore.swagger.io/v2/user/{username} -> 404
   * 2. DELETE https://petstore.swagger.io/v2/user/{username} -> 404
   */
  @Test
  void getAndDeleteNotCreatedUser() {
    String randomUserName = userJson.get("username").toString();

    Response getResponse = userApi.getUserByName(randomUserName);
    getResponse.then().log().all()
            .statusCode(HttpStatus.SC_NOT_FOUND)
            .extract()
            .body()
            .as(UserResponseDTO.class);

    Assertions.assertAll("Check not exist user",
        () -> Assertions.assertEquals("User not found", getResponse.jsonPath().get("message"), "Incorrect message for not existing user"),
        () -> Assertions.assertEquals("error", getResponse.jsonPath().get("type"), "Incorrect type for not existing user"),
        () -> Assertions.assertEquals("1", getResponse.jsonPath().get("code").toString(), "Incorrect code for not existing user")
    );

    Response deleteResponse = userApi.deleteUser(randomUserName);
    deleteResponse.then().log().all()
            .statusCode(HttpStatus.SC_NOT_FOUND);
  }


  /**
   * Проверка удаления пользователя после содания
   * Шаги:
   * 1. Генерим ДТО
   * 2. POST https://petstore.swagger.io/v2/user -> 200
   * 3. DELETE https://petstore.swagger.io/v2/user/{username} -> 200
   * 4. DELETE https://petstore.swagger.io/v2/user/{username} -> 404
   */
  @Test
  void deleteUserAfterCreation() {
    String randomUserName = userJson.get("username").toString();

    String payload = new Gson().toJson(userJson);
    Response postResponse = userApi.addUserRequest(payload);
    postResponse.then().log().all();

    Response deleteResponse = userApi.deleteUser(randomUserName);
    deleteResponse.then().log().all()
            .statusCode(HttpStatus.SC_OK)
            .body("message", equalTo(userJson.get("username").toString()));

    Response deleteResponseAgain = userApi.deleteUser(randomUserName);
    deleteResponseAgain.then().log().all()
            .statusCode(HttpStatus.SC_NOT_FOUND);
  }
}
