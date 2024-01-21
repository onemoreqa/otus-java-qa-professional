package api.petstore.services;

import static io.restassured.RestAssured.given;

import api.petstore.dto.UserDTO;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class ServiceApi {
  private static final String BASE_URL = "https://petstore.swagger.io/v2";
  private static final String BASE_PATH = "/user";
  private RequestSpecification spec;

  public ServiceApi(){
    spec =  given()
            .baseUri(BASE_URL)
            .contentType(ContentType.JSON)
            .log().all();
  }

  public ValidatableResponse createUser(UserDTO user){

    return given(spec)
            .basePath(BASE_PATH)
            .body(user)
            .when()
            .post()
            .then()
            .log().all();
  }
}