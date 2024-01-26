package api.petstore.user;

import static org.hamcrest.Matchers.equalTo;

import api.petstore.services.UserApi;
import api.petstore.steps.UserSteps;
import api.petstore.utils.FakeUserGenerate;
import com.google.gson.Gson;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;

import java.util.Map;


@Tag("api")
public class UserCRUDTest {

  Map<String,Object> userJson;
  protected UserApi userApi;
  UserSteps steps;


  /**
   * Проверяем, что тестовый пользователь не создан
   * Шаги:
   * 1. Генерим ДТО
   * 2. GET /user/{username} -> 404
   */
  @BeforeEach
  public void init() {
    userJson = FakeUserGenerate.getRandomValidUserDTO();
    userApi = new UserApi();
    steps = new UserSteps(userJson, userApi);

    steps.checkUserNotExisted();
  }

  /**
   * Подчищаем за собой после создания тестового пользователя
   * Шаги:
   * 1. DELETE /user/{username}
   * 2. GET /user/{username} -> 404
   */
  @AfterEach
  public void cleanAfterTests() {
    steps.removeUser();
    //userApi.checkUserNotExisted(userJson, userApi);
    // Мне кажется проверки отсутствия пользака достаточно в BeforeEach, но есть шанс что на самом последнем тесте как раз её и не хватит
  }

  /**
   * Создание валидного пользователя
   * Шаги:
   * 1. POST /user -> 200
   * 2. GET /user/{username} -> 200
   */
  @Test
  public void createValidUser() {
    steps.createUser();
    steps.checkUserIsCreated();
  }

  /**
   * Проверка созданного пользователя по username
   * Это по сути предыдущий тест, хочу его оставить в назидание, чтоб продемонстрировать снижении читаемости
   * Шаги:
   * 1. POST /user
   * 2. GET /user/{username} -> 200 (getResponse.id == postRequest.id)
   */
  @Test
  public void getUserByNameAfterCreation() {
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
   * Проверка получения и удаления не созданного пользователя
   * Шаги:
   * 1. GET /user/{username} -> 404
   * 2. DELETE /user/{username} -> 404
   */
  @Test
  public void getDeleteNotCreatedUser() {
    steps.checkUserNotExisted();
    steps.removeUserCondition("404");
  }


  /**
   * Проверка удаления пользователя до и после создания
   * Шаги:
   * 1. DELETE /user/{username} -> 404
   * 2. POST /user -> 200
   * 3. DELETE /user/{username} -> 200
   * 4. DELETE /user/{username} -> 404
   */
  @Test
  public void deleteUserAfterCreation() {
    steps.removeUserCondition("404");
    steps.createUser();
    steps.removeUserCondition("200");
    steps.removeUserCondition("404");
  }
}
