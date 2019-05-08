package indi.pet.consumer.util;

/**
 * @author <a href="chan@ittx.com.cn">韩超</a>
 * @version 2019.04.14
 */
public class OrderStatus {

    final public static int ORDER_BEGIN=1;    //订单已发送，但未接受

    final public static int ORDER_RECEIVED=2; //商家已接单

    final public static int ORDER_HANDLED=4;  //商家准备商品

    final public static int ORDER_WORKING=8;  //商品准备完成，等待客户取货

    final public static int USER_CONFIRM=50;  //用户确认完成

    final public static int ORDER_FINISHED=100;//订单正常完成

    final public static int ORDER_CANCEL=999;   //订单被取消

}

