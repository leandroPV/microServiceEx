package br.com.vaneli.api.services;

import br.com.vaneli.api.domain.UserDomain;
import br.com.vaneli.api.filters.UserFilter;
import br.com.vaneli.api.interfaces.json.User;
import br.com.vaneli.api.interfaces.json.UserPost;
import br.com.vaneli.api.interfaces.json.UserPut;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface UserService {

  UserDomain postUser(UserPost userPost);

  Page<User> getUsers(UserFilter userFilter, Integer offset, Integer limit);

  User getUser(UUID userId);

  void putUser(UUID userId, UserPut userPut);

}
