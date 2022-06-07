//package cz.muni.ics.elasticsearch.repositories;
//
//import cz.muni.ics.elasticsearch.dataclasses.UserData;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface UserDataRepository extends ElasticsearchRepository<UserData, String> {
//    Page<UserData> findUserByName(String name, Pageable pageable);
//    Page<UserData> findUserBySurname(String surname, Pageable pageable);
//    Page<UserData> findUserByEmail(String email, Pageable pageable);
//}
