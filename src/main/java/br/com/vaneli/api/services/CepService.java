package br.com.vaneli.api.services;

import br.com.vaneli.api.interfaces.json.Address;

public interface CepService {

  Address getCep(String cep);

}
