package br.com.vaneli.api.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.vaneli.api.domain.ContactDomain;
import br.com.vaneli.api.domain.UserDomain;
import br.com.vaneli.api.exceptions.MessageError;
import br.com.vaneli.api.exceptions.MessageError.ApiError;
import br.com.vaneli.api.exceptions.NotFoundException;
import br.com.vaneli.api.filters.ContactFilter;
import br.com.vaneli.api.interfaces.Messages;
import br.com.vaneli.api.interfaces.json.Contact;
import br.com.vaneli.api.interfaces.json.ContactPatch;
import br.com.vaneli.api.interfaces.json.ContactPost;
import br.com.vaneli.api.interfaces.json.ContactPut;
import br.com.vaneli.api.repository.ContactRepository;
import br.com.vaneli.api.repository.UserRepository;
import br.com.vaneli.api.services.ContactService;
import br.com.vaneli.api.services.UserService;
import com.querydsl.core.types.Predicate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import mockit.MockUp;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
public class ContactServiceImplTest {

  private static Integer LIMIT;
  private static Integer OFFSET;
  private static UUID USER_ID;
  private static UUID CONTACT_ID;

  private ContactService contactService;

  @Mock
  private MessageError messageError;
  @Mock
  private UserService userService;
  @Mock
  private UserRepository userRepository;
  @Mock
  private ContactRepository contactRepository;
  @Mock
  private ApiError apiError;
  @Mock
  private Predicate predicate;
  @Mock
  private PageRequest pageRequest;
  @Mock
  private UserDomain userDomain;
  @Mock
  private ContactDomain contactDomain;
  @Mock
  private ContactPost contactPost;
  @Mock
  private ContactPatch contactPatch;
  @Mock
  private ContactPut contactPut;
  @Mock
  private Contact contact;
  @Mock
  private ContactFilter contactFilter;

  @BeforeAll
  public static void beforeAll() {
    OFFSET = 0;
    LIMIT = 50;
    USER_ID = UUID.randomUUID();
    CONTACT_ID = UUID.randomUUID();
  }

  @BeforeEach
  public void beforeEach() {
    contactService = spy(new ContactServiceImpl(userService, contactRepository, messageError));
  }

  @DisplayName("Should add contact for user successfully")
  @Test
  public void postContactSuccessfully() {
    when(userService.getUserDomainById(USER_ID)).thenReturn(userDomain);
    when(contactPost.toContactDomain()).thenReturn(contactDomain);
    when(contactRepository.save(contactDomain)).thenReturn(contactDomain);

    assertEquals(contactDomain, contactService.postContact(USER_ID, contactPost));
  }

  @DisplayName("Should get contacts successfully")
  @Test
  public void getContactsSuccessfully() {
    new MockUp<PageRequest>() {
      @mockit.Mock
      public PageRequest of(int page, int size) {
        if (page != OFFSET || size != LIMIT) {
          throw new IllegalArgumentException("Stubbing has different values.");
        }
        return pageRequest;
      }
    };

    when(contactFilter.toPredicate()).thenReturn(predicate);
    when(contactRepository.findAll(predicate, pageRequest))
      .thenReturn(new PageImpl<>(List.of(contactDomain)));
    when(contactDomain.toContact()).thenReturn(contact);

    assertEquals(new PageImpl<>(List.of(contact)), contactService.getContacts(
      USER_ID,
      contactFilter,
      OFFSET,
      LIMIT
    ));
  }

  @DisplayName("Should get contact by id successfully")
  @Test
  public void getContactByIdSuccessfully() {
    when(contactRepository.findByUserIdAndId(USER_ID, CONTACT_ID)).thenReturn(Optional.of(contactDomain));
    when(contactDomain.toContact()).thenReturn(contact);

    assertEquals(contact, contactService.getContact(USER_ID, CONTACT_ID));
  }

  @DisplayName("Should get contact by id not found")
  @Test
  public void getContactByIdNotFound() {
    when(contactRepository.findByUserIdAndId(USER_ID, CONTACT_ID)).thenReturn(Optional.empty());
    when(messageError.create(Messages.CONTACT_NOT_FOUND)).thenReturn(apiError);

    assertThrows(NotFoundException.class,
      () -> contactService.getContact(USER_ID, CONTACT_ID));
  }

  @DisplayName("Should patch contact successfully")
  @Test
  public void patchContactSuccessfully() {
    doNothing().when(userService).existsUserDomainById(USER_ID);
    when(contactRepository.findByUserIdAndId(USER_ID, CONTACT_ID))
      .thenReturn(Optional.of(contactDomain));
    when(contactRepository.save(contactDomain)).thenReturn(contactDomain);

    contactService.patchContact(USER_ID, CONTACT_ID, contactPatch);
  }

  @DisplayName("Should patch contact not found")
  @Test
  public void patchContactNotFound() {
    doNothing().when(userService).existsUserDomainById(USER_ID);
    when(contactRepository.findByUserIdAndId(USER_ID, CONTACT_ID))
      .thenReturn(Optional.empty());
    when(messageError.create(Messages.CONTACT_NOT_FOUND)).thenReturn(apiError);

    assertThrows(NotFoundException.class,
      () -> contactService.patchContact(USER_ID, CONTACT_ID, contactPatch));
  }

  @DisplayName("Should put contact successfully")
  @Test
  public void putContactSuccessfully() {
    doNothing().when(userService).existsUserDomainById(USER_ID);
    when(contactRepository.findByUserIdAndId(USER_ID, CONTACT_ID))
      .thenReturn(Optional.of(contactDomain));
    when(contactRepository.save(contactDomain)).thenReturn(contactDomain);

    contactService.putContact(USER_ID, CONTACT_ID, contactPut);
  }

  @DisplayName("Should put contact not found")
  @Test
  public void putContactNotFound() {
    doNothing().when(userService).existsUserDomainById(USER_ID);
    when(contactRepository.findByUserIdAndId(USER_ID, CONTACT_ID))
      .thenReturn(Optional.empty());
    when(messageError.create(Messages.CONTACT_NOT_FOUND)).thenReturn(apiError);

    assertThrows(NotFoundException.class,
      () -> contactService.putContact(USER_ID, CONTACT_ID, contactPut));
  }

  @DisplayName("Should delete contact successfully")
  @Test
  public void deleteContactSuccessfully() {
    when(contactRepository.findByUserIdAndId(USER_ID, CONTACT_ID))
      .thenReturn(Optional.of(contactDomain));

    contactService.deleteContact(USER_ID, CONTACT_ID);
    verify(contactRepository, times(1)).delete(contactDomain);
  }

  @DisplayName("Should delete contact not found")
  @Test
  public void deleteContactNotFound() {
    doNothing().when(userService).existsUserDomainById(USER_ID);
    when(contactRepository.findByUserIdAndId(USER_ID, CONTACT_ID))
      .thenReturn(Optional.empty());
    when(messageError.create(Messages.CONTACT_NOT_FOUND)).thenReturn(apiError);

    assertThrows(NotFoundException.class,
      () -> contactService.deleteContact(USER_ID, CONTACT_ID));
  }

}
