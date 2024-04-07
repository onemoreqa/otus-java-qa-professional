package api.petstore.services;

import static io.restassured.RestAssured.given;

import api.petstore.dto.UserRequestDTO;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import java.util.List;

public class UserApi extends PetstoreApi {
  private static final String USER_POST_ENDPOINT = "/user";
  private static final String USER_LIST_POST_ENDPOINT = "/user/createWithList";
  private static final String USER_GET_PUT_DELETE_ENDPOINT = "/user/{userName}";

  @Step("Создание нескольких пользователей: \n{usersRequestBody}")
  public Response addUsersWithList(List<UserRequestDTO> usersRequestBody) {
    return given(spec)
            .with()
            .body(usersRequestBody)
            .when()
            .post(USER_LIST_POST_ENDPOINT);
  }

  @Step("Создание пользователя: "
          + "\n{userRequestBody}")
  public Response addUserRequest(UserRequestDTO userRequestBody) {
    return given(spec)
            .with()
            .body(userRequestBody)
            .when()
            .post(USER_POST_ENDPOINT);
  }

  @Step("Получение пользователя = {userName}")
  public Response getUserByName(String userName) {
    return given(spec)
            .with()
            .pathParam("userName", userName)
            .when()
            .get(USER_GET_PUT_DELETE_ENDPOINT);
  }

  @Step("Удаление пользователя: {userName}")
  public Response deleteUser(String userName) {
    return  given(spec)
            .pathParam("userName", userName)
            .when()
            .delete(USER_GET_PUT_DELETE_ENDPOINT);
  }
}
