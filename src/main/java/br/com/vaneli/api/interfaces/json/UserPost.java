package br.com.vaneli.api.interfaces.json;

import br.com.vaneli.api.domain.UserDomain;
import br.com.vaneli.api.interfaces.enumerated.Sexo;
import br.com.vaneli.api.validators.InternationalPhone;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class UserPost {

  @NotBlank
  @Size(max = 255)
  private String name;

  @NotNull
  private Sexo sexo;

  @NotBlank
  @Size(max = 20)
  @CPF
  private String cpf;

  @NotBlank
  @InternationalPhone
  private String phone;

  private String cep;

  public UserDomain toUserDomain() {
    return UserDomain.builder()
      .name(this.name)
      .cpf(this.cpf)
      .phone(this.phone)
      .cep(this.cep)
      .sexo(this.sexo)
      .build();
  }

}
