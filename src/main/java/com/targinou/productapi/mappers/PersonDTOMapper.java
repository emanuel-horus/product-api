package com.targinou.productapi.mappers;



import com.targinou.productapi.dto.PersonDTO;
import com.targinou.productapi.model.Person;
import org.springframework.stereotype.Component;

@Component
public class PersonDTOMapper implements DtoMapper<Person, PersonDTO> {
    @Override
    public PersonDTO toDto(Person entity) {
        return new PersonDTO(
                entity.getId(),
                entity.getName(),
                entity.getIdentifier(),
                entity.getEmail(),
                entity.getPhoneNumber(),
                entity.getBirthDate()
        );
    }

    @Override
    public Person toEntity(PersonDTO personDTO) {
        return Person.builder()
                .id(personDTO.id())
                .name(personDTO.name())
                .email(personDTO.email())
                .birthDate(personDTO.birthDate())
                .identifier(personDTO.identifier())
                .phoneNumber(personDTO.phoneNumber())
                .build();
    }
}
