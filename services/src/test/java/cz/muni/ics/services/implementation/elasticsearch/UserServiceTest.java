//package cz.muni.ics.services.implementation.elasticsearch;
//
//import cz.muni.ics.elasticsearch.dataclasses.ApplicationData;
//import cz.muni.ics.elasticsearch.dataclasses.UserData;
//import cz.muni.ics.elasticsearch.exceptions.ValidationException;
//import cz.muni.ics.services.ServiceApp;
//import cz.muni.ics.services.implementations.web.dto.UserDataDto;
//import cz.muni.ics.services.interfaces.elasticsearch.UserDataService;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
//import org.springframework.test.context.TestContext;
//import org.springframework.test.context.TestExecutionListeners;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.context.support.AbstractTestExecutionListener;
//import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
//
//import static org.assertj.core.api.Java6Assertions.assertThat;
//import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
//
//@RunWith(SpringRunner.class)
//@TestExecutionListeners(listeners = {
//        DependencyInjectionTestExecutionListener.class,
//        UserServiceTest.class})
//@TestPropertySource("classpath:application-elasticsearch-test.properties")
//@SpringBootTest(classes = ServiceApp.class)
//public class UserServiceTest extends AbstractTestExecutionListener {
//
//    private static final UserDataDto ORIGINAL_USER =
//            new UserDataDto("testName", "testSurname", "testMail@mail.test", "testPass12");
//    private static final UserDataDto TEST_USER =
//            new UserDataDto("testName", "testSurname", "testMail@mail.test", "testPass12");
//    private static final PageRequest PAGE_REQUEST = PageRequest.of(0, 1);
//
//    @Autowired
//    private UserDataService userService;
//
//    @Autowired
//    private ElasticsearchTemplate esTemplate;
//
//    @Override
//    public void beforeTestClass(TestContext testContext) {
//        this.esTemplate = testContext.getApplicationContext().getBean(ElasticsearchTemplate.class);
//        esTemplate.deleteIndex(UserData.class);
//        esTemplate.createIndex(UserData.class);
//        esTemplate.putMapping(UserData.class);
//        esTemplate.refresh(UserData.class);
//    }
//
//    @Before
//    public void before() {
//        userService.deleteAllUsers();
//    }
//
//    @After
//    public void resetData() {
//        TEST_USER.setId(ORIGINAL_USER.getId());
//        TEST_USER.setName(ORIGINAL_USER.getName());
//        TEST_USER.setEmail(ORIGINAL_USER.getEmail());
//        TEST_USER.setSurname(ORIGINAL_USER.getSurname());
//        TEST_USER.setPassword(ORIGINAL_USER.getPassword());
//    }
//
//    @Override
//    public void afterTestClass(TestContext testContext) {
//        esTemplate.deleteIndex(UserData.class);
//    }
//
//    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////Save
//    @Test
//    public void testSaveUser() {
//
//        UserData savedUser = userService.saveUser(TEST_USER);
//
//        assertThat(savedUser.getId()).isNotNull();
//        assertThat(savedUser.getName()).isEqualTo(TEST_USER.getName());
//        assertThat(savedUser.getSurname()).isEqualTo(TEST_USER.getSurname());
//        assertThat(savedUser.getEmail()).isEqualTo(TEST_USER.getEmail());
//        assertThat(savedUser.getPassword()).isEqualTo(TEST_USER.getPassword());
//    }
//
//    @Test
//    public void testSaveInvalidNameUser() {
//
//        TEST_USER.setName("");
//        assertThatThrownBy( () -> userService.saveUser(TEST_USER)).isInstanceOf(ValidationException.class);
//
//        TEST_USER.setName("x");
//        assertThatThrownBy( () -> userService.saveUser(TEST_USER)).isInstanceOf(ValidationException.class);
//    }
//
//    @Test
//    public void testSaveInvalidSurnameUser() {
//
//        TEST_USER.setSurname("");
//        assertThatThrownBy( () -> userService.saveUser(TEST_USER)).isInstanceOf(ValidationException.class);
//
//        TEST_USER.setSurname("x");
//        assertThatThrownBy( () -> userService.saveUser(TEST_USER)).isInstanceOf(ValidationException.class);
//    }
//
//    @Test
//    public void testSaveInvalidEmailUser() {
//
//        TEST_USER.setEmail("");
//        assertThatThrownBy( () -> userService.saveUser(TEST_USER)).isInstanceOf(ValidationException.class);
//
//        TEST_USER.setEmail("email");
//        assertThatThrownBy( () -> userService.saveUser(TEST_USER)).isInstanceOf(ValidationException.class);
//
//        TEST_USER.setEmail("email@");
//        assertThatThrownBy( () -> userService.saveUser(TEST_USER)).isInstanceOf(ValidationException.class);
//    }
//
//
//    @Test
//    public void testSaveInvalidPasswordUser() {
//
//        TEST_USER.setPassword("");
//        assertThatThrownBy( () -> userService.saveUser(TEST_USER)).isInstanceOf(ValidationException.class);
//
//        TEST_USER.setPassword("p");
//        assertThatThrownBy( () -> userService.saveUser(TEST_USER)).isInstanceOf(ValidationException.class);
//    }
//
//    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////Find
//    @Test
//    public void findOneUser() {
//        UserData savedUser = userService.saveUser(TEST_USER);
//        UserData foundUser = userService.findOne(savedUser.getId());
//
//        assertThat(savedUser).isEqualTo(foundUser);
//    }
//
//    @Test
//    public void findAllUsers() {
//        UserData savedUser = userService.saveUser(TEST_USER);
//        Iterable<UserData> foundUsers = userService.findAllUsers();
//
//        assertThat(foundUsers.iterator().hasNext()).isTrue();
//        assertThat(foundUsers.iterator().next()).isEqualTo(savedUser);
//    }
//
//    @Test
//    public void findUserByName() {
//        UserData savedUser = userService.saveUser(TEST_USER);
//        Page<UserData> foundUser = userService.findUserByName(savedUser.getName());
//
//        assertThat(foundUser.getTotalElements()).isEqualTo(1);
//        assertThat(foundUser.iterator().next()).isEqualTo(savedUser);
//    }
//
//    @Test
//    public void findUserBySurname() {
//        UserData savedUser = userService.saveUser(TEST_USER);
//        Page<UserData> foundUser = userService.findUserBySurname(savedUser.getSurname());
//
//        assertThat(foundUser.getTotalElements()).isEqualTo(1);
//        assertThat(foundUser.iterator().next()).isEqualTo(savedUser);
//    }
//
//    @Test
//    public void findUserByEmail() {
//        UserData savedUser = userService.saveUser(TEST_USER);
//        Page<UserData> foundUser = userService.findUserByEmail(savedUser.getEmail());
//
//        assertThat(foundUser.getTotalElements()).isEqualTo(1);
//        assertThat(foundUser.iterator().next()).isEqualTo(savedUser);
//    }
//
//    @Test
//    public void findOneInvalidUser() {
//        UserData foundUser = userService.findOne("00000000");
//        assertThat(foundUser).isNull();
//    }
//
//    @Test
//    public void findUserByInvalidId() {
//        assertThatThrownBy(() -> userService.findOne(null)).isInstanceOf(ValidationException.class);
//    }
//
//    @Test
//    public void findUserByInvalidName() {
//        assertThatThrownBy(() -> userService.findUserByName(null))
//                .isInstanceOf(ValidationException.class);
//        assertThatThrownBy(() -> userService.findUserByName(""))
//                .isInstanceOf(ValidationException.class);
//    }
//
//    @Test
//    public void findUserByInvalidSurname() {
//        assertThatThrownBy(() -> userService.findUserBySurname(null))
//                .isInstanceOf(ValidationException.class);
//        assertThatThrownBy(() -> userService.findUserBySurname(""))
//                .isInstanceOf(ValidationException.class);
//    }
//
//    @Test
//    public void findUserByInvalidEmail() {
//        assertThatThrownBy(() -> userService.findUserByEmail(""))
//                .isInstanceOf(ValidationException.class);
//        assertThatThrownBy(() -> userService.findUserByEmail("email"))
//                .isInstanceOf(ValidationException.class);
//        assertThatThrownBy(() -> userService.findUserByEmail("email@"))
//                .isInstanceOf(ValidationException.class);
//    }
//
//    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////Delete
//    @Test
//    public void testDeleteUser() {
//        UserData savedUser = userService.saveUser(TEST_USER);
//        UserDataDto userDataDto = new UserDataDto();
//        userDataDto.setEmail(savedUser.getEmail());
//        userDataDto.setName(savedUser.getName());
//        userDataDto.setPassword(savedUser.getPassword());
//        userDataDto.setSurname(savedUser.getSurname());
//        userDataDto.setId(savedUser.getId());
//        userService.deleteUser(userDataDto);
//        assertThat(userService.findAllUsers().iterator().hasNext()).isFalse();
//    }
//
//}
