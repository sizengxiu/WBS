var globalURL = "localhost:8080";

/*
var $go = go.GraphObject.make;
var myDiagram =
    $go(go.Diagram, "myDiagramDiv",
        {
            "undoManager.isEnabled": true,
            layout: $go(go.TreeLayout, // specify a Diagram.layout that arranges trees
                {angle: 0, layerSpacing: 35})
        });
myDiagram.nodeTemplate =
    $go(go.Node, "Horizontal",
        {background: "#44CCFF"},
        { selectionChanged: ChangedSelection},
        $go(go.TextBlock, "Default Text",
            {margin: 1, stroke: "white", font: "bold 16px sans-serif", isMultiline: true},
            new go.Binding("text", "name"),
            new go.Binding("editable", "editable")
        ),

        $go(go.Panel,
            {alignment: go.Spot.Right},
            $go("TreeExpanderButton") // 设置收缩按钮
        )
    );
var model = $go(go.TreeModel);
model.nodeDataArray =
    [ // the "key" and "parent" property names are required,
        // but you can add whatever data properties you need for your app
        {editable: true, key: "1", name: "Don Meow"},
        {editable: true, key: "2", parent: "1", name: "Demeter"},
        {editable: true, key: "3", parent: "1", name: "Copricat"},
        {editable: true, key: "4", parent: "2", name: "Jellylorum"},
        {editable: true, key: "5", parent: "2", name: "Alonzo"},
        {editable: true, key: "6", parent: "3", name: "Munkustrap"}
    ];
myDiagram.model = model;
myDiagram.linkTemplate =
    $go(go.Link,
        {routing: go.Link.Orthogonal, corner: 5},
        $go(go.Shape, {strokeWidth: 3, stroke: "#555"}));
*/
$(document).ready(function () {

    $('#goalAddDialog').on('show.bs.modal', function(){
        var $this = $(this);
        var $modal_dialog = $this.find('.modal-dialog');
        $this.css('display', 'block');
        $modal_dialog.css({'margin-top': Math.max(0, ($(window).height() - $modal_dialog.height()) / 2) });
    });

    $("#goalAddDialog").modal({backdrop: false, keyboard: false});
});

/**
 * 获取目标列表
 */
function getGoalList() {
    var des = $("#goalDec").val();
    console.info(des);
    $.ajax({
        type: "post",
        url: "/wbs/task/getGoals",
        data: {"des": des},
        dataType: "json",
        success: function (result) {
            if (result.success) {
                console.info(result);
                var menuList = "";
                $.each(result.data, function (i, item) {
                    menuList += "<li class=\"list-group-item\">";
                    // menuList +=item.des;
                    menuList += i + "、<a  href='javascript:;' onclick='openGoal(\"" + item.id + "\",\"" + item.des + "\")'  target='content'><span>" + item.des + "</span></a>";
                    menuList += "</li>";
                });
                $("#goal_list_ul").html('');
                ;
                $("#goal_list_ul").append(menuList);
                console.info(menuList);
            } else {
                $("#searchResult").html("出现错误：" + result.msg);
            }
        },
        error: function (jqXHR) {
            alert("发生错误：" + jqXHR.status);
        }
    });
}
function openAddGoalPanel(){

}


function openGoal(id, des) {
    $.ajax({
        type: "post",
        url: "/wbs/task/getTaskTreeByGoalId",
        data: {"goalId": id},
        dataType: "json",
        success: function (result) {
            if (result.success) {
                console.info(result.data);
            }
        }
    });
}