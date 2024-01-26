package api.petstore.steps;

import static org.hamcrest.Matchers.equalTo;

import java.util.Map;
import api.petstore.services.UserApi;
import com.google.gson.Gson;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

public class UserSteps {

  Map<String,Object> userJson;
  UserApi userApi;

  public UserSteps(Map<String, Object> userJson, UserApi userApi) {
    this.userJson = userJson;
    this.userApi = userApi;
  }

  public void createUser() {

    Response addUserRequest = userApi.addUserRequest(new Gson().toJson(userJson));
    addUserRequest.then().log().all()
            .statusCode(HttpStatus.SC_OK)
            .body("message", equalTo(String.valueOf(userJson.get("id").toString())));
  }

  public void checkUserIsCreated() {
    Response getUser = userApi.getUserByName(userJson.get("username").toString());
    getUser.then().log().all()
            .statusCode(HttpStatus.SC_OK)
            .body("username", equalTo(userJson.get("username").toString()))
            .body("id", equalTo(userJson.get("id")));
  }
  public void checkUserNotExisted() {
    Response getUser = userApi.getUserByName(userJson.get("username").toString());
    getUser.then().log().all()
            .statusCode(HttpStatus.SC_NOT_FOUND)
            .body("message", equalTo("User not found"));
  }

  public void removeUser() {
    Response deleteUser = userApi.deleteUser(userJson.get("username").toString());
    deleteUser.then().log().all();
    //не привязываюсь тут к коду ответа, для более гарантированного удаления
  }

  public void removeUserCondition(String expectedStatusCode) {
    Response deleteUser = userApi.deleteUser(userJson.get("username").toString());
    deleteUser.then().log().all()
            .statusCode(Integer.parseInt(expectedStatusCode));
  }

}
