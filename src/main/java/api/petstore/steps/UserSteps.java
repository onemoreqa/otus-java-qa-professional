package api.petstore.steps;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;
import java.util.List;

import api.petstore.dto.UserRequestDTO;
import api.petstore.dto.UserResponseDTO;
import api.petstore.services.UserApi;
import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import org.apache.http.HttpStatus;

public class UserSteps {

  UserApi userApi;
  UserRequestDTO userBody = generateUserDTO();
  public UserSteps() {
    this.userApi = new UserApi();
  }

  public UserRequestDTO generateUserDTO() {
    Faker faker = new Faker();

    return UserRequestDTO.builder()
            .id(faker.idNumber().hashCode())
            .username(faker.name().username())
            .firstName(faker.name().firstName())
            .lastName(faker.name().lastName())
            .email(faker.internet().safeEmailAddress())
            .password(faker.internet().password(5,10))
            .phone(faker.phoneNumber().cellPhone())
            .userStatus(0)
            .build();
  }

  public void createUser() {
    userApi.addUserRequest(userBody)
            .then()
            .log().all()
            .statusCode(HttpStatus.SC_OK)
            .body("message", equalTo(String.valueOf(userBody.getId())))
            .body(matchesJsonSchemaInClasspath("schema/CreateUser.json"));
  }

  public void createUserList() {
    UserRequestDTO secondUserBody = generateUserDTO();

    List<UserRequestDTO> users = new ArrayList<>();
    users.add(userBody);
    users.add(secondUserBody);

    userApi.addUsersWithList(users)
            .then()
            .log().all()
            .statusCode(200)
            .body("message", equalTo("ok"));

    removeUser(secondUserBody);
  }

  @Step("Пользователь создан -> 200")
  public void checkUserIsCreated() {
    userApi.getUserByName(userBody.getUsername())
            .then()
            .log().all()
            .statusCode(HttpStatus.SC_OK)
            .body("username", equalTo(userBody.getUsername()))
            .body("id", equalTo(userBody.getId()));
  }

  @Step("Пользователь не существует -> 404")
  public void checkUserNotExisted() {
    userApi.getUserByName(userBody.getUsername())
            .then()
            .log().all()
            .statusCode(HttpStatus.SC_NOT_FOUND)
            .body("message", equalTo("User not found"))
            .extract()
            .body()
            .as(UserResponseDTO.class);
  }

  public void removeUser() {
    removeUser(userBody);
  }

  public void removeUser(UserRequestDTO someUserBody) {
    userApi.deleteUser(someUserBody.getUsername())
            .then()
            .log().all();
    //не привязываюсь тут к коду ответа, для более гарантированного удаления
  }

  @Step("Удаление пользователя. Ожидаемый статус = {expectedStatusCode}")
  public void removeUserCondition(String expectedStatusCode) {
    userApi.deleteUser(userBody.getUsername())
            .then().log().all()
            .statusCode(Integer.parseInt(expectedStatusCode));
  }

}
