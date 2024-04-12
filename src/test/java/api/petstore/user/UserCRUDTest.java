package api.petstore.user;

import static com.github.automatedowl.tools.AllureEnvironmentWriter.allureEnvironmentWriter;

import api.petstore.steps.UserSteps;
import com.google.common.collect.ImmutableMap;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;


@Tag("api")
@Owner("yigorbu4")
@Execution(ExecutionMode.CONCURRENT)
public class UserCRUDTest {

  UserSteps steps;

  @BeforeAll
  public static void setAllureEnvironment() {
    allureEnvironmentWriter(
            ImmutableMap.<String, String>builder()
                    .put("BASE_URL", System.getProperty("base.url.api"))
                    .put("PARALLEL", System.getProperty("junit.jupiter.execution.parallel.config.fixed.parallelism"))
                    .put("JAVA_VERSION", System.getProperty("java.version"))
                    .put("REST_ASSURED_VERSION", System.getProperty("restassured.version"))
                    .build());
  }

  /**
   * Проверяем, что тестовый пользователь не создан
   * 1. Генерим ДТО
   * 2. GET /user/{username} -> 404
   */
  @BeforeEach
  public void init() {
    steps = new UserSteps();
    steps.checkUserNotExisted();
  }

  /**
   * Подчищаем за собой после создания тестового пользователя
   * 1. DELETE /user/{username}
   * 2. GET /user/{username} -> 404
   */
  @AfterEach
  public void cleanAfterTests() {
    steps.removeUser();
    steps.checkUserNotExisted();
  }

  /**
   * Создание валидного пользователя
   * 1. POST /user -> 200
   * 2. GET /user/{username} -> 200
   */
  @Test
  @DisplayName("Создание пользователя -> 200")
  public void createValidUser() {
    steps.createUser();
    steps.checkUserIsCreated();
  }

  /**
   * Проверка созданного пользователя по username
   * 1. POST /user/createWithList -> 200
   */
  @Test
  @DisplayName("Создание двух пользователей -> 200")
  public void createValidUserList() {
    steps.createUserList();
  }

  /**
   * Проверка получения и удаления не созданного пользователя
   * 1. GET /user/{username} -> 404
   * 2. DELETE /user/{username} -> 404
   */
  @Test
  @DisplayName("Получение и удаление не созданного пользователя -> 404")
  public void getDeleteNotCreatedUser() {
    steps.checkUserNotExisted();
    steps.removeUserCondition("404");
  }


  /**
   * Проверка удаления пользователя до и после создания
   * 1. DELETE /user/{username} -> 404
   * 2. POST /user -> 200
   * 3. DELETE /user/{username} -> 200
   * 4. DELETE /user/{username} -> 404
   */
  @Test
  @DisplayName("Удаление пользователя до и после создания")
  public void deleteUserAfterCreation() {
    steps.removeUserCondition("404");
    steps.createUser();
    steps.removeUserCondition("200");
    steps.removeUserCondition("404");
  }
}
