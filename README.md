# 基于SpringBoot的宠物社交软件的设计与实现
### 开发进度

#### 目前项目进度（截止到3-24）

宠物店端（以下称productor）：

后台基本功能已经完成，已经实现的功能包括

- 登录注册

- 商品信息的发布修改删除

- 从队列拉取订单，分条件查询订单

普通用户端（以下称consumer）：

后台基本功能已经完成，已经实现的功能包括

- 用户登录注册
- 好友的添加，动态消息的发布查看和评论
- 订单的发起

------

#### **本周项目完成情况（3-25至3-31）**：

productor：

登录注册页面完成，且已经与后台接口做好了对接（3-27）

主页面布局完成但未完成与后台接口的对接，包括四个子页面，分别是：
- 库存页面，用于查看管理已经发布的销售信息（3-30）
- 聊天页面，用于与顾客进行聊天【聊天具体实现后台未完成】（3-30）
- 订单页面，用于查看管理订单（3-30）
- 信息页面，宠物店主的信息的查看修改（3-30） 

![images/](https://github.com/Kurok1/pet-shop/blob/master/images/1.png)

consumer：

- 登录注册页面完成，且已经与后台接口做好了对接（3-26）
- 地图的展现，地图已经能在APP中展现（3-28）
- 聊天消息界面&好友动态界面&个人资料界面&好友列表界面（3-31）

![](https://github.com/Kurok1/pet-shop/blob/master/images/2.png)

**下周计划(4-1至4-7)：**

- [ ] 完成异步文件上传功能【拟用FormData进行实现，但是好像出了点问题，准备改用jquery.form实现】（4-1至4-2）
- [ ] consumer端的前后台对接（4-3至4-7）

------

#### **本周项目完成情况 （4-1至4-7）**

文件上传功能：

- 上传功能已经完成，可以通过AJAX这种异步方式进行上传文件，从而不需要刷新页面，提高用户体验（4-1）

consumer：

- 用户信息数据的页面渲染和修改，包括头像的上传与及时生效，基本信息的修改（4-2至4-3）
- 用户动态分享信息的发布和渲染页面（4-5）
- 用户动态信息的评论的发布获取，以及层次的进行展示（4-6）

productor：

- 商家信息数据的页面渲染和修改，包括头像的上传与及时生效，基本信息的修改（4-7）

**下周计划(4-8至4-14)：**

- [ ] 商家销售信息的发布，普通客户的获取（根据地图api）
- [ ] 普通客户指定销售信息，发起订单，商家接单

***

####  **本周项目完成情况 （4-8至4-14）**

consumer：

- 用户可以根据自身地理位置信息获取附件商家的信息，以信息窗口的形式展现，[信息窗口demo（腾讯官方）](https://lbs.qq.com/javascript_v2/doc/infowindow.html)（4-11）
- 用户能够正常浏览销售信息并发起订单（4-13）

productor：

- 发布销售信息，并携带自身地理位置信息，同时增加商家自定义位置信息（4-9至4-10）
- 商家能够接收到订单（4-13至4-14）

**Appendix:**

1. 用户端获取销售信息过程如下

```sql
   //inputs
   double latitude；  //纬度
   double longitude； //经度
   double accurate;   //精确程度，默认值为0.5，用于进行地理位置范围确定
   int size;          //获取商家的最大数
   
   //查询过程
   //1.先根据地理范围确定附件商家列表
   SELECT id
   FROM shopkeeper
   WHERE latitude BETWEEN latitude-accurate AND latitude+accurate
   AND longitude BETWEEN longitude-accurate AND longitude+accurate
   limit size;
   
   //2.根据商家列表，获取每个商家最近发布的信息
   SELECT column1,column2,column3,...
   FROM shock
   WHERE shock.shopkeeperId IN (?,?,?,...)
   GROUP BY shock.shopkeeperId
   ORDER BY shock.timestamp DESC //降序，时间戳越大表示是越新的
   Limit 1;
   
   //打包数据，封装成json格式
   {
       "size": 5,
       "accurate": 0.5,
       "latitude": 39.092,
       "longitude": 117.39811,
       "shocks": [
           {
               "shopkeeperId": "AWn2zZXXTHjYzWcv3v45",
               "shopkeeperLogo": "AWn23MI6THjYzWcv3v5M",
               "shopkeeperName": "Hello Doge",
               "shock": {
                   "id": "AWoVzkiFUJOR1j7HV_RI",
                   "resources": [
                       "AWoVzjFUUJOR1j7HV_RG",
                       "AWoVzjIBUJOR1j7HV_RH"
                   ],
                   "title": "123库存标题1123456456",
                   "text": " 来点文字描述吧.\n                            ",
                   "shopkeeperId": "AWn2zZXXTHjYzWcv3v45",
                   "amount": 0,
                   "last": 9,
                   "price": 11,
                   "timestamp": 0
               },
               "latitude": 39.09204,
               "longitude": 117.09811
           }
       ]
   }
```

2. 序列化对象需要注意的问题（踩坑记）

```Java
//由于是两个系统之间使用消息队列进行传输，必然要将订单实体对象进行序列化操作
//但是由于两个系统的订单实体对象使用的包名不同，因此需要在反序列化时进行包名处理
public class OrderObjectInputStream extends ObjectInputStream {
    protected OrderObjectInputStream() throws IOException, SecurityException {
        super();
    }

    public OrderObjectInputStream(InputStream arg0) throws IOException {
        super(arg0);
    }

    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException{
       String name = desc.getName();
       try {
            if(name.startsWith("indi.pet.consumer"))
                name = name.replace("consumer", "producer");//包名转换
            return Class.forName(name);

       } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
       }

       return super.resolveClass(desc);
    }

}
//同时需要注意，两个系统下的实体序列化id必须一致！！！！
public class Order implements Serializable {

    private static final long serialVersionUID = 123456L;
```

3.发送订单&接收订单截图

![发起订单](https://github.com/Kurok1/pet-shop/blob/master/images/5.png)

![接收订单](https://github.com/Kurok1/pet-shop/blob/master/images/6.png)



下周计划任务（4-15至4-21）

- [ ] 利用WebSocket技术进行商家实时接单，并且用户和商家可以进行聊天互动
- [ ] 订单完整运行流程，从发起到结束

***

####  **本周项目完成情况 （4-15至4-21）**

consumer&productor：

1. 支持实时接收订单状态（利用websocket）
2. 完整订单流程运行 ： 发起->接单->拣货->拣货完成->用户确认取货->商家确认完成

完整订单流程图：
![订单流程](https://github.com/Kurok1/pet-shop/blob/master/images/orderstream.png)
用户订单发起&商家接收订单：
![接收订单](https://github.com/Kurok1/pet-shop/blob/master/images/10.png)
商家接单&用户提醒
![接单](https://github.com/Kurok1/pet-shop/blob/master/images/7.png)
商家拣货&用户提醒
![拣货&通知](https://github.com/Kurok1/pet-shop/blob/master/images/8.png)
订单完成
![订单完成](https://github.com/Kurok1/pet-shop/blob/master/images/9.png)



本周未完成：

- [ ] 商家和用户之间聊天功能（可能需要单独的一套聊天系统）

下周计划任务（4-22至4-26）

- [ ] 搭建聊天系统