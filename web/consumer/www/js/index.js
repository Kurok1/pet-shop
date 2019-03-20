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

var app = new Framework7({
    // App root element
    root: '#app',
    // App Name
    name: 'My App',
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
            name: 'login',
            path: '/login/',
            url: 'login.html'
            // content: '<div class="page no-navbar no-toolbar no-swipeback"><div class="page-content login-screen-content">'+
            //             '<div class="login-screen-title">My App</div>'+
            //             '<form>'+
            //             '<div class="list">'+
            //             '...'+
            //             '</div>'+
            //             '<div class="list">'+
            //             '<ul>'+
            //                 '<li><a href="#" class="item-link list-button">Sign In</a></li>'+
            //             '</ul>'+
            //             '<div class="block-footer">'+
            //             '<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>'+
            //             '<p><a href="#" class="link back">Close Login Screen</a></p>'+
            //             '</div></div></form></div></div>'
        }
    ],
    // ... other parameters
});

var mainView = app.views.create('.view-main');
// mainView.router.navigate({name:'about'})
// //
// app.loginScreen.create("#login-screen").open(true);
// $$("#login").on('click',function () {
//
//     var login=app.loginScreen.create("#login-screen");
//     console.log(login);
//     login.open(true)
// })

