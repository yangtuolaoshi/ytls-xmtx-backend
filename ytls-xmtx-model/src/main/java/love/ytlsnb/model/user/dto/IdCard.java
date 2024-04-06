package love.ytlsnb.model.user.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @author ula
 * @date 2024/2/15 13:46
 */
@Data
@ToString
public class IdCard {
    private String name;
    private String gender;
    private String nationality;
    private String birthDate;
    private String address;
    private String idNumber;
}
