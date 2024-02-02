package api.petstore.services;

import static io.restassured.RestAssured.given;

import api.petstore.dto.UserRequestDTO;
import io.restassured.response.Response;
import java.util.List;

public class UserApi extends PetstoreApi {
  private static final String USER_POST_ENDPOINT = "/user";
  private static final String USER_LIST_POST_ENDPOINT = "/user/createWithList";
  private static final String USER_GET_PUT_DELETE_ENDPOINT = "/user/{userName}";

  public Response addUsersWithList(List<UserRequestDTO> usersRequestBody) {
    return given(spec)
            .with()
            .body(usersRequestBody)
            .when()
            .post(USER_LIST_POST_ENDPOINT);
  }

  public Response addUserRequest(UserRequestDTO userRequestBody) {
    return given(spec)
            .with()
            .body(userRequestBody)
            .when()
            .post(USER_POST_ENDPOINT);
  }

  public Response getUserByName(String userName) {
    return given(spec)
            .with()
            .pathParam("userName", userName)
            .when()
            .get(USER_GET_PUT_DELETE_ENDPOINT);
  }

  public Response deleteUser(String userName) {
    return  given(spec)
            .pathParam("userName", userName)
            .when()
            .delete(USER_GET_PUT_DELETE_ENDPOINT);
  }
}
