package cn.liweidan.web.common.ex;

/**
 * 项目查询不到异常
 *
 * @author liweidan
 * @date 2018/8/22 下午3:16
 * @email toweidan@126.com
 */
public class ItemNotFoundException extends RuntimeException {

    private String id;

    public ItemNotFoundException(String id) {
        super("未找到id为" + id + "的资源");
    }

    public String getId() {
        return id;
    }
}
