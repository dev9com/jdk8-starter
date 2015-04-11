package com.dev9.presentation.service;

import java.util.Map;

import com.dev9.presentation.model.Person;
import com.dev9.presentation.support.CallError;
import com.dev9.presentation.support.CallResult;
import com.google.common.collect.Maps;

public class PersonService {

    static final String FIRST_NAME_1 = "Pedro";
    static final String LAST_NAME_1 = "W";
    static final long ID_1 = 1L;

    static final String FIRST_NAME_2 = "Ashley";
    static final String LAST_NAME_2 = "W";
    static final long ID_2 = 2L;

    private static Map<Long, Person> personRepository;

    public PersonService() {
        personRepository = Maps.newHashMap();
        personRepository.put(1L, new Person().setId(ID_1).setFirstName(FIRST_NAME_1).setLastName(LAST_NAME_1));
        personRepository.put(2L, new Person().setId(ID_2).setFirstName(FIRST_NAME_2).setLastName(LAST_NAME_2));
    }

    public CallResult<Person> getPerson(Long id) {
        Person person = personRepository.get(id);
        if (person == null) {
            return CallResult.error(new CallError("http://person.service/url", "Person not in repository"));
        }
        return CallResult.ok(person);
    }

    public CallResult<Person> getPersonThrowsCheckedException(Long id) throws Exception {
        Person person = personRepository.get(id);
        if (person == null) {
            throw new Exception("Person not found");
        }
        return CallResult.ok(person);
    }

}
