/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

var $$=Dom7;

var host="http://localhost/";
var ORDER_BEGIN=1;
var ORDER_RECEIVED=2;
var ORDER_HANDLED=4;
var ORDER_WORKING=8;
var ORDER_FINISHED=100;
var ORDER_CANCEL=99;

function statusTextMap(status){
    var txt="";
    switch (status) {
        case ORDER_BEGIN:{txt="未接单"};break;
        case ORDER_RECEIVED:{txt="已接单"};break;
        case ORDER_HANDLED : {txt="拣货中"};break;
        case ORDER_WORKING: {txt="等待用户取货确认"};break;
        case ORDER_FINISHED: {txt="订单已完成"};break;
        case ORDER_CANCEL: {txt="订单已取消"};break;
        case 50:{txt="等待商家确认"};break;
    }
    return txt;
}

var basicHost="localhost";

var orderCurrentPage=1;
var orderHasNext=true;

function timeUtil(timestamp) {
    var date = new Date(timestamp*1000);
    var Y = date.getFullYear() + '-';
    var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
    var D = date.getDate() + ' ';
    var h = date.getHours() + ':';
    var m = date.getMinutes() + ':';
    var s = date.getSeconds();
    return Y+M+D+h+m+s;
}

var app = new Framework7({
    // App root element
    root: '#app',
    // App Name
    name: 'My App',
    //侧滑菜单栏
    panel: {
        swipe: 'left'
    },
    // App id
    id: 'com.myapp.test',
    // Enable swipe panel
    panel: {
        swipe: 'left',
    },
    // Add default routes
    routes: [
        {
            path: '/',
            url: 'index.html',
        },
        {
            name: 'about',
            path: '/about/',
            url: 'about.html',
        },
        {
            name:'chat',
            path:'/chat',
            url:'pages/chat/single.html'
        },
        {
            name:'moment',
            path:'/moment/:id',
            url:"pages/moment/detail.html",
            on:{
                pageInit:function (e,page) {
                    var messageId=page.route.params.id;
                    app.request({
                        url: host + "/message/" + messageId + "?token=" + localStorage.getItem("currentUserToken"),
                        method: "GET",
                        dataType: "json",
                        contentType: "application/json",
                        async: false,
                        success: function (data, status, xhr) {
                            if (data instanceof String)
                                data = JSON.parse(data);//转json
                            if (data.flag === true){
                                var message=data.message;
                                var date = new Date(message.timestamp);
                                var Y = date.getFullYear() + '-';
                                var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
                                var D = date.getDate() + ' ';
                                var h = date.getHours() + ':';
                                var m = date.getMinutes() + ':';
                                var s = date.getSeconds();
                                var template="<div class='card-header'>" +
                                    "<div class='demo-facebook-avatar'><img src='"+host+"/res/"+message.logo+"' width='34' height='34'/></div>" +
                                    "<div class='demo-facebook-name'>"+message.username+"</div>" +
                                    "<div class='demo-facebook-date'>"+Y+M+D+h+m+s+"</div>" +
                                    "</div>" +
                                    "<div class='card-content card-content-padding'><h5> "+message.title+"</h5>" +
                                    "<p>"+message.content+"</p>";
                                for (var i in message.imgs){
                                    var img=message.imgs[i];
                                    template+="<img src='"+host+"/res/"+img+"' width='100%'/>"
                                }

                                    template+="</div>";
                                $$('#message-body').append(template);
                                renderComments(data.comments);
                            }

                        }
                    });


                    $$('#add-root-comment').on('click',function () {
                        addComment(null,messageId,$("#root-comments ul"))
                    });

                    $$('.comments').on('click',function () {
                        var id=$$(this).attr("data-id");
                        var appendObj=$$(this).find("ul");
                        addComment(id,messageId,appendObj);
                    });

                }
            }
        },
        {
            name:'moment-add',
            path:'/moment/add',
            url:'pages/moment/add.html',
            on:{
                pageInit:function (e,page) {
                    $$('#moment-append-image').on('change',function () {
                        var imgList = asyncUploadFiles("#moment-add-image").pop();
                        for (var i in imgList){
                            var resource=imgList[i];
                            if(resource instanceof String)
                                resource=JSON.parse(resource);
                            var img=document.createElement('img');
                            img.setAttribute("width",75);
                            img.setAttribute("src",host+"/res/"+resource.id);
                            img.setAttribute("data-id",resource.id);
                            img.className="moment-imgs"
                            document.querySelector("#moment-append-imgs").appendChild(img)
                        }
                        $$('.moment-imgs').on('click',function () {
                            var id=$$(this).attr("data-id");
                            app.dialog.confirm("确定要删除吗？", function(){
                                app.request({
                                        url: host + "/res/"+id+"?token=" + localStorage.getItem("currentUserToken"),
                                        method: "DELETE",
                                        dataType: "json",
                                        contentType: "application/json",
                                        async: false,
                                        success: function (data, status, xhr) {
                                            if (data instanceof String)
                                                data = JSON.parse(data);//转json
                                            if(data.flag===true){
                                                $$("#moment-append-imgs").find('img[data-id='+id+']').remove();
                                            }
                                        }
                                    }
                                )

                            }, function () {

                            })
                        })
                    });
                    
                    $$('#moment-publish-btn').on('click',function () {
                        var data;
                        var title=$$("#moment-add-title").val();
                        var content=$$("#moment-add-content").html();
                        var list=[];
                        $$("#moment-append-imgs img").each(function (index,ele) {
                            list.push($$("#moment-append-imgs img").eq(index).attr("data-id"));
                        });
                        user=JSON.parse(localStorage.getItem("currentUser"));
                        data={
                            user:user.id,
                            imgs:list,
                            title:title,
                            content:content
                        };
                        app.request({url:host+"/message/publish?token="+localStorage.getItem("currentUserToken"),
                            data:JSON.stringify(data),
                            processData:false,
                            method:"POST",
                            dataType:"json",
                            contentType:"application/json",
                            async:false,
                            success:function(data, status, xhr){
                                app.dialog.close();
                                if(data instanceof String)
                                    data=JSON.parse(data);//转json
                                app.dialog.alert(data.message);
                                mainView.router.back();
                            }}
                        )
                    })
                }
            }
        },
        {
            name: 'orders',
            path: '/orders',
            url: 'pages/order/list.html',
            on: {
                pageInit:function (e,page) {
                    $$('#order-list').find("li").remove();
                    renderOrders()

                    $$('#loadMoreOrder').on('click',function () {
                        if(orderHasNext===false){
                            app.dialog.alert("没有更多数据了");
                        }else {
                            app.dialog.preloader('加载中');
                            orderCurrentPage++;
                            renderOrders();
                            app.dialog.close();
                        }

                    })
                },
                pageReinit:function () {
                    orderHasNext=true;
                    orderCurrentPage=1;
                    $$('#order-list').find("li").remove();
                    renderOrders();
                }
            }
        },
        {
            name:'order',
            path:'/order/:id',
            url:'pages/order/detail.html',
            on:{
                pageInit:function (e,page) {
                    var id = page.route.params.id;
                    token=localStorage.getItem("currentUserToken");
                    app.request({url:host+"/order/"+id+"?token="+token,
                        processData:false,
                        method:"GET",
                        dataType:"json",
                        contentType:"application/json",
                        async:false,
                        success:function(data, status, xhr){
                            app.dialog.close();
                            if(data instanceof String)
                                data=JSON.parse(data);//转json
                            var timestamp=data.createTimeStamp;
                            var id=data.id;
                            var keeperId=data.shopkeeper.id;
                            var amount=data.amount;
                            var status=data.status;
                            var shockName=data.shock.title;
                            var price=data.shock.price;

                            var currentStatusText=statusTextMap(status);

                            $$('#order-detail-id').html(id);
                            $$('#order-detail-shock').html(shockName);
                            $$('#order-detail-price').html("￥ "+price);
                            $$('#order-detail-amount').html(amount);
                            $$('#order-detail-keeper').html('<a href="javascript:" data-keeper="'+keeperId+'">点击联系商家</a>');
                            $$('#order-detail-createTime').html(timeUtil(timestamp));
                            $$('#order-detail-status').html(currentStatusText);
                            if(status===ORDER_WORKING){
                                $$('#order-status-confirm').attr("data-order",id);
                                $$('#order-status-confirm').show()
                            }else{
                                $$('#order-status-confirm').hide()
                            }
                        }
                    }
                        
                        
                    );
                    $$('#order-status-confirm').on('click',function () {
                        var id=$$('#order-status-confirm').attr("data-order");
                        app.dialog.confirm("确定执行操作吗？", function(){
                            app.request({
                                    url: host + "/order/confirm/"+id+"?token=" + localStorage.getItem("currentUserToken"),
                                    method: "PUT",
                                    dataType: "json",
                                    contentType: "application/json",
                                    async: false,
                                    success: function (data, status, xhr) {
                                        if (data instanceof String)
                                            data = JSON.parse(data);//转json
                                        app.dialog.alert(data.message);
                                        if(data.flag===true){
                                            mainView.router.back();
                                        }
                                    }
                                }
                            )
                        }, function () {
                        });
                    })
                }
            }
            
        },
        {
            name: 'user',
            path: '/profile',
            url: 'pages/user.html',
            on: {
                pageInit: function (e, page) {
                    var user = localStorage.getItem("currentUser");
                    user = JSON.parse(user);
                    // 渲染数据
                    document.querySelector("#profile_logo_img").src=host + '/res/' +user.logo;
                    document.querySelector("#profile_user_name").value=user.username;
                        /**
                     * 修改用户头像绑定事件
                     */
                    $$('#profile_logo').on('change', function () {
                        var imgList = asyncUploadFiles("#profile_logo_form");
                        if (imgList.length !== 0) {
                            var resource=imgList.pop()[0];
                            if(resource instanceof String)
                                resource = JSON.parse(resource);
                            document.querySelector("#profile_logo_img").src=host + '/res/' + resource.id;
                            document.querySelector("#panel_user_logo").src=host + '/res/' + resource.id;
                            user.logo =  resource.id;
                            localStorage.setItem("currentUser",JSON.stringify(user))
                        }
                        document.querySelector("#profile_logo").value="";
                    });

                    /**
                     * 用户修改事件绑定
                     */
                    $$('#update-user-btn').on('click',function () {
                        //获取用户名
                        var newUserName=$$("#profile_user_name").val();
                        user.username=newUserName;
                        var token=localStorage.getItem("currentUserToken");
                        app.request({url:host+"/user/update?token="+token,
                            data:JSON.stringify(user),
                            processData:false,
                            method:"PUT",
                            dataType:"json",
                            contentType:"application/json",
                            async:false,
                            success:function(data, status, xhr){
                                app.dialog.close();
                                if(data instanceof String)
                                    data=JSON.parse(data);//转json
                                localStorage.setItem("currentUser",JSON.stringify(data));
                                afterLogin(data);
                                app.dialog.alert("修改完成");
                            }}
                        )
                    })
                },
            },
        },
        {
            name:'friends',
            path:'/friends',
            url:'pages/friends.html'
        },
        {
            name:'shock-detail',
            path:'/shock/detail/:id',
            url:'pages/shock/detail.html',
            on:{
                pageInit:function (e,page) {
                    var id = page.route.params.id;
                    app.request({
                            url: host + "/shock/" + id + "?token=" + localStorage.getItem("currentUserToken"),
                            processData: false,
                            method: "GET",
                            dataType: "json",
                            contentType: "application/json",
                            async: false,
                            success: function (data, status, xhr) {
                                app.dialog.close();
                                if (data instanceof String)
                                    data = JSON.parse(data);//转json
                                var shock = data.shock;
                                $$('#shock-title').html(shock.title);
                                $$('#shock-shopkeeper').val(shock.shopkeeperId);
                                $$('#shock_price').html("￥" + shock.price);
                                $$('#shock-price-total').html("共计：￥"+shock.price);
                                $$('#shock-price').val(shock.price);
                                $$('#shock-amount').attr("max", shock.last);
                                $$('#shock-text').html(shock.text);
                                var date = new Date(shock.timestamp);
                                var Y = date.getFullYear() + '-';
                                var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
                                var D = date.getDate() + ' ';
                                var h = date.getHours() + ':';
                                var m = date.getMinutes() + ':';
                                var s = date.getSeconds();
                                $$('#shock-time').html(Y + M + D + h + m + s);
                                for (var i in shock.resources) {
                                    var resource = shock.resources[i];
                                    var template = "<div class='swiper-slide'><img src='" + host + "/res/" + resource + "' alt='' height='400'></div>";
                                    $$('#shock-slider').append(template);
                                }
                            }
                        }
                    );
                    $$('#shock-amount').on('change',function () {
                       var amount= $$('#shock-amount').val();
                       var total=$$('#shock-price').val()*amount;
                       $$('#shock-price-total').html("共计：￥"+total);
                    });

                    $$('#order-publish').on('click',function () {
                       //collect data
                        var order={};
                        var user=JSON.parse(localStorage.getItem("currentUser"));
                        order.userId=user.id;
                        order.shopkeeperId=$$('#shock-shopkeeper').val();
                        order.shockId=id;
                        order.count=$$('#shock-amount').val();
                        app.dialog.preloader("正在提交订单");
                        app.request({url:host+"/order/create?token="+localStorage.getItem("currentUserToken"),
                            data:JSON.stringify(order),
                            processData:false,
                            method:"POST",
                            dataType:"json",
                            contentType:"application/json",
                            async:false,
                            success:function(data, status, xhr){
                                if(data instanceof String)
                                    data=JSON.parse(data);//转json
                                if(data.flag===true){
                                    setTimeout(function () {
                                        app.dialog.close();
                                        app.dialog.alert("订单提交成功~");
                                    },1000)
                                }
                            }}
                        );
                    });
                }
            }
        }
    ],
    // ... other parameters
});


function loadMessage(page) {
    var rtn={};
    app.request({url:host+"/message/get?token="+localStorage.getItem("currentUserToken")+"&currentPage="+page,
        processData:false,
        method:"GET",
        dataType:"json",
        contentType:"application/json",
        async:false,
        success:function(data, status, xhr){
            app.dialog.close();
            if(data instanceof String)
                data=JSON.parse(data);//转json
            if(data.flag===true){
                rtn.data=data.data;
                rtn.hasNext=data.hasNext;
            }

        }}
    );
    return rtn;
}

function asyncUploadFiles(ele){
    var formEle=document.querySelector(ele);
    var formData=new FormData(formEle);
    var list=[];
    jQuery.ajax({
        url: host+"/res/upload",
        type: "POST",
        data: formData,
        processData: false,
        contentType: false,
        cache: false,
        async: false,
        complete: function (data, status, xhr) {
            app.dialog.close();
            data=data.responseJSON;
            if (data instanceof String)
                data = JSON.parse(data);
            if (data.flag == true) {
                list.push(data.data);
            }
            app.dialog.alert(data.message);
        }
    });
    return list;
}

function addComment(root,messageId,ele) {//root为父级评论id
    app.dialog.prompt("说出你想说的",function (value) {
        var data={};
        data.userId=JSON.parse(localStorage.getItem("currentUser")).id;
        data.message=messageId;
        data.content=value;
        data.parent=root;
        app.request({url:host+"/comment/publish?token="+localStorage.getItem("currentUserToken"),
            data:JSON.stringify(data),
            processData:false,
            method:"POST",
            dataType:"json",
            contentType:"application/json",
            async:false,
            success:function(data, status, xhr){
                app.dialog.close();
                if(data instanceof String)
                    data=JSON.parse(data);//转json
                if(data.flag===true){
                    var data=data.data;
                    var html="<li "+(root==null?"class='comments' data-id='"+data.id+"'":"")+">" +
                        "<a href='javascript:' class='item-content "+(root==null?"item-link":"")+"'>" +
                        "<div class='item-media'><img src='"+host+"/res/"+data.logo+"' width='34'/></div>" +
                        "<div class='item-inner'>" +
                        "<div class='item-title'>"+data.content+"</div>" +
                        "</div>" +
                        "</a>";
                    if(root!=null)
                        html+="</li>";
                    else
                        html+="<ul></ul></li>";
                    ele.append(html);
                }
                app.dialog.alert(data.message);
            }}
        );
    },function (value) {
        
    },"")
}

function renderComments(data) {
    var dom="";
    for (var index in data){
        var parent=data[index];
        var template="<li class='comments' data-id='"+parent.id+"'>" +
            "<a href='javascript:' class='item-link item-content'>" +
            "<div class='item-media'><img src='"+host+"/res/"+parent.logo+"' width='34'/></div>" +
            "<div class='item-inner'>" +
            "<div class='item-title'>"+parent.content+"</div>" +
            "</div>" +
            "</a>" +
            "<ul>";
        for (var j in parent.children) {
            var ch=parent.children[j];
            var html="<li>" +
                "<a href='javascript:' class='item-content'>" +
                "<div class='item-media'><img src='"+host+"/res/"+ch.logo+"' width='34'/></div>" +
                "<div class='item-inner'>" +
                "<div class='item-title'>"+ch.content+"</div>" +
                "</div>" +
                "</a>" +
                "</li>"
            template+=html;
        }
        template+="</ul></li>";
        dom+=template;
    }
    $$('#root-comments ul').append(dom)
}

function registerWebSocket(id) {
    var ws=new WebSocket("ws://"+basicHost+"/user/order/"+id);

    ws.onopen=function (event) {
        console.log(">>>>>>>>>>>>>>>>>>>>>>");
        console.log(id+": 连接WebSocket成功");
        console.log("<<<<<<<<<<<<<<<<<<<<<<");
    };

    ws.onmessage=function (message) {
        // Create full-layout notification
        var notificationFull = app.notification.create({
            title: '您有新的消息',
            titleRightText: 'now',
            text: message.data,
            closeTimeout: 3000,
        });
        notificationFull.open()
    };
}

function afterLogin(user) {
    document.querySelector("#panel_user_logo").src=host + '/res/' + user.logo;
    document.querySelector("#panel_user_name").innerHTML=user.username;
    registerWebSocket(user.id);
}

var mainView = app.views.create('.view-main');

$$('.moment-card').on('click',function () {
    mainView.router.navigate({
        name:'moment'
    })
});

var dialog=app.dialog;

if(!localStorage.hasOwnProperty("currentUserToken"))
    app.loginScreen.open("#login",true);
else{
    app.request({url:host+"/user/login?token="+localStorage.getItem("currentUserToken"),
                            method:"PUT",
                            success(data, status, xhr) {
                                app.dialog.close();
                                data=JSON.parse(data);
                                if(data.flag==false)
                                    app.dialog.alert(data.message,"请重新登录",function () {
                                        app.loginScreen.open("#login",true)
                                    });
                                else {
                                    localStorage.setItem("currentUserToken",data.token);
                                    localStorage.setItem("currentUser",JSON.stringify(data.user));
                                    afterLogin(data.user)
                                }
                            }})
}


$$('#login #login-btn').on('click', function () {
    var username = $$('#login [name="username"]').val();
    var password = $$('#login [name="password"]').val();

    app.request({url:host+"/user/login?username="+username+"&password="+password,
                            method:"POST",
                            success:function(data, status, xhr){
                                app.dialog.close();
                                data=JSON.parse(data);//转json
                                if(data.flag==false)
                                    app.dialog.alert(data.message==null?"用户名和密码不正确，请重试":data.message);
                                else{
                                    localStorage.setItem("currentUserToken",data.token);
                                    localStorage.setItem("currentUser",JSON.stringify(data.user));
                                    // Close login screen
                                    app.loginScreen.close('#login',true);
                                    afterLogin(data.user)
                                }

                            }
                            })



});


$$('#register #register-btn').on('click',function () {
    var username = $$('#register [name="username"]').val();
    var password = $$('#register [name="password"]').val();
    var repassword=$$('#register [name="re-password"]').val();

    if(repassword!=password){
        $$('#register [name="password"]').val("");
        $$('#register [name="re-password"]').val("")
        app.dialog.alert("两次密码不一致,请重新输入!!!");

        return;
    }
    if (username==null || ""==username){
        $$('#register [name="username"]').val("");
        $$('#register [name="password"]').val("");
        $$('#register [name="repassword"]').val("");
        app.dialog.alert("用户名不能为空,请重新输入");
        return
    }


    app.request({url:host+"/user/register",
        data:JSON.stringify({
            "username":username,
            "password":password,
            "logo":"",
            "friends":[],
            "pets":[],
            "shopkeepers":[]
        }),
        processData:false,
        method:"POST",
        dataType:"json",
        contentType:"application/json",
        success:function(data, status, xhr){
            // data=JSON.parse(data);//转json
            app.dialog.close();
            if(data.flag==false){
                app.dialog.alert(data.message);
                $$('#register [name="username"]').val("");
            }
            else {
                localStorage.setItem("currentUserToken",data.token);//存储token
                localStorage.setItem("currentUser",JSON.stringify(data.user));
                afterLogin(data.user)
                app.loginScreen.close("#register",true);
                app.loginScreen.close('#login',true);
            }
        }}
    )

});

var iconTooltip = app.tooltip.create({
    targetEl: '#loadMoreMoment',
    text: '加载更多',
});

var currentMessagePage=1;//当前为第一页
var hasNext=true;
function renderMessage() {
    var messages=loadMessage(currentMessagePage);
    hasNext=messages.hasNext;
    var messageData=messages.data;

    for (var i in messageData){
        var message=messageData[i];
        var date = new Date(message.timestamp);
        var Y = date.getFullYear() + '-';
        var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
        var D = date.getDate() + ' ';
        var h = date.getHours() + ':';
        var m = date.getMinutes() + ':';
        var s = date.getSeconds();
        var html="<div class='card moment-card' data-id='"+message.id+"'>" +
            "<div class='card-header'>" +
            "<div class='demo-facebook-avatar'><img src='"+host+"/res/"+message.logo+"' width='34' height='34'/></div>" +
            "<div class='demo-facebook-name'>"+message.username+"</div>" +
            "<div class='demo-facebook-date'>"+Y+M+D+h+m+s+"</div>" +
            "</div>" +
            "<div class='card-content card-content-padding'><p>"+message.title+"</p><p>"+message.content;
        if(message.imgs.length===0)
            html+="</p></div></div>";
        else
            html+="<img src='"+host+"/res/"+message.imgs[0]+"' alt='' width='100%'></p></div></div>";
        $$('#moments').append(html);
    }
}

renderMessage();

$$('#loadMoreMoment').on('click',function () {
    if(hasNext===false){
        app.dialog.alert("没有更多数据了");
    }else {
        app.dialog.preloader('加载中');
        currentMessagePage++;
        renderMessage();
        app.dialog.close();
    }
});

$$('.moment-card').on('click',function () {
    var id=$$(this).attr("data-id");
    mainView.router.navigate({
        name: 'moment',
        params: { id:id },
    });
});

var orderList=[];

function loadMoreOrder() {
    var token=localStorage.getItem("currentUserToken");
    app.request({url:host+"/order/list/"+orderCurrentPage+"?token="+token,
        processData:false,
        method:"GET",
        dataType:"json",
        contentType:"application/json",
        async:false,
        success:function(data, status, xhr){
            app.dialog.close();
            if(data instanceof  String)
                data=JSON.parse(data);
            orderHasNext=data.hasNext;
            orderList=data.data;
        }}
    );
}

function renderOrders(){
    loadMoreOrder()
    var list=orderList;
    for (var i in list){
        var order=list[i];
        var template="<li>" +
            "<a href='/order/"+order.id+"' data-order='"+order.id+"' class='item-link item-content'>" +
            "<div class='item-media'><img src='"+host+"/res/"+order.shock.logo+"' width='80'/></div>" +
            "<div class='item-inner'>" +
            "<div class='item-title-row'>" +
            "<div class='item-title'>店铺名称："+order.shopkeeper.name+"</div>" +
            "</div>" +
            "<div class='item-subtitle'>商品名称："+order.shock.title+"</div>" +
            "<div class='item-text'>当前状态 &nbsp;&nbsp;：&nbsp;&nbsp; "+statusTextMap(order.status)+"</div>" +
            "</div></a></li>";
        $$('#order-list').append(template);
    }
}