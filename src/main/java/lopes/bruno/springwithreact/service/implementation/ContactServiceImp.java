package lopes.bruno.springwithreact.service.implementation;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lopes.bruno.springwithreact.entity.Contact;
import lopes.bruno.springwithreact.exception.ResourceNotFoundException;
import lopes.bruno.springwithreact.repository.ContactRepository;
import lopes.bruno.springwithreact.service.ContactService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static lopes.bruno.springwithreact.constant.Constant.PHOTO_DIRECTORY;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class ContactServiceImp implements ContactService {
    private final ContactRepository contactRepository;

    public Page<Contact> getAllContacts(int page, int size) {
        return contactRepository.findAll(PageRequest.of(page, size, Sort.by("name")));
    }

    public Contact getContact(String id) {
        return contactRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contact not found"));
    }

    public Contact createContact(Contact contact) {
        return contactRepository.save(contact);
    }

    public void deleteContact(String id) {
        Contact contact = getContact(id);
        contactRepository.deleteById(id);
    }

    /**
     * Uploads a photo for a contact.
     *
     * @param id The ID of the contact.
     * @param file The image file to upload.
     * @return The URL where the uploaded photo can be accessed.
     */
    public String uploadPhoto(String id, MultipartFile file) {
        Contact contact = getContact(id);
        String photoUrl = photoFunction.apply(id, file);
        contact.setPhotoUrl(photoUrl);
        contactRepository.save(contact);
        return photoUrl;
    }

    /**
     * Determines the file extension from a filename.
     *
     * Takes a String (filename)
     * Returns a String
     *
     * If the filename contains a period (.), it extracts the file extension (like .jpg or .png).
     * If the filename doesn't contain a period, it defaults to .png.
     */
    private final Function<String, String> fileExtension = filename -> Optional.of(filename)
            .filter(name -> name.contains("."))
            .map(name -> "." + name.substring(filename.lastIndexOf(".") + 1))
            .orElse(".png");

    /**
     * Saves the image for a contact and returns the URL to access it.
     *
     * Takes a String (id) and a MultipartFile (image)
     * Returns a String (URL to the uploaded image)
     *
     * - The image is stored in a directory (PHOTO_DIRECTORY).
     * - If the directory doesn't exist, it creates it.
     * - The file is saved with the format: contact's id + file extension (e.g., "12345.png").
     * - If an image already exists with the same name, it is replaced.
     * - After saving, it returns a URL where the image can be accessed.
     */
    private final BiFunction<String, MultipartFile, String> photoFunction = (id, image) -> {
        String filename = id + fileExtension.apply(image.getOriginalFilename());
        try {
            Path fileStorageLocation = Paths.get(PHOTO_DIRECTORY).toAbsolutePath().normalize();
            if(!Files.exists(fileStorageLocation)) { Files.createDirectories(fileStorageLocation); }
            Files.copy(image.getInputStream(), fileStorageLocation.resolve(filename), REPLACE_EXISTING);
            return ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/contacts/image/" + filename).toUriString();
        }catch (Exception exception) {
            throw new RuntimeException("Unable to save image");
        }
    };



}
