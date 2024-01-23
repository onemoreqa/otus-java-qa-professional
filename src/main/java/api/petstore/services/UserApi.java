package api.petstore.services;

import static io.restassured.RestAssured.given;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class UserApi extends PetstoreApi {
  private static final String USER_POST_ENDPOINT = "/user";
  private static final String USER_GET_PUT_DELETE_ENDPOINT = "/user/{userName}";

  public void createContext(String url) {
    spec = given()
            .contentType(ContentType.JSON)
            .baseUri(url);
  }

  public Response addUserRequest(String userJSON) {
    return given(spec)
            .with()
            .body(userJSON)
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
