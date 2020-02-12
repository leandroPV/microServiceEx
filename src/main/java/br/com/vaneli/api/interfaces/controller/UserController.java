package br.com.vaneli.api.interfaces.controller;

import br.com.vaneli.api.domain.UserDomain;
import br.com.vaneli.api.interfaces.json.User;
import br.com.vaneli.api.interfaces.json.UserPost;
import br.com.vaneli.api.services.UserService;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/users")
@Validated
public class UserController implements BaseController<User> {

  private UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  public ResponseEntity<Void> postUser(
    @RequestBody @Valid UserPost userPost) {

    UserDomain userDomain = this.userService.postUser(userPost);
    return created(userDomain.getId());

  }
}
