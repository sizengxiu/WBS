
var globalURL="localhost:8080";
$(document).ready(function() {

});

/**
 * 获取目标列表
 */
function getGoalList(){
    var des=$("#goalDec").val();
    console.info(des);
    $.ajax({
        type:"post",
        url:"/wbs/task/getGoals",
        data:{"des":des},
        dataType:"json",
        success:function(result){
        if(result.success){
            console.info(result);
            var menuList="";
            $.each(result.data,function(i,item){
                menuList+="<li class=\"list-group-item\">";
                // menuList +=item.des;
                menuList +=i+"、<a  href='javascript:;' onclick='openGoal(\""+item.id+"\",\""+item.des+"\")'  target='content'><span>"+item.des+"</span></a>";
                menuList +="</li>";
            });
            $("#goal_list_ul").html('');;
            $("#goal_list_ul").append(menuList);
            console.info(menuList);
        }else{
            $("#searchResult").html("出现错误：" + result.msg);
        }
    },
    error:function(jqXHR){
        alert("发生错误："+ jqXHR.status);
    }
});

}