package indi.pet.producer.util;

/**
 * 订单状态类
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2018.11.05
 */
public class OrderStatus {

    /**
     * 买家刚刚提交订单,卖家未做任何响应
     */
    public static final Integer CREATED=0;

    /**
     * 卖家确认订单
     */
    public static final Integer ACCEPTED=1;

    /**
     * 订单已经完成
     */
    public static final Integer FINISHED=2;

    /**
     * 订单被取消
     */
    public static final Integer CANCELED=4;
}
