package br.com.vaneli.api.interfaces.json;

import br.com.vaneli.api.interfaces.enumerators.Sexo;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

  private UUID id;

  private String name;

  private Sexo sexo;

  private String cpf;

  private String phone;

  private String cep;

  private Address address;

}
