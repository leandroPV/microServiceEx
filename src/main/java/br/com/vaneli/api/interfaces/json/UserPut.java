package br.com.vaneli.api.interfaces.json;

import br.com.vaneli.api.domain.UserDomain;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPut {

  @NotBlank
  @Size(max = 255)
  private String name;

  @NotBlank
  @Size(max = 20)
  private String cpf;

  public void toUserDomain(UserDomain userDomain) {
    userDomain.setName(this.name);
    userDomain.setCpf(this.cpf);
  }

}
