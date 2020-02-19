package br.com.vaneli.api.services.impl;

import br.com.vaneli.api.domain.ContactDomain;
import br.com.vaneli.api.domain.UserDomain;
import br.com.vaneli.api.exceptions.MessageError;
import br.com.vaneli.api.exceptions.NotFoundException;
import br.com.vaneli.api.filters.ContactFilter;
import br.com.vaneli.api.interfaces.Messages;
import br.com.vaneli.api.interfaces.json.Contact;
import br.com.vaneli.api.interfaces.json.ContactPatch;
import br.com.vaneli.api.interfaces.json.ContactPost;
import br.com.vaneli.api.interfaces.json.ContactPut;
import br.com.vaneli.api.repository.ContactRepository;
import br.com.vaneli.api.services.ContactService;
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
public class ContactServiceImpl implements ContactService {

  private final UserService userService;
  private final ContactRepository contactRepository;
  private final MessageError messageError;

  public ContactServiceImpl(UserService userService,
    ContactRepository contactRepository,
    MessageError messageError) {
    this.userService = userService;
    this.contactRepository = contactRepository;
    this.messageError = messageError;
  }

  @Override
  @Transactional
  public ContactDomain postContact(UUID userId, ContactPost contactPost) {
    UserDomain userDomain = this.userService.getUserDomainById(userId);
    ContactDomain contactDomain = contactPost.toContactDomain();
    contactDomain.setUser(userDomain);
    return this.contactRepository.save(contactDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Contact> getContacts(
    UUID userId,
    ContactFilter contactFilter,
    Integer offset,
    Integer limit) {
    PageRequest pageRequest = PageRequest.of(offset, limit);
    return this.contactRepository.findAll(contactFilter.toPredicate(), pageRequest)
      .map(ContactDomain::toContact);
  }

  @Override
  @Transactional(readOnly = true)
  public Contact getContact(UUID userId, UUID contactId) {
    return this.getContactDomain(userId, contactId).toContact();
  }

  @Override
  @Transactional(readOnly = true)
  public ContactDomain getContactDomain(UUID userId, UUID contactId) {
    return this.contactRepository.findByUserIdAndId(userId, contactId)
      .orElseThrow(() -> new NotFoundException(this.messageError.create(
        Messages.CONTACT_NOT_FOUND), MessageFormat.format(
        "Contact not found -> userId={0} and contactId={1}", userId, contactId)));
  }

  @Override
  @Transactional
  public void putContact(
    UUID userId,
    UUID contactId,
    ContactPut contactPut) {
    this.userService.existsUserDomainById(userId);
    ContactDomain contactDomain = this.getContactDomain(userId, contactId);
    contactPut.toContactDomain(contactDomain);
    this.contactRepository.save(contactDomain);
  }

  @Override
  @Transactional
  public void patchContact(
    UUID userId,
    UUID contactId,
    ContactPatch contactPatch) {
    this.userService.existsUserDomainById(userId);
    ContactDomain contactDomain = this.getContactDomain(userId, contactId);
    contactPatch.toContactDomain(contactDomain);
    this.contactRepository.save(contactDomain);
  }

  @Override
  @Transactional
  public void deleteContact(UUID userId, UUID contactId) {
    this.userService.existsUserDomainById(userId);
    ContactDomain contactDomain = this.getContactDomain(userId, contactId);
    this.contactRepository.delete(contactDomain);
  }

}
