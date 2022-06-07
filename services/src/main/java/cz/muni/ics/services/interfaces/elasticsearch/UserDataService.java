//package cz.muni.ics.services.interfaces.elasticsearch;
//
//import cz.muni.ics.elasticsearch.dataclasses.UserData;
//import cz.muni.ics.services.implementations.web.dto.UserDataDto;
//import org.springframework.data.domain.Page;
//
//public interface UserDataService {
//
//
//    UserData saveUser(UserDataDto user);
//    void deleteUser(UserDataDto user);
//
//    UserData findOne(String id);
//    Iterable<UserData> findAllUsers();
//    Page<UserData> findUserByName(String name);
//    Page<UserData> findUserBySurname(String surname);
//    Page<UserData> findUserByEmail(String email);
//
//    void deleteAllUsers();
//}
