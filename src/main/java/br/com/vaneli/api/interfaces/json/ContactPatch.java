package br.com.vaneli.api.interfaces.json;

import br.com.vaneli.api.domain.ContactDomain;
import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactPatch {

  private String value;

  public void toContactDomain(ContactDomain contactDomain) {
    if (!Strings.isNullOrEmpty(this.value)) contactDomain.setValue(this.value);
  }

}
