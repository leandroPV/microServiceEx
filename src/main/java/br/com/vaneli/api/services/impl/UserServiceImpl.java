package br.com.vaneli.api.services.impl;

import br.com.vaneli.api.domain.AddressDomain;
import br.com.vaneli.api.domain.UserDomain;
import br.com.vaneli.api.exceptions.MessageError;
import br.com.vaneli.api.exceptions.NotFoundException;
import br.com.vaneli.api.filters.UserFilter;
import br.com.vaneli.api.interfaces.Messages;
import br.com.vaneli.api.interfaces.json.Address;
import br.com.vaneli.api.interfaces.json.User;
import br.com.vaneli.api.interfaces.json.UserPatch;
import br.com.vaneli.api.interfaces.json.UserPost;
import br.com.vaneli.api.interfaces.json.UserPut;
import br.com.vaneli.api.queue.json.CepData;
import br.com.vaneli.api.queue.senders.CepSender;
import br.com.vaneli.api.repository.UserRepository;
import br.com.vaneli.api.services.CepService;
import br.com.vaneli.api.services.UserService;
import com.google.common.base.Strings;
import java.text.MessageFormat;
import java.util.Objects;
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
  private final CepService cepService;
  private final CepSender cepSender;

  public UserServiceImpl(
    MessageError messageError,
    UserRepository userRepository, CepService cepService,
    CepSender cepSender) {
    this.messageError = messageError;
    this.userRepository = userRepository;
    this.cepService = cepService;
    this.cepSender = cepSender;
  }

  @Override
  public UserDomain postUser(UserPost userPost) {
    UserDomain userDomain = userPost.toUserDomain();

    userDomain = this.userRepository.save(userDomain);

    addToQueueCep(userDomain, userPost.getCep());

    return userDomain;
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
  public void putUser(UUID userId, UserPut userPut) {
    UserDomain userDomain = this.getUserDomainById(userId);

    userPut.toUserDomain(userDomain);
    userDomain = this.userRepository.save(userDomain);

    addToQueueCep(userDomain, userDomain.getCep());

  }

  @Override
  public void patchUser(UUID userId, UserPatch userPatch) {
    UserDomain userDomain = this.getUserDomainById(userId);

    userPatch.toUserDomain(userDomain);
    userDomain = this.userRepository.save(userDomain);

    addToQueueCep(userDomain, userDomain.getCep());
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

  @Override
  public void addAddressToUserByCep(CepData cepData) {
    UserDomain userDomain = this.getUserDomainById(cepData.getUserId());
    if (!Strings.isNullOrEmpty(userDomain.getCep())) {
      Address address = this.cepService.getCep(userDomain.getCep());
      if (Objects.nonNull(address)) {
        AddressDomain addressDomain = address.toAddressDomain();
        addressDomain.setUser(userDomain);
        userDomain.setAddress(addressDomain);
      }
    } else {
      userDomain.setAddress(null);
    }
    this.userRepository.save(userDomain);
  }

  private void addToQueueCep(UserDomain userDomain, String cep) {
    this.cepSender.addToQueue(CepData.builder()
      .userId(userDomain.getId())
      .cep(cep)
      .build());
  }

}
