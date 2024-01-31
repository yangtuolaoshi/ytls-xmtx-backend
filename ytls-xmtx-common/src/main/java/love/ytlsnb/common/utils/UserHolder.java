package love.ytlsnb.common.utils;

import love.ytlsnb.model.user.entity.User;

/**
 * APP用户状态保存
 *
 * @author 金泓宇
 * @date 2024/01/21
 */
public class UserHolder {
    /**
     * 独立线程保存用户状态
     */
    private static final ThreadLocal<User> tl = new ThreadLocal<>();

    /**
     * 保存用户信息
     * @param user 用户信息
     */
    public static void saveUser(User user){
        tl.set(user);
    }

    /**
     * 获取用户信息
     * @return 用户信息
     */
    public static User getUser(){
        return tl.get();
    }

    /**
     * 移出用户信息
     */
    public static void removeUser(){
        tl.remove();
    }
}
