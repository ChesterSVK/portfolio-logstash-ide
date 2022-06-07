//package cz.muni.ics.services.implementations.elasticsearch;
//
//import cz.muni.ics.elasticsearch.dataclasses.UserData;
//import cz.muni.ics.elasticsearch.repositories.UserDataRepository;
//import cz.muni.ics.services.implementations.web.dto.UserDataDto;
//import cz.muni.ics.services.exceptions.EmailExistsException;
//import cz.muni.ics.services.interfaces.elasticsearch.AdminDtoService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.annotation.Resource;
//
///**
// * Created by Jozef Cibík on 21.05.2018.
// */
//@Slf4j
//@Service
//public class AdminServiceImpl implements AdminDtoService {
//
//    @Resource
//    private UserDataRepository userRepository;
//
//    @Transactional
//    @Override
//    public UserData registerAdminAccount(UserDataDto userDto) throws EmailExistsException {
//
//        if (emailExist(userDto.getEmail())) {
//            throw new EmailExistsException(
//                    "There is an account with that email adress: "
//                            +  userDto.getEmail());
//        }
//
//
//        UserData user = new UserData();
//        user.setName(userDto.getName());
//        user.setSurname(userDto.getSurname());
//        user.setPassword(userDto.getPassword());
//        user.setEmail(userDto.getEmail());
//        log.info("Saving user {}", user);
//        return userRepository.save(user);
//    }
//
//    private boolean emailExist(String email) {
//        Page<UserData> user = userRepository.findUserByEmail(email, PageRequest.of(0, 1));
//        return user.getTotalElements() == 0;
//    }
//
//    @Override
//    public boolean deleteAdminAccount(UserDataDto userDto) {
//        return false;
//    }
//}
