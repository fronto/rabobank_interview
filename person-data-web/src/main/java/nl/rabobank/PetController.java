package nl.rabobank;

import nl.rabobank.interview.domain.Pet;
import nl.rabobank.interview.domain.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
public class PetController {

    @Autowired
    private PetRepository petRepository;

    @GetMapping("/pet/")
    public ResponseEntity<List<PetDto>> index() {

        List<PetDto> allPets = petRepository.findAll()
                .stream()
                .map(PetController::serializePetToDto)
                .collect(toList());

        return new ResponseEntity<>(allPets, HttpStatus.OK);
    }

    @PostMapping("/pet/")
    public ResponseEntity<PetDto> createPet(@RequestBody PetDto petDto) {

        Pet parsedPet = parsePetDto(petDto);
        Pet newPet = petRepository.save(parsedPet);

        PetDto newPetDto = serializePetToDto(newPet);
        return new ResponseEntity<>(newPetDto, HttpStatus.CREATED);

    }

    private static PetDto serializePetToDto(Pet newPet) {
        PetDto toReturn = new PetDto();
        toReturn.setId(Optional.of(newPet.getId()));
        toReturn.setName(newPet.getName());
        toReturn.setAge(newPet.getAge());
        return toReturn;
    }

    private static Pet parsePetDto(PetDto petDto) {
        return new Pet(petDto.getName(), petDto.getAge());
    }

    @GetMapping("/pet/{id}/")
    public ResponseEntity<Pet> retrievePet(@PathVariable String id) {

        Long idNumber = Long.valueOf(id);
        Optional<Pet> lookUpResult = petRepository.findById(idNumber);

        return lookUpResult
                .map(PetController::serializePetToDto)
                .map(dto -> new ResponseEntity(dto, HttpStatus.OK))
                .orElse(new ResponseEntity(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/pet/{id}/")
    public ResponseEntity<PetDto> modifyPet(@PathVariable String id, PetDto petDto) {

        Pet modified = parsePetDto(petDto);
        Long idNumber = Long.valueOf(id);

        if (petRepository.existsById(idNumber)) {
            Pet update = new Pet(idNumber, modified.getName(), modified.getAge());
            Pet savedUpdate = petRepository.save(update);
            PetDto toReturn = serializePetToDto(savedUpdate);
            return new ResponseEntity<>(toReturn, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @DeleteMapping("/pet/{id}/")
    public ResponseEntity<Void> deletePet(@PathVariable String id) {

        Long idNumber = Long.valueOf(id);
        petRepository.deleteById(idNumber);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}
