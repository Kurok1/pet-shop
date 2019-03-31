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

var app = new Framework7({
    // App root element
    root: '#app',
    // App Name
    name: 'My App',
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
            url: 'index.html',
        },
        {
            name:'info',
            path: '/info/:userId/:postId',
            url: 'page/auth/info.html',
            on: {
                pageAfterIn: function test (e, page) {
                    // do something after page gets into the view
                },
                pageInit: function (e, page) {
                    console.log(page);
                },
            }
        },
        {
            name:"add-shock",
            path:'/add-shock',
            url:'page/shock/add.html'
        },
        {
            name:"manage-shock",
            path:'/manage-shock',
            url:'page/shock/manage.html'
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
                    // do something after page gets into the view
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
            url:"page/profile.html"
        }

    ],
    // ... other parameters
});



var mainView = app.views.create('.view-main');
// mainView.router.navigate({name:'about'})
// //

var dialog=app.dialog;

if(!localStorage.hasOwnProperty("currentKeeperToken"))
    app.loginScreen.open("#login",true);
else{
    app.request({url:host+"/keeper/login?token="+localStorage.getItem("currentKeeperToken"),
                            method:"PUT",
                            success(data, status, xhr) {
                                data=JSON.parse(data);
                                console.log(status)
                                if(data.flag==false || status!=200)
                                    app.dialog.alert(data.message,"请重新登录",function () {
                                        app.loginScreen.open("#login",true)
                                    });
                                else {

                                }
                            },
                            statusCode: {
                                404: function (xhr) {
                                    alert('page not found');
                                }
                            }
    })
}


$$('#toInfo').on('click',function () {
    mainView.router.navigate({
        name: 'info',
        params: { userId: 1, postId: 2 },
    });
})

$$('.toManage').on('click',function(){
    mainView.router.navigate({
        name: 'manage-shock'
    });
})

$$('#login #login-btn').on('click', function () {
    var email = $$('#login [name="email"]').val();
    var password = $$('#login [name="password"]').val();

    app.request({url:host+"/keeper/login?email="+email+"&password="+password,
                            method:"POST",
                            success:function(data, status, xhr){
                                console.log(data)
                                data=JSON.parse(data);//转json
                                if(data.flag==false)
                                    app.dialog.alert(data.message==null?"用户名和密码不正确，请重试":data.message);
                                else{
                                    localStorage.setItem("currentKeeperToken",data.token);
                                    localStorage.setItem("currentKeeper",JSON.stringify(data.keeper));
                                    // Close login screen
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
                localStorage.setItem("currentKeeper",data.keeper);
                app.loginScreen.close("#register",true);
                app.loginScreen.close('#login',true);
                
            }
        }
    });
})
