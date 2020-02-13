package br.com.vaneli.api.services;

import br.com.vaneli.api.domain.ContactDomain;
import br.com.vaneli.api.filters.ContactFilter;
import br.com.vaneli.api.interfaces.json.Contact;
import br.com.vaneli.api.interfaces.json.ContactPatch;
import br.com.vaneli.api.interfaces.json.ContactPost;
import br.com.vaneli.api.interfaces.json.ContactPut;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface ContactService {

  ContactDomain postContact(UUID userId, ContactPost contactPost);

  Page<Contact> getContacts(
    UUID userId,
    ContactFilter contactFilter,
    Integer offset,
    Integer limit);

  Contact getContact(UUID userId, UUID contactId);

  ContactDomain getContactDomain(UUID userId, UUID contactId);


  void putContact(UUID userId, UUID contactId, ContactPut contactPut);

  void patchContact(UUID userId, UUID contactId, ContactPatch contactPatch);

  void deleteContact(UUID userId, UUID contactId);
}
