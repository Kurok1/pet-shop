var keeperSize=5;
var accurate=5.0;
window.onload = function () {
    function init() {
        // 创建地图
        document.getElementById('container').style.top=0;
        var center = new qq.maps.LatLng(39.0920,117.39811);
        var map = new qq.maps.Map(document.getElementById('container'),{
            center: center,
            zoom: 13
        });
        var userLocationInfo={
            "latitude":39.09204,
            "longitude":117.09813,
            "accurate":accurate
        };
        app.request({url:host+"shock/get?token="+localStorage.getItem("currentUserToken")+"&size="+keeperSize,
            data:JSON.stringify(userLocationInfo),
            processData:false,
            method:"POST",
            dataType:"json",
            contentType:"application/json",
            async:false,
            success:function(data, status, xhr){
                if(data instanceof String)
                    data=JSON.parse(data);
                var shocks=data.shocks;
                for (var i in shocks){
                    var shock=shocks[i];
                    var infoWin = new qq.maps.InfoWindow({
                        map: map
                    });
                    infoWin.open();
                    infoWin.setContent("<p class='shock'><a href='/shock/detail/"+shock.shock.id+"'><img src='"+host+"/res/"+shock.shopkeeperLogo+"' width='50'/></a></p>");
                    infoWin.setPosition(new qq.maps.LatLng(shock.latitude,shock.longitude));
                    qq.maps.event.addListener(
                        infoWin,
                        'click',
                        function(event) {
                            console.log(event);
                        }
                    );
                }

            }}
        );

        // var infoWin = new qq.maps.InfoWindow({
        //     map: map
        // });
        // infoWin.open();
        // infoWin.setContent('<p><img src="https://cdn.framework7.io/placeholder/nature-1000x700-8.jpg" width="50"/></p>');
        // infoWin.setPosition(map.getCenter());
        // //setMap
        // var mapM=document.getElementById("mapM");
        // qq.maps.event.addDomListener(mapM,"click",function(){
        //     console.log(mapM);
        // });
        //setVisible
        var flag=true;
        var setP=document.getElementById("setP");
    //     var latLng=new qq.maps.LatLng(39.908701,116.397497);
    //     qq.maps.event.addDomListener(setP,"click",function(){
    //         infoWin.setMap(map);
    //         if(flag){
    //             flag=false;
    //             infoWin.setPosition(latLng);
    //         }else{
    //             flag=true;
    //             infoWin.setPosition(center);
    //         }
    //     });
    //     var anchor = new qq.maps.Point(0, 39),
    //         size = new qq.maps.Size(42, 68),
    //         origin = new qq.maps.Point(0, 0),
    //         markerIcon = new qq.maps.MarkerImage(
    //             "https://lbs.qq.com/doc/img/nilt.png",
    //             size,
    //             origin,
    //             anchor
    //         );
    //     // marker.setIcon(markerIcon);
    }
    //调用初始化函数
    init();

};