package br.com.vaneli.api.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.vaneli.api.domain.AddressDomain;
import br.com.vaneli.api.domain.UserDomain;
import br.com.vaneli.api.exceptions.MessageError;
import br.com.vaneli.api.exceptions.MessageError.ApiError;
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
public class UserServiceImplTest {

  private static Integer LIMIT;
  private static Integer OFFSET;
  private static UUID USER_ID;
  private  static String CEP;

  private UserService userService;

  @Mock
  private MessageError messageError;
  @Mock
  private UserRepository userRepository;
  @Mock
  private ApiError apiError;
  @Mock
  private Predicate predicate;
  @Mock
  private PageRequest pageRequest;
  @Mock
  private UserFilter userFilter;
  @Mock
  private UserDomain userDomain;
  @Mock
  private UserPost userPost;
  @Mock
  private UserPut userPut;
  @Mock
  private UserPatch userPatch;
  @Mock
  private User user;
  @Mock
  private CepService cepService;
  @Mock
  private Address address;
  @Mock
  private AddressDomain addressDomain;
  @Mock
  private CepSender cepSender;
  @Mock
  private CepData cepData;

  @BeforeAll
  public static void beforeAll() {
    OFFSET = 0;
    LIMIT = 50;
    USER_ID = UUID.randomUUID();
    CEP = "123456";
  }

  @BeforeEach
  public void beforeEach() {
    userService = spy(new UserServiceImpl(messageError, userRepository, cepService, cepSender));
  }

  @DisplayName("Should add user successfully")
  @Test
  public void postUserSuccessfully() {
    when(userPost.toUserDomain()).thenReturn(userDomain);
    when(userRepository.save(userDomain)).thenReturn(userDomain);

    assertEquals(userDomain, userService.postUser(userPost));
  }

  @DisplayName("Should add user with cep successfully")
  @Test
  public void postUserWithCepSuccessfully() {
    when(userPost.toUserDomain()).thenReturn(userDomain);
    when(userRepository.save(userDomain)).thenReturn(userDomain);
//    when(userDomain.getCep()).thenReturn(CEP);
//    when(cepService.getCep(CEP)).thenReturn(address);
//    when(address.toAddressDomain()).thenReturn(addressDomain);

    assertEquals(userDomain, userService.postUser(userPost));
  }

  @DisplayName("Should get users successfully")
  @Test
  public void getUsersSuccessfully() {
    new MockUp<PageRequest>() {
      @mockit.Mock
      public PageRequest of(int page, int size) {
        if (page != OFFSET || size != LIMIT) {
          throw new IllegalArgumentException("Stubbing has different values.");
        }
        return pageRequest;
      }
    };

    when(userFilter.toPredicate()).thenReturn(predicate);
    when(userRepository.findAll(predicate, pageRequest))
      .thenReturn(new PageImpl<>(List.of(userDomain)));
    when(userDomain.toUser()).thenReturn(user);

    assertEquals(new PageImpl<>(List.of(user)), userService.getUsers(
      userFilter,
      OFFSET,
      LIMIT
    ));
  }

  @DisplayName("Should get user by id successfully")
  @Test
  public void getUserByIdSuccessfully() {
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(userDomain));
    when(userDomain.toUser()).thenReturn(user);

    assertEquals(user, userService.getUser(USER_ID));
  }

  @DisplayName("Should get user by id not found")
  @Test
  public void getUserByIdNotFound() {
    when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());
    when(messageError.create(Messages.USER_NOT_FOUND)).thenReturn(apiError);

    assertThrows(NotFoundException.class,
      () -> userService.getUser(USER_ID));
  }

  @DisplayName("Should patch user successfully")
  @Test
  public void patchUserSuccessfully() {
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(userDomain));
    when(userRepository.save(userDomain)).thenReturn(userDomain);

    userService.patchUser(USER_ID, userPatch);
  }

  @DisplayName("Should patch user not found")
  @Test
  public void patchUserNotFound() {
    when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());
    when(messageError.create(Messages.USER_NOT_FOUND)).thenReturn(apiError);

    assertThrows(NotFoundException.class,
      () -> userService.patchUser(USER_ID, userPatch));
  }

  @DisplayName("Should put user successfully")
  @Test
  public void putUserSuccessfully() {
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(userDomain));
    when(userRepository.save(userDomain)).thenReturn(userDomain);

    userService.putUser(USER_ID, userPut);
  }

  @DisplayName("Should put user not found")
  @Test
  public void putUserNotFound() {
    when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());
    when(messageError.create(Messages.USER_NOT_FOUND)).thenReturn(apiError);

    assertThrows(NotFoundException.class,
      () -> userService.putUser(USER_ID, userPut));
  }

  @DisplayName("Should delete user successfully")
  @Test
  public void deleteUserSuccessfully() {
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(userDomain));

    userService.deleteUser(USER_ID);
    verify(userRepository, times(1)).delete(userDomain);
  }

  @DisplayName("Should delete user not found")
  @Test
  public void deleteUserNotFound() {
    when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());
    when(messageError.create(Messages.USER_NOT_FOUND)).thenReturn(apiError);

    assertThrows(NotFoundException.class,
      () -> userService.deleteUser(USER_ID));
  }

}
