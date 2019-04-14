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

var $$=Dom7;

var host="http://localhost:8080/"
var currentShocksPage=1;//当前为第一页
var hasNext=true;
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
            name:"add-shock",
            path:'/add-shock',
            url:'page/shock/add.html',
            on:{
                pageInit:function (e,page) {
                    $$('#shock-add-logo').on('change', function () {
                        var imgList = asyncUploadFiles("#shock_logo_form");
                        if (imgList.length !== 0) {
                            var resource=imgList.pop()[0];
                            if(resource instanceof String)
                                resource = JSON.parse(resource);
                            document.querySelector("#shock-logo").src=host + '/res/' + resource.id;
                            $$("#shock-logo-value").val(resource.id);
                        }
                        document.querySelector("#shock-add-logo").value="";
                    });
                    $$("#shock-add-imgs").on('change',function () {
                        var imgList = asyncUploadFiles("#shock_imgs_form").pop();
                        if(imgList.length!==0){
                            for (var i in imgList){
                                var resource=imgList[i];
                                if(resource instanceof String)
                                    resource = JSON.parse(resource);
                                var template="<img src='"+host+'/res/'+resource.id+"' data-id='"+resource.id+"' style='line-height: 50px' width='50' height='50' >"
                                $$('#shock-add-imgs-list').append(template);
                            }
                        }
                    });
                    $$('#shock-publish').on('click',function () {
                        //收集数据
                        var keeper=JSON.parse(localStorage.getItem("currentKeeper"));
                        var data={};
                        data.shopkeeperId=keeper.id;
                        data.last=$$("#shock-amount").val();
                        data.price=$$("#shock-price").val();
                        data.text=$$('#shock-description').html();
                        data.logo=$$('#shock-logo-value').val();
                        var imgs=[];
                        $$('#shock-add-imgs-list img').forEach(function (item) {
                            imgs.push(item.getAttribute("data-id"));
                        });
                        data.resources=imgs;
                        data.title=$$("#shock-title").val();
                        app.request({url:host+"/shock/save?token="+localStorage.getItem("currentKeeperToken"),
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
                                if(data.flag===true)
                                    mainView.router.back();
                            }}
                        )
                    });
                }
            }
        },
        {
            name:"manage-shock",
            path:'/manage-shock/:id',
            url:'page/shock/manage.html',
            on:{
                pageInit:function (e,page) {
                    var shockId=page.route.params.id;
                    renderSingleShock(shockId);

                    $$('#shock-update-logo').on('change', function () {
                        var imgList = asyncUploadFiles("#shock-update-logo-form");
                        if (imgList.length !== 0) {
                            var resource=imgList.pop()[0];
                            if(resource instanceof String)
                                resource = JSON.parse(resource);
                            document.querySelector("#shock-update-logo-img").src=host + '/res/' + resource.id;
                            $$("#shock-update-logo-value").val(resource.id);
                        }
                        document.querySelector("#shock-update-logo").value="";
                    });
                    $$("#shock-update-imgs-list img").on('click',function () {
                        var id=$(this).attr("data-id");
                        app.dialog.confirm("确定要删除吗？", function(){
                            app.request({
                                    url: host + "/res/"+id+"?token=" + localStorage.getItem("currentKeeperToken"),
                                    method: "DELETE",
                                    dataType: "json",
                                    contentType: "application/json",
                                    async: false,
                                    success: function (data, status, xhr) {
                                        if (data instanceof String)
                                            data = JSON.parse(data);//转json
                                        if(data.flag===true){
                                            $$("#shock-update-imgs-list").find('img[data-id='+id+']').remove();
                                        }
                                    }
                                }
                            )
                        }, function () {
                        });
                    });
                    $$("#shock-update-imgs").on('change',function () {
                        var imgList = asyncUploadFiles("#shock-update-imgs-form").pop();
                        if(imgList.length!==0){
                            for (var i in imgList){
                                var resource=imgList[i];
                                if(resource instanceof String)
                                    resource = JSON.parse(resource);
                                var template="<img src='"+host+'/res/'+resource.id+"' data-id='"+resource.id+"' style='line-height: 50px' width='50' height='50' >";
                                $$('#shock-update-imgs-list').append(template);
                            }
                        }
                        $$("#shock-update-imgs-list img").on('click',function () {
                            var id=$(this).attr("data-id");
                            app.dialog.confirm("确定要删除吗？", function(){
                                app.request({
                                        url: host + "/res/"+id+"?token=" + localStorage.getItem("currentKeeperToken"),
                                        method: "DELETE",
                                        dataType: "json",
                                        contentType: "application/json",
                                        async: false,
                                        success: function (data, status, xhr) {
                                            if (data instanceof String)
                                                data = JSON.parse(data);//转json
                                            if(data.flag===true){
                                                $$("#shock-update-imgs-list").find('img[data-id='+id+']').remove();
                                            }
                                        }
                                    }
                                )
                            }, function () {
                            });
                        });
                    });

                    $$('#shock-update-btn').on('click',function () {
                        //收集数据
                        var keeper=JSON.parse(localStorage.getItem("currentKeeper"));
                        var data={};
                        data.id=shockId;
                        data.shopkeeperId=keeper.id;
                        data.last=$$("#shock-update-amount").val();
                        data.price=$$("#shock-update-price").val();
                        data.text=$$('#shock-update-description').html();
                        data.logo=$$('#shock-update-logo-value').val();
                        var imgs=[];
                        $$('#shock-update-imgs-list img').forEach(function (item) {
                            imgs.push(item.getAttribute("data-id"));
                        });
                        data.resources=imgs;
                        data.title=$$("#shock-update-title").val();
                        app.request({url:host+"/shock/update?token="+localStorage.getItem("currentKeeperToken"),
                            data:JSON.stringify(data),
                            processData:false,
                            method:"PUT",
                            dataType:"json",
                            contentType:"application/json",
                            async:false,
                            beforeSend: function () {
                                app.dialog.preloader('请稍等');
                            },
                            success:function(data, status, xhr){
                                app.dialog.close();
                                if(data instanceof String)
                                    data=JSON.parse(data);//转json
                                if(data.flag===true)
                                    mainView.router.navigate({
                                        name:"main"
                                    });
                                app.dialog.alert(data.message);

                            }}
                        )
                    });

                }
            }
        },
        {
            name:'chats',
            path:'/chats',
            url:'page/chats/list.html'
        },
        {
            name:'chat',
            path:'/chat',
            url:'page/chats/single.html',
            on: { //需要隐藏下部toolbar
                pageAfterIn: function test (e, page) {
                    app.toolbar.hide(".toolbar", true)
                },
                pageInit: function (e, page) {
                    app.toolbar.hide(".toolbar", true)
                },
            }
        },
        {
            name:'orders',
            path:"/orders",
            url:'page/order/list.html'
        },
        {
            name:'order',
            path:"/order",
            url:'page/order/detail.html'
        },
        {
            name:'profile',
            path:"/profile",
            url:"page/profile.html",
            on:{
                pageInit:function () {

                    var keeper=JSON.parse(localStorage.getItem("currentKeeper"));
                    document.querySelector("#profile_logo_img").src=host + '/res/' + keeper.logo;
                    document.querySelector("#profile_description").innerHTML=keeper.description;
                    $$("#profile_name").val(keeper.name);
                    document.querySelector("#profile_location").innerHTML=keeper.address==null?"":keeper.address;
                    /**
                     * 修改用户头像绑定事件
                     */
                    $$('#profile_logo').on('change', function () {
                        var keeper=JSON.parse(localStorage.getItem("currentKeeper"));
                        var imgList = asyncUploadFiles("#profile_logo_form");
                        if (imgList.length !== 0) {
                            var resource=imgList.pop()[0];
                            if(resource instanceof String)
                                resource = JSON.parse(resource);
                            document.querySelector("#profile_logo_img").src=host + '/res/' + resource.id;
                            document.querySelector("#keeper_logo").src=host + '/res/' + resource.id;
                            keeper.logo =  resource.id;
                            localStorage.setItem("currentKeeper",JSON.stringify(keeper))
                        }
                        document.querySelector("#profile_logo").value="";
                    });

                    /**
                     * 用户修改事件绑定
                     */
                    $$('#update-user-btn').on('click',function () {
                        var keeper=JSON.parse(localStorage.getItem("currentKeeper"));
                        //获取用户名
                        var newName=$$("#profile_name").val();
                        keeper.name=newName;
                        keeper.description=document.querySelector("#profile_description").innerHTML;
                        var token=localStorage.getItem("currentKeeperToken");
                        if(keeper.address!==null)
                            keeper.authenticated=true;
                        app.request({url:host+"/keeper/update?token="+token,
                            data:JSON.stringify(keeper),
                            processData:false,
                            method:"PUT",
                            dataType:"json",
                            contentType:"application/json",
                            async:false,
                            success:function(data, status, xhr){
                                app.dialog.close();
                                if(data instanceof String)
                                    data=JSON.parse(data);//转json
                                localStorage.setItem("currentKeeper",JSON.stringify(data));
                                afterLogin(data);
                                app.dialog.alert("修改完成");
                            }}
                        )
                    })
                },
                pageReinit:function () {
                    var keeper=JSON.parse(localStorage.getItem("currentKeeper"));
                    document.querySelector("#profile_logo_img").src=host + '/res/' + keeper.logo;
                    document.querySelector("#profile_description").innerHTML=keeper.description;
                    $$("#profile_name").val(keeper.name);
                    document.querySelector("#profile_location").innerHTML=keeper.address==null?"":keeper.address;
                }
            }
        },
        {
            name:'location',
            path:'/location',
            url:'page/location.html',
            on:{
                pageInit:function(e,page){
                    $$("#mapPage").attr("height",document.querySelector("body").clientHeight);
                    window.addEventListener('message', function(event) {
                        // 接收位置信息，用户选择确认位置点后选点组件会触发该事件，回传用户的位置信息
                        var loc = event.data;
                        if (loc && loc.module == 'locationPicker') {//防止其他应用也会向该页面post信息，需判断module是否为'locationPicker'
                            console.log('location', loc);
                            app.dialog.confirm("选好了吗？",function(){
                                var keeper=JSON.parse(localStorage.getItem("currentKeeper"));
                                keeper.address=loc.poiaddress+"--"+loc.poiname;
                                keeper.latitude=loc.latlng.lat;
                                keeper.longitude=loc.latlng.lng;
                                localStorage.setItem("currentKeeper",JSON.stringify(keeper));
                                mainView.router.back();
                            },function () {

                            })
                        }
                    }, false);
                }
            }
        }

    ],
    // ... other parameters
});

function afterLogin(keeper) {
    document.querySelector("#keeper_logo").src=host + '/res/' + keeper.logo;
    document.querySelector("#keeper_name").innerHTML=keeper.name;
}

var mainView = app.views.create('.view-main');

var dialog=app.dialog;

$$(document).on('page:init', '.page[data-name="shocks"]', function (e) {
    console.log(111);
    hasNext=true;
    currentShocksPage=1;
    renderShocks(true);
});

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

if(!localStorage.hasOwnProperty("currentKeeperToken"))
    app.loginScreen.open("#login",true);
else{
    app.request({url:host+"/keeper/login?token="+localStorage.getItem("currentKeeperToken"),
                            method:"PUT",
                            success(data, status, xhr) {
                                data=JSON.parse(data);
                                if(data.flag===false)
                                    app.dialog.alert(data.message,"请重新登录",function () {
                                        app.loginScreen.open("#login",true)
                                    });
                                else {
                                    localStorage.setItem("currentKeeperToken",data.token);
                                    localStorage.setItem("currentKeeper",JSON.stringify(data.keeper));
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

    app.request({url:host+"/keeper/login?email="+email+"&password="+password,
                            method:"POST",
                            success:function(data, status, xhr){
                                data=JSON.parse(data);//转json
                                if(data.flag==false)
                                    app.dialog.alert(data.message==null?"用户名和密码不正确，请重试":data.message);
                                else{
                                    localStorage.setItem("currentKeeperToken",data.token);
                                    localStorage.setItem("currentKeeper",JSON.stringify(data.keeper));
                                    // Close login screen
                                    afterLogin(data.keeper);
                                    app.loginScreen.close('#login',true);
                                }

                            }
                            })



});


$$('#register #register-btn').on('click',function () {
    var email = $$('#register [name="email"]').val();
    var password = $$('#register [name="password"]').val();
    var repassword=$$('#register [name="re-password"]').val();

    if(repassword!=password){
        $$('#register [name="password"]').val("");
        $$('#register [name="re-password"]').val("")
        app.dialog.alert("两次密码不一致,请重新输入!!!");

        return;
    }
    if (email==null || ""==email){
        $$('#register [name="email"]').val("");
        $$('#register [name="password"]').val("");
        $$('#register [name="repassword"]').val("");
        app.dialog.alert("用户名不能为空,请重新输入");
        return
    }


    app.request({url:host+"/keeper/save",
        data:JSON.stringify({
            "email":email,
            "password":password,
            "description":"",
            "authenticated":false,
            "longitude":0.0,
            "latitude":0.0,
            "logo":""
        }),
        processData:false,
        method:"POST",
        dataType:"json",
        contentType:"application/json",
        success:function(data, status, xhr){
            // data=JSON.parse(data);//转json
            if(data.flag==false){
                app.dialog.alert(data.message);
                $$('#register [name="email"]').val("")
            }else {
                localStorage.setItem("currentKeeperToken",data.token);
                localStorage.setItem("currentKeeper",JSON.stringify(data.keeper));
                afterLogin(data.keeper);
                app.loginScreen.close("#register",true);
                app.loginScreen.close('#login',true);
                
            }
        }
    });
});
var iconTooltip = app.tooltip.create({
    targetEl: '#loadMoreShock',
    text: '加载更多',
});

function loadShock(page) {
    var rtn={};
    app.request({url:host+"/shock/"+page+"?token="+localStorage.getItem("currentKeeperToken"),
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


function renderShocks(clear=false) {
    var shocks=loadShock(currentShocksPage);
    hasNext=shocks.hasNext;
    var shockData=shocks.data;
    if(clear)
        $$('#shocks').find(".toManage").remove();
    for (var i in shockData){
        var shock=shockData[i];
        var date = new Date(shock.timestamp);
        var Y = date.getFullYear() + '-';
        var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
        var D = date.getDate() + ' ';
        var h = date.getHours() + ':';
        var m = date.getMinutes() + ':';
        var s = date.getSeconds();
        var html="<div class='card toManage' data-id='"+shock.id+"'>" +
            "<div class='card-header align-items-flex-end'><img src='"+host+"/res/"+shock.logo+"' width='34' height='34' alt=''>"+shock.title+"</div>" +
            "<div class='card-content card-content-padding'>" +
            "<p class='date'>发布时间 ："+Y+M+D+h+m+s+"</p>" +
            "<p>"+shock.text+"</p>" +
            "</div>" +
            "<div class='card-footer'><a href='javascript:' class='f7-color-green link'>￥"+shock.price+"</a></div>" +
            "</div>";
        $$('#shocks').append(html);
    }
}

renderShocks();

$$('#loadMoreShock').on('click',function () {
    if(hasNext===false){
        app.dialog.alert("没有更多数据了");
    }else {
        app.dialog.preloader('加载中');
        currentMessagePage++;
        renderShocks();
        app.dialog.close();
    }
});

$$('.toManage').on('click',function () {
    var id=$$(this).attr("data-id");
    mainView.router.navigate({
        name: 'manage-shock',
        params: { id:id },
    });
});

function renderSingleShock(id) {
    app.request({url:host+"/shock/single/"+id+"?token="+localStorage.getItem("currentKeeperToken"),
        processData:false,
        method:"GET",
        dataType:"json",
        contentType:"application/json",
        async:false,
        success:function(data, status, xhr){
            app.dialog.close();
            if(data instanceof String)
                data=JSON.parse(data);//转json
            $$("#shock-update-title").val(data.title);
            document.querySelector("#shock-update-logo-img").src=host+"/res/"+data.logo;
            $$("#shock-update-logo-value").val(data.logo);
            var imgs=data.resources;
            for(var i in imgs){
                var img=imgs[i];
                var template="<img src='"+host+'/res/'+img+"' data-id='"+img+"' style='line-height: 50px' width='50' height='50' >";
                $$('#shock-update-imgs-list').append(template);
            }
            $$("#shock-update-description").html(data.text);
            $$("#shock-update-amount").val(data.last);
            $$("#shock-update-price").val(data.price);
        }}
    );
}