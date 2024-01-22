package api.petstore.stepdefs;

import api.petstore.services.UserApi;
import api.petstore.utils.Parser;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

public class UserSteps {

  UserApi userApi = new UserApi();
  JSONObject user;
  Response response;
  String userName;
  JSONCompareMode compareMode;

  @Given("Swagger: {string}")
  public void createContext(String url) {
    userApi.createContext(url);
  }

  @And("данные пользователя {string}")
  public void createUser(String path) {
    user = Parser.parseJSONFile("src/test/resources/" + path);
  }

  @When("отправляется запрос на создание пользователя")
  public void sendRequest() {
    response =  userApi.addUserRequest(user.toString());
  }

  @Then("возвращается ответ со статусом {string}")
  public void checkResponse(String responseCode) {
    Assertions.assertEquals(Integer.valueOf(responseCode), response.jsonPath().get("code"));
  }

  @And("{string} пользователя равен указанному в файле {string}")
  public void checkUserId(String attribute,  String filePath) {
    Integer userId = null;
    try {
      userId = user.getInt(attribute);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    Assertions.assertEquals(userId.toString(), response.jsonPath().get("message"));
  }

  @And("имя пользователя через атрибут {string}")
  public void setUserName(String attribute) {
    try {
      userName = user.getString(attribute);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    System.out.println(userName);
  }

  @When("отправляется запрос на получение сведений о пользователе")
  public void getUserByName() {
    response = userApi.getUserByName(userName);
  }

  @And("проверяем полное совпадение")
  public void strictMode() {
    compareMode = JSONCompareMode.STRICT;
  }

  @And("проверяем частичное совпадение")
  public void lenientMode() {
    compareMode = JSONCompareMode.LENIENT;
  }

  @Then("данные в файле и ответе с сайта совпадают")
  public void checkDataInResponse() {
    String responseJsonString = response.body().asString();
    try {
      JSONAssert.assertEquals(user.toString(), responseJsonString, compareMode);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

}
