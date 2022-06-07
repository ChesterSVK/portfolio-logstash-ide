//package cz.muni.ics.services.implementations.elasticsearch;
//
//import cz.muni.ics.elasticsearch.dataclasses.UserData;
//import cz.muni.ics.elasticsearch.repositories.UserDataRepository;
//import cz.muni.ics.elasticsearch.validators.EmailValidator;
//import cz.muni.ics.elasticsearch.validators.StringValidator;
//import cz.muni.ics.elasticsearch.validators.UserValidator;
//import cz.muni.ics.services.implementations.web.dto.UserDataDto;
//import cz.muni.ics.services.interfaces.elasticsearch.UserDataService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.annotation.Resource;
//import java.util.Optional;
//
//@Slf4j
//@Service
//@Transactional
//public class UserServiceImpl implements UserDataService {
//
//    @Autowired
//    private UserValidator userValidator;
//
//    @Autowired
//    private StringValidator stringValidator;
//
//    @Autowired
//    private EmailValidator emailValidator;
//
//    @Resource
//    private UserDataRepository userRepository;
//
//    @Override
//    public UserData saveUser(UserDataDto userDataDto) {
//        UserData user = getUser(userDataDto);
//        userValidator.validate(user);
//        log.debug("Saving user {}", user);
//        return userRepository.save(user);
//    }
//
//    private UserData getUser(UserDataDto userDataDto) {
//        UserData userData = new UserData();
//        userData.setName(userDataDto.getName());
//        userData.setEmail(userDataDto.getEmail());
//        userData.setSurname(userDataDto.getSurname());
//        userData.setId(userDataDto.getId());
//        userData.setPassword(userDataDto.getPassword());
//        return userData;
//    }
//
//    @Override
//    public void deleteUser(UserDataDto userDataDto) {
//        UserData user = getUser(userDataDto);
//        log.debug("Deleting user {}", user);
//        userRepository.delete(user);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public UserData findOne(String id) {
//        stringValidator.validate(id);
//        log.debug("Searching for user with id {}", id);
//        Optional<UserData> found = userRepository.findById(id);
//        return found.orElse(null);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public Iterable<UserData> findAllUsers() {
//        log.debug("Searching for all users");
//        return userRepository.findAll();
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public Page<UserData> findUserByName(String name) {
//        stringValidator.validate(name);
//        log.debug("Searching for users with name: {}", name);
//        return userRepository.findUserByName(name, Pageable.unpaged());
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public Page<UserData> findUserBySurname(String surname) {
//        stringValidator.validate(surname);
//        log.debug("Searching for users with surname: {}", surname);
//        return userRepository.findUserBySurname(surname, Pageable.unpaged());
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public Page<UserData> findUserByEmail(String email) {
//        emailValidator.validate(email);
//        log.debug("Searching for users with mail: {}", email);
//        return userRepository.findUserByEmail(email, Pageable.unpaged());
//    }
//
//    @Override
//    public void deleteAllUsers() {
//        log.debug("Deleting all users");
//        userRepository.deleteAll();
//    }
//}
