package br.com.vaneli.api.interfaces.json;

import br.com.vaneli.api.domain.UserDomain;
import com.google.common.base.Strings;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPatch {

  @Size(max = 255)
  private String name;

  @Size(max = 20)
  private String cpf;

  public void toUserDomain(UserDomain userDomain) {
    if (!Strings.isNullOrEmpty(this.name)) userDomain.setName(this.name);
    if (!Strings.isNullOrEmpty(this.cpf)) userDomain.setCpf(this.cpf);
  }

}