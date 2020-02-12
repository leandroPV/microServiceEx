package br.com.vaneli.api.interfaces.controller;

import br.com.vaneli.api.domain.UserDomain;
import br.com.vaneli.api.filters.UserFilter;
import br.com.vaneli.api.interfaces.json.User;
import br.com.vaneli.api.interfaces.json.UserPost;
import br.com.vaneli.api.services.UserService;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

  @GetMapping
  public ResponseEntity<List<User>> getUsers(
    @RequestParam("_offset") @PositiveOrZero @NotNull Integer offset,
    @RequestParam("_limit") @Positive @NotNull @Max(ACCEPT_RANGE) Integer limit,
    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime startCreated,
    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime endCreated,
    @RequestParam(required = false) String name,
    @RequestParam(required = false) String cpf
  ) {
    UserFilter userFilter = UserFilter.builder()
      .startCreated(startCreated)
      .endCreated(endCreated)
      .name(name)
      .cpf(cpf)
      .build();
    return partialContent(this.userService.getUsers(userFilter, offset, limit), ACCEPT_RANGE);
  }

  @GetMapping("/{userId}")
  public ResponseEntity<User> getUser(@PathVariable UUID userId) {
    return ok(this.userService.getUser(userId));
  }

}
