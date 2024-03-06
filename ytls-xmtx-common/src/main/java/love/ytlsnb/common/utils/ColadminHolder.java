package love.ytlsnb.common.utils;

import love.ytlsnb.model.coladmin.po.Coladmin;

/**
 * @author ula
 * @date 2024/2/7 10:48
 */
public class ColadminHolder {
    /**
     * 独立线程保存管理员状态
     */
    private static final ThreadLocal<Coladmin> TL = new ThreadLocal<>();

    /**
     * 保存管理员信息
     *
     * @param coladmin 管理员信息
     */
    public static void saveColadmin(Coladmin coladmin) {
        TL.set(coladmin);
    }

    /**
     * 获取管理员信息
     *
     * @return 管理员信息
     */
    public static Coladmin getColadmin() {
        return TL.get();
    }

    /**
     * 移出管理员信息
     */
    public static void removeColadmin() {
        TL.remove();
    }
}
