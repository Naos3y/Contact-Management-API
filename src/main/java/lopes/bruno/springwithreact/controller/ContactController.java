package lopes.bruno.springwithreact.controller;

import lombok.RequiredArgsConstructor;
import lopes.bruno.springwithreact.entity.Contact;
import lopes.bruno.springwithreact.service.ContactService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import static lopes.bruno.springwithreact.constant.Constant.PHOTO_DIRECTORY;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;
@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {
    private final ContactService contactService;

    /**
     * Creates a new contact.
     *
     * @param contact The contact details in the request body.
     * @return A ResponseEntity with the created contact and a location URI.
     *
     * - Takes the contact data from the request body.
     * - Calls the service to create the contact.
     * - Returns a response with the created contact and the location URI ("/contacts/userID").
     */
    @PostMapping
    public ResponseEntity<Contact> createContact(@RequestBody Contact contact) {
        return ResponseEntity.created(URI.create("/contacts/userID")).body(contactService.createContact(contact));
    }

    /**
     * Retrieves a paginated list of contacts.
     *
     * @param page The page number to retrieve (default is 0).
     * @param size The number of contacts per page (default is 10).
     * @return A ResponseEntity containing a paginated list of contacts.
     *
     * - Takes two request parameters: page and size.
     * - Calls the service to fetch contacts, paginated.
     * - Returns a response with the list of contacts.
     */
    @GetMapping
    public ResponseEntity<Page<Contact>> getContacts(@RequestParam(value = "page", defaultValue = "0") int page,
                                                     @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok().body(contactService.getAllContacts(page, size));
    }

    /**
     * Retrieves a contact by its ID.
     *
     * @param id The ID of the contact to retrieve.
     * @return A ResponseEntity with the contact details.
     *
     * - Takes the contact ID from the URL path.
     * - Calls the service to fetch the contact by ID.
     * - Returns the contact if found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContact(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(contactService.getContact(id));
    }

    /**
     * Uploads a photo for a contact.
     *
     * @param id The ID of the contact.
     * @param file The photo file to upload.
     * @return A ResponseEntity with the URL of the uploaded photo.
     *
     * - Takes the contact ID and photo file as request parameters.
     * - Calls the service to upload the photo and associate it with the contact.
     * - Returns the URL where the photo can be accessed.
     */
    @PutMapping("/photo")
    public ResponseEntity<String> uploadPhoto(@RequestParam("id") String id, @RequestParam("file")MultipartFile file) {
        return ResponseEntity.ok().body(contactService.uploadPhoto(id, file));
    }

    /**
     * Retrieves a photo by its filename.
     *
     * @param filename The name of the photo file to retrieve.
     * @return A byte array containing the photo data.
     * @throws IOException If the file cannot be read.
     *
     * - Takes the filename from the URL path.
     * - Reads the photo from the file system using the filename.
     * - Returns the photo as a byte array, either PNG or JPEG format.
     */
    @GetMapping(path = "/image/{filename}", produces = { IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE })
    public byte[] getPhoto(@PathVariable("filename") String filename) throws IOException {
        return Files.readAllBytes(Paths.get(PHOTO_DIRECTORY + filename));
    }
}
