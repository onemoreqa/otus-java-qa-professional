package api.petstore.services;

import static io.restassured.RestAssured.given;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public abstract class PetstoreApi {
  private static final String BASE_URL = "https://petstore.swagger.io/v2";
  protected RequestSpecification spec;
  private boolean isDebug = Boolean.parseBoolean(System.getProperty("isDebug", "true"));

  public PetstoreApi() {
    spec =  given()
            .baseUri(BASE_URL)
            .contentType(ContentType.JSON)
            .log().all(isDebug);
  }
}