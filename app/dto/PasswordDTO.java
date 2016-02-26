package dto;

import play.data.validation.Constraints;

/**
 * Created by Anton Chernov on 2/21/2016.
 */
public class PasswordDTO {
    @Constraints.Required
    @Constraints.MinLength(8)
    @Constraints.MaxLength(40)
    @Constraints.Pattern("(\\w|\\d)*")
    public String newPassword;

    @Constraints.Required
    public String oldPassword;
}
