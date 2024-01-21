
package api.petstore.dto;

import lombok.Data;

@Data
public class UserResponseDTO {

  private Long code;
  private String message;
  private String type;
}
