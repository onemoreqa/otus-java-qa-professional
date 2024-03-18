package pojo.http;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    @JsonProperty("name")
    private String name;
    @JsonProperty("course")
    String course;
    @JsonProperty("email")
    String email;
    @JsonProperty("age")
    int age;
}
