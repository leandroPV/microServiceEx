package br.com.vaneli.api.services;

import br.com.vaneli.api.domain.UserDomain;
import br.com.vaneli.api.interfaces.json.UserPost;

public interface UserService {

  UserDomain postUser(UserPost userPost);

}
