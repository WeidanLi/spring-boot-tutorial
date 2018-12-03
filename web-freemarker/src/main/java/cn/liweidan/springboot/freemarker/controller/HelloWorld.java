package cn.liweidan.springboot.freemarker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Description：控制器
 *
 * @author liweidan
 * @version 1.0
 * @date 2018/11/22 10:09 PM
 * @email toweidan@126.com
 */
@Controller
public class HelloWorld {

    @RequestMapping("hello")
    public ModelAndView helloWorld() {
        String msg = "HelloWorld!" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        return new ModelAndView("hello").addObject("message", msg);
    }

}
