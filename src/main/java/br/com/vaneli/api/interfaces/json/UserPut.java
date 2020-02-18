package br.com.vaneli.api.interfaces.json;

import br.com.vaneli.api.domain.UserDomain;
import br.com.vaneli.api.validators.InternationalPhone;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

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
  @CPF
  private String cpf;

  @NotBlank
  @InternationalPhone
  private String phone;

  private String cep;

  public void toUserDomain(UserDomain userDomain) {
    userDomain.setName(this.name);
    userDomain.setCpf(this.cpf);
    userDomain.setPhone(this.phone);
    userDomain.setCep(this.cep);
  }

}
