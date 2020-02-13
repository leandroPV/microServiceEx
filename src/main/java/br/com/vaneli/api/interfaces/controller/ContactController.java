package br.com.vaneli.api.interfaces.controller;

import br.com.vaneli.api.domain.ContactDomain;
import br.com.vaneli.api.filters.ContactFilter;
import br.com.vaneli.api.interfaces.json.Contact;
import br.com.vaneli.api.interfaces.json.ContactPost;
import br.com.vaneli.api.interfaces.json.ContactPut;
import br.com.vaneli.api.services.ContactService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/users/{userId}/contacts")
@Validated
public class ContactController implements BaseController<Contact> {

  private final ContactService contactService;

  public ContactController(ContactService contactService) {
    this.contactService = contactService;
  }

  @PostMapping
  public ResponseEntity<Void> postContact(
    @PathVariable UUID userId,
    @RequestBody @Valid ContactPost contactPost) {
    ContactDomain contactDomain = this.contactService.postContact(userId, contactPost);
    return created(contactDomain.getId());
  }

  @GetMapping
  public ResponseEntity<List<Contact>> getContacts(
    @PathVariable UUID userId,
    @RequestParam("_offset") @PositiveOrZero @NotNull Integer offset,
    @RequestParam("_limit") @Positive @NotNull @Max(ACCEPT_RANGE) Integer limit,
    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime startCreated,
    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime endCreated,
    @RequestParam(required = false) String value
  ) {
    ContactFilter contactFilter = ContactFilter.builder()
      .startCreated(startCreated)
      .endCreated(endCreated)
      .value(value)
      .build();
    return partialContent(this.contactService.getContacts(userId, contactFilter, offset, limit),
      ACCEPT_RANGE);

  }

  @GetMapping("/{contactId}")
  public ResponseEntity<Contact> getContact(
    @PathVariable UUID userId,
    @PathVariable UUID contactId
  ) {
    return ok(this.contactService.getContact(userId, contactId));
  }

  @PutMapping("/{contactId}")
  public ResponseEntity<Void> putContact(
    @PathVariable UUID userId,
    @PathVariable UUID contactId,
    @RequestBody @Valid ContactPut contactPut
  ) {
    this.contactService.putContact(userId, contactId, contactPut);
    return noContent();
  }

}
