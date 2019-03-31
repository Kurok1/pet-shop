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

var host="http://localhost/"

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
            name:'user',
            path:'/user/',
            url:'pages/user.html'
        },
        {
            name:'chat',
            path:'/chat',
            url:'pages/chat/single.html'
        },
        {
            name:'moment',
            path:'/moment',
            url:"pages/moment/detail.html"
        },
        {
            name:'moment-add',
            path:'/moment/add',
            url:'pages/moment/add.html'
        },
        {
            name:'user',
            path:'/profile',
            url:'pages/user.html'
        },
        {
            name:'friends',
            path:'/friends',
            url:'pages/friends.html'
        }
    ],
    // ... other parameters
});

var mainView = app.views.create('.view-main');
// mainView.router.navigate({name:'about'})
// //

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
                                data=JSON.parse(data)
                                if(data.flag==false)
                                    app.dialog.alert(data.message,"请重新登录",function () {
                                        app.loginScreen.open("#login",true)
                                    });
                                else {
                                    localStorage.setItem("currentUserToken",data.token);
                                }
                            }})
}

$$('#submit_file').on('click',function () {
    var fd = new FormData(document.forms.namedItem("files"));
    console.log(fd)
})

$$('#login #login-btn').on('click', function () {
    var username = $$('#login [name="username"]').val();
    var password = $$('#login [name="password"]').val();

    app.request({url:host+"/user/login?username="+username+"&password="+password,
                            method:"POST",
                            success:function(data, status, xhr){
                                data=JSON.parse(data);//转json
                                if(data.flag==false)
                                    app.dialog.alert(data.message==null?"用户名和密码不正确，请重试":data.message);
                                else{
                                    localStorage.setItem("currentUserToken",data.token);
                                    // Close login screen
                                    app.loginScreen.close('#login',true);
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
            if(data.flag==false){
                app.dialog.alert(data.message);
                $$('#register [name="username"]').val("");
            }
            else {
                localStorage.setItem("currentUserToken",data.token);//存储token
                app.loginScreen.close("#register",true);
                app.loginScreen.close('#login',true);
            }
        }}
    )

})
