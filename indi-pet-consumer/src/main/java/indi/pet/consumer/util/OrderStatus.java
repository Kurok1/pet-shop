package indi.pet.consumer.util;

/**
 * @author <a href="chan@ittx.com.cn">韩超</a>
 * @version 2019.04.14
 */
public class OrderStatus {

    final public static Integer ORDER_BEGIN=1;    //订单已发送，但未接受

    final public static Integer ORDER_RECEIVED=2; //商家已接单

    final public static Integer ORDER_HANDLED=4;  //商家准备商品

    final public static Integer ORDER_WORKING=8;  //商品准备完成，等待客户取货

    final public static Integer ORDER_FINISHED=100;//订单正常完成

    final public static Integer ORDER_CANCEL=99;   //订单被取消

}

