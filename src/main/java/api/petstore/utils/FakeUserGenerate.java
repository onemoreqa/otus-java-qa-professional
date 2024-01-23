package api.petstore.utils;

import com.github.javafaker.Faker;

import java.util.HashMap;
import java.util.Map;

public class FakeUserGenerate {

  public static Map<String,Object> getRandomValidUserDTO() {
    Faker faker = new Faker();
    Map<String,Object> bodyParams = new HashMap<String,Object>();
    bodyParams.put("id", faker.idNumber().hashCode());
    bodyParams.put("username", faker.name().username());
    bodyParams.put("firstName", faker.name().firstName());
    bodyParams.put("lastName", faker.name().lastName());
    bodyParams.put("email", faker.internet().safeEmailAddress());
    bodyParams.put("password", faker.internet().password(5,10));
    bodyParams.put("phone", faker.phoneNumber().cellPhone());
    bodyParams.put("userStatus", 0);

    return bodyParams;
  }

  public static Map<String,Object> getInvalidUserDTO() {
    Faker faker = new Faker();
    Map<String,Object> bodyParams = new HashMap<String,Object>();
    bodyParams.put("username", faker.name().username());
    bodyParams.put("firstName", faker.name().firstName());
    bodyParams.put("userStatus", 0);

    return bodyParams;
  }
}
