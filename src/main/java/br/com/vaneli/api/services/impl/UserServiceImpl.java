package br.com.vaneli.api.services.impl;

import br.com.vaneli.api.domain.UserDomain;
import br.com.vaneli.api.filters.UserFilter;
import br.com.vaneli.api.interfaces.json.User;
import br.com.vaneli.api.interfaces.json.UserPost;
import br.com.vaneli.api.repository.UserRepository;
import br.com.vaneli.api.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDomain postUser(UserPost userPost) {

    UserDomain userDomain = userPost.toUserDomain();

    return this.userRepository.save(userDomain);

  }

  @Override
  public Page<User> getUsers(
    UserFilter userFilter, Integer offset, Integer limit) {
    PageRequest pageRequest = PageRequest.of(offset, limit);
    return this.userRepository.findAll(userFilter.toPredicate(), pageRequest)
      .map(UserDomain::toUser);
  }

}
