package cn.liweidan.springboot.validate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 中文验证器实现类
 * author : Weidan
 * Email : toweidan@126.com
 * version : v1.0
 * date : 2017/3/13
 */
public class ChsValidator implements ConstraintValidator<Chs, String> { // 实现ConstraintValidator接口，第一个泛型是注解类型，第二个是目标值的类型

    private String reg = "^[\\u0391-\\uFFE5]+[\\w*[\\u0391-\\uFFE5]*]*";

    @Override
    public void initialize(Chs constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (null == value) {
            return false;
        }
        return value.matches(reg);
    }

}
