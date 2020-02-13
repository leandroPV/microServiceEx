package br.com.vaneli.api.services.impl;

import br.com.vaneli.api.domain.UserDomain;
import br.com.vaneli.api.exceptions.MessageError;
import br.com.vaneli.api.exceptions.NotFoundException;
import br.com.vaneli.api.filters.UserFilter;
import br.com.vaneli.api.interfaces.Messages;
import br.com.vaneli.api.interfaces.json.User;
import br.com.vaneli.api.interfaces.json.UserPatch;
import br.com.vaneli.api.interfaces.json.UserPost;
import br.com.vaneli.api.interfaces.json.UserPut;
import br.com.vaneli.api.repository.UserRepository;
import br.com.vaneli.api.services.UserService;
import java.text.MessageFormat;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

  private final MessageError messageError;
  private final UserRepository userRepository;

  public UserServiceImpl(MessageError messageError,
    UserRepository userRepository) {
    this.messageError = messageError;
    this.userRepository = userRepository;
  }

  @Override
  @Transactional
  public UserDomain postUser(UserPost userPost) {
    UserDomain userDomain = userPost.toUserDomain();

    return this.userRepository.save(userDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<User> getUsers(
    UserFilter userFilter, Integer offset, Integer limit) {
    PageRequest pageRequest = PageRequest.of(offset, limit);
    return this.userRepository.findAll(userFilter.toPredicate(), pageRequest)
      .map(UserDomain::toUser);
  }

  @Override
  @Transactional(readOnly = true)
  public User getUser(UUID userId) {
    return getUserDomainById(userId).toUser();
  }

  @Override
  @Transactional
  public void putUser(UUID userId, UserPut userPut) {
    UserDomain userDomain = this.getUserDomainById(userId);

    userPut.toUserDomain(userDomain);
    this.userRepository.save(userDomain);
  }

  @Override
  @Transactional
  public void patchUser(UUID userId, UserPatch userPatch) {
    UserDomain userDomain = this.getUserDomainById(userId);

    userPatch.toUserDomain(userDomain);
    this.userRepository.save(userDomain);
  }

  @Override
  @Transactional
  public void deleteUser(UUID userId) {
    UserDomain userDomain = this.getUserDomainById(userId);

    this.userRepository.delete(userDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public UserDomain getUserDomainById(UUID userId) {
    return this.userRepository.findById(userId)
      .orElseThrow(() -> new NotFoundException(this.messageError.create(
        Messages.USER_NOT_FOUND), MessageFormat.format(
        "User not found -> userId={0}", userId)));
  }

  @Override
  @Transactional(readOnly = true)
  public void existsUserDomainById(UUID userId) {
    if (!this.userRepository.existsById(userId)) {
      throw new NotFoundException(this.messageError.create(
        Messages.USER_NOT_FOUND), MessageFormat.format(
        "User not found -> userId={0}", userId));
    }
  }

}
