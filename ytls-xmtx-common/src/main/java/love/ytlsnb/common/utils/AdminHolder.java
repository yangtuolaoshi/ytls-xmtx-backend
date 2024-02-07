package love.ytlsnb.common.utils;

import love.ytlsnb.model.school.po.Admin;

/**
 * @author ula
 * @date 2024/2/7 10:48
 */
public class AdminHolder {
    /**
     * 独立线程保存管理员状态
     */
    private static final ThreadLocal<Admin> TL = new ThreadLocal<>();

    /**
     * 保存管理员信息
     *
     * @param admin 管理员信息
     */
    public static void saveAdmin(Admin admin) {
        TL.set(admin);
    }

    /**
     * 获取管理员信息
     *
     * @return 管理员信息
     */
    public static Admin getAdmin() {
        return TL.get();
    }

    /**
     * 移出管理员信息
     */
    public static void removeAdmin() {
        TL.remove();
    }
}
