//package cz.muni.ics.elasticsearch.dataclasses;
//
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.NonNull;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.elasticsearch.annotations.Document;
//
//import javax.validation.constraints.*;
//
//@Data
//@NoArgsConstructor
//@RequiredArgsConstructor
//@Document(indexName = "#{@appUserIndex}", type = "user", shards = 2, replicas = 2)
//public class UserData {
//
//    @Id
//    private String id;
//
//    @NonNull
//    @NotBlank
//    @Size(min = 3, max = 30)
//    private String name;
//
//    @NonNull
//    @NotBlank
//    @Size(min = 3, max = 30)
//    private String surname;
//
//    @NonNull
//    @Email
//    @NotEmpty
//    private String email;
//
//    @NonNull
//    @Size(min = 6, max = 30)
//    @Pattern(
//            regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,30}$",
//            message = "6 to 30 character password requiring numbers and both lowercase and uppercase letters"
//    )
//    private String password;
//}
