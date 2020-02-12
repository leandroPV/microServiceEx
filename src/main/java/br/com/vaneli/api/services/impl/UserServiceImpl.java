package br.com.vaneli.api.services.impl;

import br.com.vaneli.api.domain.UserDomain;
import br.com.vaneli.api.interfaces.json.UserPost;
import br.com.vaneli.api.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

  @Override
  public UserDomain postUser(UserPost userPost) {
    return null;
  }
}
