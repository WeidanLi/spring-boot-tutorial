package cn.liweidan.springboot.validate.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 验证纯中文的注解
 */
@Constraint(validatedBy = { ChsValidator.class }) // 定义注解的校验器
@Target({ElementType.FIELD, ElementType.PARAMETER}) // 定义该注解使用在类属性上
@Retention(RUNTIME) // 运行时有效
@Documented
public @interface Chs {

    /** 可以自定义属性，这个属性暂时没什么用 */
    String chineseName() default "";

    /** 以下是注解必须的三个属性，分别是定义组别、错误等级以及提示信息 */
    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    String message() default "该字段需要纯中文";

}
