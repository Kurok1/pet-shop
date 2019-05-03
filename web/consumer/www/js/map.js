var keeperSize=5;
var accurate=5.0;
function init() {
        // 创建地图
        document.getElementById('container').style.top=0;
        var center = new qq.maps.LatLng(39.0920,117.39811);
        var map = new qq.maps.Map(document.getElementById('container'),{
            center: center,
            zoom: 13, 
            draggable:false
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
                }

            }}
        );

    }
window.onload = function () {
    
    //调用初始化函数
    // init();

};