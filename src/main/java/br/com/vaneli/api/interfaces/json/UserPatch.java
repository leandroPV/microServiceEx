package br.com.vaneli.api.interfaces.json;

import br.com.vaneli.api.domain.UserDomain;
import br.com.vaneli.api.interfaces.enumerated.Sexo;
import br.com.vaneli.api.validators.InternationalPhone;
import com.google.common.base.Strings;
import java.util.Objects;
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
public class UserPatch {

  @Size(max = 255)
  private String name;

  private Sexo sexo;

  @Size(max = 20)
  @CPF
  private String cpf;

  @InternationalPhone
  private String phone;

  private String cep;

  public void toUserDomain(UserDomain userDomain) {
    if (!Strings.isNullOrEmpty(this.name)) userDomain.setName(this.name);
    if (!Strings.isNullOrEmpty(this.cpf)) userDomain.setCpf(this.cpf);
    if (!Strings.isNullOrEmpty(this.phone)) userDomain.setCpf(this.phone);
    if (!Strings.isNullOrEmpty(this.cep)) userDomain.setCep(this.cep);
    if (Objects.nonNull(this.sexo)) userDomain.setSexo(this.sexo);
  }

}
