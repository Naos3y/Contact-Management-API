package lopes.bruno.springwithreact.service;

import lopes.bruno.springwithreact.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface ContactService {
    Page<Contact>getAllContacts(int page, int size);
    Contact getContact(String id);
    Contact createContact(Contact contact);
    void deleteContact(String id);
    String uploadPhoto(String id, MultipartFile file);
}
