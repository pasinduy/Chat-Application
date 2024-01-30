package dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter@Setter
@ToString
public class UserDto {
    private String username;
    private String passWord;
}
