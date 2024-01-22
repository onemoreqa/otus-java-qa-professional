package api.petstore.user.createuser;

import static org.hamcrest.Matchers.*;

import api.petstore.dto.UserRequestDTO;
import api.petstore.services.UserApi;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("api")
public class CreateUserTest {

  protected UserApi userApi;
  Faker faker;

  @BeforeEach
  public void init() {
    faker = new Faker();
    userApi = new UserApi();
    UserRequestDTO userPayload = new UserRequestDTO();

    userPayload.setEmail(faker.internet().safeEmailAddress());
    userPayload.setFirstName(faker.name().firstName());
    userPayload.setId(faker.idNumber().hashCode());
    userPayload.setLastName(faker.name().lastName());
    userPayload.setPassword(faker.internet().password(5,10));
    userPayload.setPhone(faker.phoneNumber().cellPhone());
    userPayload.setUsername(faker.name().username());
    System.out.println("Random user by Faker:\n" + userPayload.toString());
  }

  @Test
  public void createValidUser() {
    UserRequestDTO user = UserRequestDTO.builder()
            .username("user")
            .phone("123")
            .email("123@123.ru")
            .userStatus(1)
            .id(1)
            .firstName("userName")
            .password("123")
            .lastName("userLastName")
            .build();

    userApi.addUserRequest(user.toString())
            .then()
            .statusCode(200)
            .body("message", equalTo("1"));
  }
}
