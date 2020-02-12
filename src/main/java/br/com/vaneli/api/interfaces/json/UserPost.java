package br.com.vaneli.api.interfaces.json;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPost {

  @NotBlank
  private String name;

  @NotBlank
  private String cpf;

}
