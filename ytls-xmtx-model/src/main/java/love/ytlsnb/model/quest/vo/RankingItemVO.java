package love.ytlsnb.model.quest.vo;

import lombok.Data;

/**
 * 排行榜列表项
 *
 * @author 金泓宇
 * @date 2024/3/30
 */
@Data
public class RankingItemVO {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户的昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 积分
     */
    private Integer point;

    /**
     * 任务完成数
     */
    private Integer questFinishCount;

    /**
     * 排名
     */
    private Long rank;
}
