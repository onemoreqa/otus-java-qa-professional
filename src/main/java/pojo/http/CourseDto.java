package pojo.http;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseDto {
    @JsonProperty("name")
    String name;
    @JsonProperty("price")
    long price;
}
