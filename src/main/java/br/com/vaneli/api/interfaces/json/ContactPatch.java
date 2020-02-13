package br.com.vaneli.api.interfaces.json;

import br.com.vaneli.api.domain.ContactDomain;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactPatch {

  @NotBlank
  private String value;

  public ContactDomain toContactDomain() {
    return ContactDomain.builder()
      .value(this.value)
      .build();
  }

}
