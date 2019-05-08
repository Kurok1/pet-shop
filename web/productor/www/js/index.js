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
// import Framework7 from './js/framework7.min.js';

var $$ = Dom7;


var ORDER_BEGIN = 1;
var ORDER_RECEIVED = 2;
var ORDER_HANDLED = 4;
var ORDER_WORKING = 8;
var ORDER_FINISHED = 100;
var ORDER_CANCEL = 99;

var host = "http://localhost/producer";
var basicHost = "127.0.0.1";
var basicPort = 80;
var chatHost = "localhost";
var chatPort = 9090;
var currentShocksPage = 1;//当前为第一页
var hasNext = true;

orderHasNext = true;
orderCurrentPage = 1;

var keeperSocket = null;
var connectionType = 2;

function timeUtil(timestamp) {
    var date = new Date(timestamp * 1000);
    var Y = date.getFullYear() + '-';
    var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
    var D = date.getDate() + ' ';
    var h = date.getHours() + ':';
    var m = date.getMinutes() + ':';
    var s = date.getSeconds();
    return Y + M + D + h + m + s;
}


function initUserConnection(keeper) {
    keeperSocket = new WebSocket("ws://" + chatHost + ":" + chatPort + "/socket/" + connectionType + "/" + keeper.id);
    keeperSocket.onopen = function (event) {
        console.log("<<<<<<<<<");
        console.log(keeper.name + "已经连接成功");
        console.log(">>>>>>>>>");
    };

}


function registerChat(toId) {
    var keeper = JSON.parse(localStorage.getItem("currentKeeper"));
    app.request({
            url: "http://"+chatHost + ":" + chatPort + "/chat/3/" + keeper.id + "/" + toId,
            processData: false,
            method: "POST",
            dataType: "json",
            contentType: "application/json",
            async: false,
            success: function (data, status, xhr) {
                if (data instanceof String)
                    data = JSON.parse(data);

                if (data.flag) {
                    mainView.router.navigate({
                        name: 'chat',
                        params: {id: toId, type: 1},
                    });
                }else
                    app.dialog.alert(data.message);
            }
        }
    );
}

var app = new Framework7({
    // App root element
    root: '#app',
    // App Name
    name: 'Productor App',
    //侧滑菜单栏
    panel: {
        swipe: 'left'
    },
    theme: 'auto',
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
            name: "main",
            url: 'index.html'
        },
        {
            name: "add-shock",
            path: '/add-shock',
            url: 'page/shock/add.html',
            on: {
                pageInit: function (e, page) {
                    $$('#shock-add-logo').on('change', function () {
                        var imgList = asyncUploadFiles("#shock_logo_form");
                        if (imgList.length !== 0) {
                            var resource = imgList.pop()[0];
                            if (resource instanceof String)
                                resource = JSON.parse(resource);
                            document.querySelector("#shock-logo").src = host + '/res/' + resource.id;
                            $$("#shock-logo-value").val(resource.id);
                        }
                        document.querySelector("#shock-add-logo").value = "";
                    });
                    $$("#shock-add-imgs").on('change', function () {
                        var imgList = asyncUploadFiles("#shock_imgs_form").pop();
                        if (imgList.length !== 0) {
                            for (var i in imgList) {
                                var resource = imgList[i];
                                if (resource instanceof String)
                                    resource = JSON.parse(resource);
                                var template = "<img src='" + host + '/res/' + resource.id + "' data-id='" + resource.id + "' style='line-height: 50px' width='50' height='50' >"
                                $$('#shock-add-imgs-list').append(template);
                            }
                        }
                    });
                    $$('#shock-publish').on('click', function () {
                        //收集数据
                        var keeper = JSON.parse(localStorage.getItem("currentKeeper"));
                        var data = {};
                        data.shopkeeperId = keeper.id;
                        data.last = $$("#shock-amount").val();
                        data.price = $$("#shock-price").val();
                        data.text = $$('#shock-description').html();
                        data.logo = $$('#shock-logo-value').val();
                        var imgs = [];
                        $$('#shock-add-imgs-list img').forEach(function (item) {
                            imgs.push(item.getAttribute("data-id"));
                        });
                        data.resources = imgs;
                        data.title = $$("#shock-title").val();
                        app.request({
                                url: host + "/shock/save?token=" + localStorage.getItem("currentKeeperToken"),
                                data: JSON.stringify(data),
                                processData: false,
                                method: "POST",
                                dataType: "json",
                                contentType: "application/json",
                                async: false,
                                success: function (data, status, xhr) {
                                    app.dialog.close();
                                    if (data instanceof String)
                                        data = JSON.parse(data);//转json
                                    app.dialog.alert(data.message);
                                    if (data.flag === true) {
                                        currentShocksPage++;
                                        $$('#shocks').find(".toManage").remove();
                                        hasNext = true;
                                        renderShocks();

                                        mainView.router.back();
                                    }

                                }
                            }
                        )
                    });
                }
            }
        },
        {
            name: "manage-shock",
            path: '/manage-shock/:id',
            url: 'page/shock/manage.html',
            on: {
                pageInit: function (e, page) {
                    var shockId = page.route.params.id;
                    renderSingleShock(shockId);

                    $$('#shock-update-logo').on('change', function () {
                        var imgList = asyncUploadFiles("#shock-update-logo-form");
                        if (imgList.length !== 0) {
                            var resource = imgList.pop()[0];
                            if (resource instanceof String)
                                resource = JSON.parse(resource);
                            document.querySelector("#shock-update-logo-img").src = host + '/res/' + resource.id;
                            $$("#shock-update-logo-value").val(resource.id);
                        }
                        document.querySelector("#shock-update-logo").value = "";
                    });
                    $$("#shock-update-imgs-list img").on('click', function () {
                        var id = $(this).attr("data-id");
                        app.dialog.confirm("确定要删除吗？", function () {
                            app.request({
                                    url: host + "/res/" + id + "?token=" + localStorage.getItem("currentKeeperToken"),
                                    method: "DELETE",
                                    dataType: "json",
                                    contentType: "application/json",
                                    async: false,
                                    success: function (data, status, xhr) {
                                        if (data instanceof String)
                                            data = JSON.parse(data);//转json
                                        if (data.flag === true) {
                                            $$("#shock-update-imgs-list").find('img[data-id=' + id + ']').remove();
                                        }
                                    }
                                }
                            )
                        }, function () {
                        });
                    });
                    $$("#shock-update-imgs").on('change', function () {
                        var imgList = asyncUploadFiles("#shock-update-imgs-form").pop();
                        if (imgList.length !== 0) {
                            for (var i in imgList) {
                                var resource = imgList[i];
                                if (resource instanceof String)
                                    resource = JSON.parse(resource);
                                var template = "<img src='" + host + '/res/' + resource.id + "' data-id='" + resource.id + "' style='line-height: 50px' width='50' height='50' >";
                                $$('#shock-update-imgs-list').append(template);
                            }
                        }
                        $$("#shock-update-imgs-list img").on('click', function () {
                            var id = $(this).attr("data-id");
                            app.dialog.confirm("确定要删除吗？", function () {
                                app.request({
                                        url: host + "/res/" + id + "?token=" + localStorage.getItem("currentKeeperToken"),
                                        method: "DELETE",
                                        dataType: "json",
                                        contentType: "application/json",
                                        async: false,
                                        success: function (data, status, xhr) {
                                            if (data instanceof String)
                                                data = JSON.parse(data);//转json
                                            if (data.flag === true) {
                                                $$("#shock-update-imgs-list").find('img[data-id=' + id + ']').remove();
                                            }
                                        }
                                    }
                                )
                            }, function () {
                            });
                        });
                    });

                    $$('#shock-update-btn').on('click', function () {
                        //收集数据
                        var keeper = JSON.parse(localStorage.getItem("currentKeeper"));
                        var data = {};
                        data.id = shockId;
                        data.shopkeeperId = keeper.id;
                        data.last = $$("#shock-update-amount").val();
                        data.price = $$("#shock-update-price").val();
                        data.text = $$('#shock-update-description').html();
                        data.logo = $$('#shock-update-logo-value').val();
                        var imgs = [];
                        $$('#shock-update-imgs-list img').forEach(function (item) {
                            imgs.push(item.getAttribute("data-id"));
                        });
                        data.resources = imgs;
                        data.title = $$("#shock-update-title").val();
                        app.request({
                                url: host + "/shock/update?token=" + localStorage.getItem("currentKeeperToken"),
                                data: JSON.stringify(data),
                                processData: false,
                                method: "PUT",
                                dataType: "json",
                                contentType: "application/json",
                                async: false,
                                beforeSend: function () {
                                    app.dialog.preloader('请稍等');
                                },
                                success: function (data, status, xhr) {
                                    app.dialog.close();
                                    if (data instanceof String)
                                        data = JSON.parse(data);//转json
                                    if (data.flag === true)
                                        mainView.router.navigate({
                                            name: "main"
                                        });
                                    app.dialog.alert(data.message);

                                }
                            }
                        )
                    });

                }
            }
        },
        {
            name: 'chats',
            path: '/chats',
            url: 'page/chats/list.html',
            on:{
                pageInit: function (e,page) {
                    var keeper = JSON.parse(localStorage.getItem("currentKeeper"));
                    app.request({
                        url: "http://"+chatHost+":"+chatPort+"/list/"+connectionType+"/"+keeper.id,
                        processData: false,
                        method: "GET",
                        dataType: "json",
                        contentType: "application/json",
                        async: false,
                        success: function(data ,status ,xhr){
                            if (data instanceof String)
                                data = JSON.parse(data);
                            $$('#chats-list').find("li").remove();
                            for (var i in data){
                                var item = data[i];
                                var template="<li>" +
                                    "<a href='/chat/"+item.type+"/"+item.id+"' class='item-link item-content to-chat-detail'>" +
                                    "<div class='item-media'><img src='"+host+"/res/"+item.logo+"' width='44'/></div>" +
                                    "<div class='item-inner'>" +
                                    "<div class='item-title-row'>" +
                                    "<div class='item-title'>"+item.name+"</div>" +
                                    "</div>" +
                                    "<div class='item-subtitle'>"+item.timestamp+"</div>" +
                                    "</div>" +
                                    "</a>" +
                                    "</li>";
                                $$('#chats-list').append(template);
                            }
                        }
                    })

                }
            }
        },
        {
            name: 'chat',
            path: '/chat/:type/:id',
            url: 'page/chats/single.html',
            on: { //需要隐藏下部toolbar
                pageAfterIn: function test(e, page) {
                    app.toolbar.hide(".toolbar", true);
                },
                pageInit: function (e, page) {
                    app.toolbar.hide(".toolbar", true);
                    var type = page.route.params.type;
                    var id = page.route.params.id;
                    var flag = false;
                    var name = "";
                    var logo = "";


                    app.request({
                            url: "http://"+chatHost + ":" + chatPort + "/exist/" + type + "/" + id,
                            processData: false,
                            method: "GET",
                            dataType: "json",
                            contentType: "application/json",
                            async: false,
                            success: function (data, status, xhr) {
                                if (data instanceof String)
                                    data = JSON.parse(data);
                                flag = data.flag;
                                if (!data.flag) {
                                    app.dialog.alert(data.message);
                                    mainView.router.back();
                                }

                            }
                        }
                    );


                    if (flag) {//获取聊天对象信息
                        app.request({
                                url: "http://"+chatHost + ":" + chatPort + "/info/" + type + "/" + id,
                                processData: false,
                                method: "GET",
                                dataType: "json",
                                contentType: "application/json",
                                async: false,
                                success: function (data, status, xhr) {
                                    if (data instanceof String)
                                        data = JSON.parse(data);
                                    if (data.flag) {
                                        id = data.info.id;
                                        name = data.info.name;
                                        logo = data.info.logo;
                                        type = data.info.type;
                                        afterGetInfo();
                                    }else {
                                        app.dialog.alert("系统错误");
                                        mainView.router.back();
                                    }

                                }
                            }
                        );
                    }else return;
                    var messageRecords = $$('#message-record');
                    //消息接受事件
                    keeperSocket.onmessage=function(event){
                        var data = JSON.parse(event.data);
                        if(data.messageType===3){//文字消息
                            var logoUrl = host+"/res/"+logo;
                            var template="<div class='message message-received'>" +
                                "<div class='message-avatar' style='background-image:url("+logoUrl+");'></div>" +
                            "<div class='message-content'>" +
                            "<div class='message-name'>"+name+"</div>" +
                            "<div class='message-bubble'>" +
                            "<div class='message-text'>"+data.content+"</div>" +
                            "</div>" +
                            "</div>" +
                            "</div>";
                            messageRecords.append(template);
                        }else if(data.messageType===4){
                            var logoUrl = host+"/res/"+logo;
                            var imgUrl = host + '/res/' + data.resource
                            var template="<div class='message message-received'>" +
                                "<div class='message-avatar' style='background-image:url("+logoUrl+");'></div>" +
                                "<div class='message-content'>" +
                                "<div class='message-bubble'>" +
                                "<div class='message-image'><img src='"+ imgUrl +"' style='max-width:200px;'></div>" +
                                "</div>" +
                                "</div>" +
                                "</div>";
                            messageRecords.append(template);
                        }else if(data.leaved){
                            var notifyLeaved = app.notification.create({
                                title: '对方已下线',
                                titleRightText: 'now',
                                text: name+" 已经下线",
                                closeTimeout: 3000,
                            });
                            notifyLeaved.open();
                        }
                    };

                    function afterGetInfo() {
                        $$('#name-title').html(name);
                        $$('#send-chat-message').on('click',function () {
                           var content = $$('#need-send-message').val();
                           if(content == null || content === "")
                               app.dialog.alert("消息不能为空！");
                           else{
                               var message={};
                               var keeper=JSON.parse(localStorage.getItem("currentKeeper"));
                               message.content=content;
                               message.sender=keeper.id;
                               message.senderType=2;
                               message.receiver=id;
                               message.receiverType=type;
                               message.messageType=3;//文字
                               message.resource="";
                               keeperSocket.send(JSON.stringify(message));
                               //消息展示
                               var template="<div class='message message-sent'>" +
                                   "<div class='message-content'>" +
                                   "<div class='message-bubble'>" +
                                   "<div class='message-text'>"+content+"</div>" +
                                   "</div>" +
                                   "</div>" +
                                   "</div>";
                               $$('#need-send-message').val("");
                               $$('#message-record').append(template);
                           }

                        });

                        $$('#chat-add-img').on('change',function(){
                            var imgList = asyncUploadFiles("#chat_img_form");
                            if (imgList.length !== 0) {
                                var resource = imgList.pop()[0];
                                if (resource instanceof String)
                                    resource = JSON.parse(resource);
                                var message={};
                                var keeper=JSON.parse(localStorage.getItem("currentKeeper"));
                                message.content="";
                                message.sender=keeper.id;
                                message.senderType=2;
                                message.receiver=id;
                                message.receiverType=type;
                                message.messageType=4;//图片
                                message.resource=resource.id;
                                keeperSocket.send(JSON.stringify(message));
                                var template="<div class='message message-sent'>" +
                                    "<div class='message-content'>" +
                                    "<div class='message-bubble'>" +
                                    "<div class='message-image'><img src='"+ host + '/res/' + resource.id +"' style='max-width:200px;'></div>" +
                                    "</div>" +
                                    "</div>" +
                                    "</div>";
                                $$('#message-record').append(template);
                            }
                            document.querySelector("#chat-add-img").value = "";
                        })
                    }

                },
            }
        },
        {
            name: 'orders',
            path: "/orders",
            url: 'page/order/list.html',
            on: {
                pageInit: function (e, page) {
                    $$('#filter-status').on('change', function () {
                        orderCurrentPage = 1;
                        orderHasNext = true;
                        token = localStorage.getItem("currentKeeperToken");
                        var status = $$(this).val();
                        var list = onChangeStatus(status, 1);
                        $$('#order-list').find("li").remove();
                        for (var i in list.data) {
                            var order = list.data[i];
                            var template = "<li>" +
                                "<a href='/order/" + order.id + "' class='item-link item-content'>" +
                                "<div class='item-media'><img src='" + host + "/res/" + order.shock.logo + "' width='80'/></div>" +
                                "<div class='item-inner'>" +
                                "<div class='item-title-row'>" +
                                "<div class='item-title'>订单号：" + order.id + "</div>" +
                                "</div>" +
                                "<div class='item-subtitle'>商品名称：" + order.shock.title + "</div>" +
                                "<div class='item-text'>创建时间 &nbsp;&nbsp;：&nbsp;&nbsp; " + timeUtil(order.createTimeStamp) + "</div>" +
                                "</div></a></li>";
                            $$('#order-list').append(template);
                        }
                    });

                    $$('#loadMoreOrder').on('click', function () {
                        if (orderHasNext === false) {
                            app.dialog.alert("没有更多数据了");
                        } else {
                            app.dialog.preloader('加载中');
                            orderCurrentPage++;
                            onChangeStatus($$('#filter-status').val(), orderCurrentPage);
                            app.dialog.close();
                        }

                    })
                },
                pageReinit: function (e, page) {
                    orderCurrentPage = 1;
                    orderHasNext = true;
                    token = localStorage.getItem("currentKeeperToken");
                    var status = $$('#filter-status').val();
                    var list = onChangeStatus(status, 1);
                    $$('#order-list').find("li").remove();
                    for (var i in list.data) {
                        var order = list.data[i];
                        var template = "<li>" +
                            "<a href='/order/" + order.id + "' class='item-link item-content'>" +
                            "<div class='item-media'><img src='" + host + "/res/" + order.shock.logo + "' width='80'/></div>" +
                            "<div class='item-inner'>" +
                            "<div class='item-title-row'>" +
                            "<div class='item-title'>订单号：" + order.id + "</div>" +
                            "</div>" +
                            "<div class='item-subtitle'>商品名称：" + order.shock.title + "</div>" +
                            "<div class='item-text'>创建时间 &nbsp;&nbsp;：&nbsp;&nbsp; " + timeUtil(order.createTimeStamp) + "</div>" +
                            "</div></a></li>";
                        $$('#order-list').append(template);
                    }
                }
            }
        },
        {
            name: 'order',
            path: "/order/:id",
            url: 'page/order/detail.html',
            on: {
                pageInit: function (e, page) {
                    var id = page.route.params.id;
                    token = localStorage.getItem("currentKeeperToken");
                    app.request({
                            url: host + "/order/" + id + "?token=" + token,
                            processData: false,
                            method: "GET",
                            dataType: "json",
                            contentType: "application/json",
                            async: false,
                            success: function (data, status, xhr) {
                                app.dialog.close();
                                if (data instanceof String)
                                    data = JSON.parse(data);//转json
                                var timestamp = data.createTimeStamp;
                                var id = data.id;
                                var userId = data.user.id;
                                var amount = data.amount;
                                var status = data.status;
                                var shockName = data.shock.title;
                                var price = data.shock.price;

                                var nextStatusText = getStatusTextMap(status);
                                var currentStatusText = statusTextMap(status);

                                $$('#order-detail-id').html(id);
                                $$('#order-detail-shock').html(shockName);
                                $$('#order-detail-price').html("￥ " + price);
                                $$('#order-detail-amount').html(amount);
                                $$('#order-detail-user').html('<a href="javascript:" id="toChat" data-user="' + userId + '">点击联系用户</a>');
                                $$('#order-detail-createTime').html(timeUtil(timestamp));
                                $$('#order-detail-status').html(currentStatusText);
                                $$('#order-status-update').html(nextStatusText).attr("data-status", status).attr("data-order", id);

                                $$('#toChat').on('click', function () {
                                    var toId = $$(this).attr("data-user");
                                    registerChat(toId)
                                })
                            }
                        }
                    );

                    $$('#order-status-update').on('click', function () {
                        var orderId = $(this).attr("data-order");
                        var status = $(this).attr("data-status");
                        var nextStatus = getNextStatus(status);
                        var json = {id: orderId, status: nextStatus};
                        app.dialog.confirm("确定执行操作吗？", function () {
                            app.request({
                                    url: host + "/order/update?token=" + localStorage.getItem("currentKeeperToken"),
                                    data: JSON.stringify(json),
                                    processData: false,
                                    method: "PUT",
                                    dataType: "json",
                                    contentType: "application/json",
                                    async: false,
                                    success: function (data, status, xhr) {
                                        if (data instanceof String)
                                            data = JSON.parse(data);//转json
                                        app.dialog.alert(data.message);
                                        if (data.flag === true) {
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
            name: 'profile',
            path: "/profile",
            url: "page/profile.html",
            on: {
                pageInit: function () {

                    var keeper = JSON.parse(localStorage.getItem("currentKeeper"));
                    document.querySelector("#profile_logo_img").src = host + '/res/' + keeper.logo;
                    document.querySelector("#profile_description").innerHTML = keeper.description;
                    $$("#profile_name").val(keeper.name);
                    document.querySelector("#profile_location").innerHTML = keeper.address == null ? "" : keeper.address;
                    /**
                     * 修改用户头像绑定事件
                     */
                    $$('#profile_logo').on('change', function () {
                        var keeper = JSON.parse(localStorage.getItem("currentKeeper"));
                        var imgList = asyncUploadFiles("#profile_logo_form");
                        if (imgList.length !== 0) {
                            var resource = imgList.pop()[0];
                            if (resource instanceof String)
                                resource = JSON.parse(resource);
                            document.querySelector("#profile_logo_img").src = host + '/res/' + resource.id;
                            document.querySelector("#keeper_logo").src = host + '/res/' + resource.id;
                            keeper.logo = resource.id;
                            localStorage.setItem("currentKeeper", JSON.stringify(keeper))
                        }
                        document.querySelector("#profile_logo").value = "";
                    });

                    /**
                     * 用户修改事件绑定
                     */
                    $$('#update-user-btn').on('click', function () {
                        var keeper = JSON.parse(localStorage.getItem("currentKeeper"));
                        //获取用户名
                        var newName = $$("#profile_name").val();
                        keeper.name = newName;
                        keeper.description = document.querySelector("#profile_description").innerHTML;
                        var token = localStorage.getItem("currentKeeperToken");
                        if (keeper.address !== null)
                            keeper.authenticated = true;
                        app.request({
                                url: host + "/keeper/update?token=" + token,
                                data: JSON.stringify(keeper),
                                processData: false,
                                method: "PUT",
                                dataType: "json",
                                contentType: "application/json",
                                async: false,
                                success: function (data, status, xhr) {
                                    app.dialog.close();
                                    if (data instanceof String)
                                        data = JSON.parse(data);//转json
                                    localStorage.setItem("currentKeeper", JSON.stringify(data));
                                    afterLogin(data);
                                    app.dialog.alert("修改完成");
                                }
                            }
                        )
                    })
                },
                pageReinit: function () {
                    var keeper = JSON.parse(localStorage.getItem("currentKeeper"));
                    document.querySelector("#profile_logo_img").src = host + '/res/' + keeper.logo;
                    document.querySelector("#profile_description").innerHTML = keeper.description;
                    $$("#profile_name").val(keeper.name);
                    document.querySelector("#profile_location").innerHTML = keeper.address == null ? "" : keeper.address;
                }
            }
        },
        {
            name: 'location',
            path: '/location',
            url: 'page/location.html',
            on: {
                pageInit: function (e, page) {
                    $$("#mapPage").attr("height", document.querySelector("body").clientHeight);
                    window.addEventListener('message', function (event) {
                        // 接收位置信息，用户选择确认位置点后选点组件会触发该事件，回传用户的位置信息
                        var loc = event.data;
                        if (loc && loc.module == 'locationPicker') {//防止其他应用也会向该页面post信息，需判断module是否为'locationPicker'
                            console.log('location', loc);
                            app.dialog.confirm("选好了吗？", function () {
                                var keeper = JSON.parse(localStorage.getItem("currentKeeper"));
                                keeper.address = loc.poiaddress + "--" + loc.poiname;
                                keeper.latitude = loc.latlng.lat;
                                keeper.longitude = loc.latlng.lng;
                                localStorage.setItem("currentKeeper", JSON.stringify(keeper));
                                mainView.router.back();
                            }, function () {

                            })
                        }
                    }, false);
                }
            }
        }

    ],
    // ... other parameters
});


function registerWebSocket(id) {
    var ws = new WebSocket("ws://" + basicHost + ":" + basicPort + "/server/order/" + id);
    ws.onopen = function (event) {
        console.log(">>>>>>>>>>>>>>>>>>>>>>");
        console.log(id + ": 连接WebSocket成功");
        console.log("<<<<<<<<<<<<<<<<<<<<<<");
    };

    ws.onmessage = function (message) {
        // Create full-layout notification
        var notificationFull = app.notification.create({
            title: '新的订单',
            titleRightText: 'now',
            text: message.data,
            closeTimeout: 3000,
        });
        notificationFull.open()
    };
}

function afterLogin(keeper) {
    document.querySelector("#keeper_logo").src = host + '/res/' + keeper.logo;
    document.querySelector("#keeper_name").innerHTML = keeper.name;
    //注册websocket
    registerWebSocket(keeper.id);
    initUserConnection(keeper);
    mainView.router.refreshPage();
}

var mainView = app.views.create('.view-main');

var dialog = app.dialog;

$$(document).on('page:init', '.page[data-name="shocks"]', function (e) {
    hasNext = true;
    currentShocksPage = 1;
    renderShocks(true);
});

function asyncUploadFiles(ele) {
    var formEle = document.querySelector(ele);
    var formData = new FormData(formEle);
    var list = [];
    jQuery.ajax({
        url: host + "/res/upload",
        type: "POST",
        data: formData,
        processData: false,
        contentType: false,
        cache: false,
        async: false,
        complete: function (data, status, xhr) {
            app.dialog.close();
            data = data.responseJSON;
            if (data instanceof String)
                data = JSON.parse(data);
            if (data.flag == true) {
                list.push(data.data);
            }
            // app.dialog.alert(data.message);
        }
    });
    return list;
}

if (!localStorage.hasOwnProperty("currentKeeperToken"))
    app.loginScreen.open("#login", true);
else {
    app.request({
        url: host + "/keeper/login?token=" + localStorage.getItem("currentKeeperToken"),
        method: "PUT",
        success(data, status, xhr) {
            data = JSON.parse(data);
            if (data.flag === false)
                app.dialog.alert(data.message, "请重新登录", function () {
                    app.loginScreen.open("#login", true)
                });
            else {
                localStorage.setItem("currentKeeperToken", data.token);
                localStorage.setItem("currentKeeper", JSON.stringify(data.keeper));
                afterLogin(data.keeper)
            }
        },
        statusCode: {
            404: function (xhr) {
                alert('page not found');
            }
        }
    })
}


$$('#login #login-btn').on('click', function () {
    var email = $$('#login [name="email"]').val();
    var password = $$('#login [name="password"]').val();

    app.request({
        url: host + "/keeper/login?email=" + email + "&password=" + password,
        method: "POST",
        success: function (data, status, xhr) {
            data = JSON.parse(data);//转json
            if (data.flag == false)
                app.dialog.alert(data.message == null ? "用户名和密码不正确，请重试" : data.message);
            else {
                localStorage.setItem("currentKeeperToken", data.token);
                localStorage.setItem("currentKeeper", JSON.stringify(data.keeper));
                // Close login screen
                afterLogin(data.keeper);
                app.loginScreen.close('#login', true);
            }

        }
    })


});


$$('#register #register-btn').on('click', function () {
    var email = $$('#register [name="email"]').val();
    var password = $$('#register [name="password"]').val();
    var repassword = $$('#register [name="re-password"]').val();

    if (repassword != password) {
        $$('#register [name="password"]').val("");
        $$('#register [name="re-password"]').val("")
        app.dialog.alert("两次密码不一致,请重新输入!!!");

        return;
    }
    if (email == null || "" == email) {
        $$('#register [name="email"]').val("");
        $$('#register [name="password"]').val("");
        $$('#register [name="repassword"]').val("");
        app.dialog.alert("用户名不能为空,请重新输入");
        return
    }


    app.request({
        url: host + "/keeper/save",
        data: JSON.stringify({
            "email": email,
            "password": password,
            "description": "",
            "authenticated": false,
            "longitude": 0.0,
            "latitude": 0.0,
            "logo": ""
        }),
        processData: false,
        method: "POST",
        dataType: "json",
        contentType: "application/json",
        success: function (data, status, xhr) {
            // data=JSON.parse(data);//转json
            if (data.flag == false) {
                app.dialog.alert(data.message);
                $$('#register [name="email"]').val("")
            } else {
                localStorage.setItem("currentKeeperToken", data.token);
                localStorage.setItem("currentKeeper", JSON.stringify(data.keeper));
                afterLogin(data.keeper);
                app.loginScreen.close("#register", true);
                app.loginScreen.close('#login', true);

            }
        }
    });
});
var iconTooltip = app.tooltip.create({
    targetEl: '#loadMoreShock',
    text: '加载更多',
});

function loadShock(page) {
    var rtn = {};
    app.request({
            url: host + "/shock/" + page + "?token=" + localStorage.getItem("currentKeeperToken"),
            processData: false,
            method: "GET",
            dataType: "json",
            contentType: "application/json",
            async: false,
            success: function (data, status, xhr) {
                app.dialog.close();
                if (data instanceof String)
                    data = JSON.parse(data);//转json
                if (data.flag === true) {
                    rtn.data = data.data;
                    rtn.hasNext = data.hasNext;
                }

            }
        }
    );
    return rtn;
}


function renderShocks(clear = false) {
    var shocks = loadShock(currentShocksPage);
    hasNext = shocks.hasNext;
    var shockData = shocks.data;
    if (clear)
        $$('#shocks').find(".toManage").remove();
    for (var i in shockData) {
        var shock = shockData[i];
        var date = new Date(shock.timestamp);
        var Y = date.getFullYear() + '-';
        var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
        var D = date.getDate() + ' ';
        var h = date.getHours() + ':';
        var m = date.getMinutes() + ':';
        var s = date.getSeconds();
        var html = "<div class='card toManage' data-id='" + shock.id + "'>" +
            "<div class='card-header align-items-flex-end'><img src='" + host + "/res/" + shock.logo + "' width='34' height='34' alt=''>" + shock.title + "</div>" +
            "<div class='card-content card-content-padding'>" +
            "<p class='date'>发布时间 ：" + Y + M + D + h + m + s + "</p>" +
            "<p>" + shock.text + "</p>" +
            "</div>" +
            "<div class='card-footer'><a href='javascript:' class='f7-color-green link'>￥" + shock.price + "</a></div>" +
            "</div>";
        $$('#shocks').append(html);
    }
}

renderShocks();

$$('#loadMoreShock').on('click', function () {
    if (hasNext === false) {
        app.dialog.alert("没有更多数据了");
    } else {
        app.dialog.preloader('加载中');
        currentShocksPage++;
        renderShocks();
        app.dialog.close();
    }
});

$$('.toManage').on('click', function () {
    var id = $$(this).attr("data-id");
    mainView.router.navigate({
        name: 'manage-shock',
        params: {id: id},
    });
});

function renderSingleShock(id) {
    app.request({
            url: host + "/shock/single/" + id + "?token=" + localStorage.getItem("currentKeeperToken"),
            processData: false,
            method: "GET",
            dataType: "json",
            contentType: "application/json",
            async: false,
            success: function (data, status, xhr) {
                app.dialog.close();
                if (data instanceof String)
                    data = JSON.parse(data);//转json
                $$("#shock-update-title").val(data.title);
                document.querySelector("#shock-update-logo-img").src = host + "/res/" + data.logo;
                $$("#shock-update-logo-value").val(data.logo);
                var imgs = data.resources;
                for (var i in imgs) {
                    var img = imgs[i];
                    var template = "<img src='" + host + '/res/' + img + "' data-id='" + img + "' style='line-height: 50px' width='50' height='50' >";
                    $$('#shock-update-imgs-list').append(template);
                }
                $$("#shock-update-description").html(data.text);
                $$("#shock-update-amount").val(data.last);
                $$("#shock-update-price").val(data.price);
            }
        }
    );
}


function getNextStatus(current) {
    if (current == ORDER_BEGIN)
        return ORDER_RECEIVED;
    else if (current == ORDER_RECEIVED)
        return ORDER_HANDLED;
    else if (current == ORDER_HANDLED)
        return ORDER_WORKING;
    else if (current == ORDER_WORKING)
        return ORDER_FINISHED;
    else if (current == 50)
        return ORDER_FINISHED;
    else return -1;
}

function getStatusTextMap(current) {
    if (current == ORDER_BEGIN)
        return "接单";
    else if (current == ORDER_RECEIVED)
        return "开始拣货";
    else if (current == ORDER_HANDLED)
        return "拣货完成，提醒取货";
    else if (current >= ORDER_WORKING)
        return "确认完成";
    else return "";
}

function onChangeStatus(status, page) {
    token = localStorage.getItem("currentKeeperToken");
    var list = {};
    app.request({
            url: host + "/order/" + page + "/" + status + "?token=" + token,
            processData: false,
            method: "GET",
            dataType: "json",
            contentType: "application/json",
            async: false,
            success: function (data, status, xhr) {
                app.dialog.close();
                if (data instanceof String)
                    data = JSON.parse(data);//转json
                list = data;
                orderHasNext = data.hasNext;
            }
        }
    );
    return list;
}

function statusTextMap(status) {
    var txt = "";
    switch (status) {
        case ORDER_BEGIN: {
            txt = "未接单"
        }
            ;
            break;
        case ORDER_RECEIVED: {
            txt = "已接单"
        }
            ;
            break;
        case ORDER_HANDLED : {
            txt = "拣货中"
        }
            ;
            break;
        case ORDER_WORKING: {
            txt = "等待用户取货确认"
        }
            ;
            break;
        case ORDER_FINISHED: {
            txt = "订单已完成"
        }
            ;
            break;
        case 50: {
            txt = "等待商家确认"
        }
            ;
            break;
        case ORDER_CANCEL: {
            txt = "订单已取消"
        }
            ;
            break;
    }
    return txt;
}