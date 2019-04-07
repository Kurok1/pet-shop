window.onload = function () {
    function init() {
        // 创建地图
        document.getElementById('container').clientHeight=window.height
        var center = new qq.maps.LatLng(39.910390,116.397369);
        var map = new qq.maps.Map(document.getElementById('container'),{
            center: center,
            zoom: 13
        });
        var infoWin = new qq.maps.InfoWindow({
            map: map
        });
        infoWin.open();
        infoWin.setContent('<p><img src="https://cdn.framework7.io/placeholder/nature-1000x700-8.jpg" width="125"/></p>');
        infoWin.setPosition(map.getCenter());
        //setMap
        var mapM=document.getElementById("mapM");
        qq.maps.event.addDomListener(mapM,"click",function(){
            if(infoWin.getMap()){
                infoWin.setMap(null);
            }else{
                infoWin.setMap(map);
            }
        });
        //setVisible
        var flag=true;
        var setP=document.getElementById("setP");
        var latLng=new qq.maps.LatLng(39.908701,116.397497);
        qq.maps.event.addDomListener(setP,"click",function(){
            infoWin.setMap(map);
            if(flag){
                flag=false;
                infoWin.setPosition(latLng);
            }else{
                flag=true;
                infoWin.setPosition(center);
            }
        });
        var anchor = new qq.maps.Point(0, 39),
            size = new qq.maps.Size(42, 68),
            origin = new qq.maps.Point(0, 0),
            markerIcon = new qq.maps.MarkerImage(
                "https://lbs.qq.com/doc/img/nilt.png",
                size,
                origin,
                anchor
            );
        // marker.setIcon(markerIcon);
    }
    //调用初始化函数
    init();
}