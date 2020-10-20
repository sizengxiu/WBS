var globalURL = "localhost:8080";

var $go = go.GraphObject.make;
var myDiagram;

//所选的类别信息
var selectedItemId=null;
var selectedItemDes=null;

var globalVm;
var globalItemData={selectedItemId:selectedItemId,selectedItemDes:selectedItemDes,hello:"sadf"};



$(document).ready(function () {
    globalVm = new Vue({
        el: '#goalAddDialog',
        data: globalItemData
    });
    $('#itemAddDialog').on('show.bs.modal', function(){
        var $this = $(this);
        var $modal_dialog = $this.find('.modal-dialog');
        $this.css('display', 'block');
        $modal_dialog.css({'margin-top': Math.max(0, ($(window).height() - $modal_dialog.height()) / 2) });
    });

    //$("#itemAddDialog").modal({backdrop: false, keyboard: false});
    initTree();

    //此处是为了解决对话框再次打开数据任然还在的问题
    $('#itemAddDialog').on('hide.bs.modal', function () {
        document.getElementById("itemAddForm").reset();
    });
    $('#goalAddDialog').on('hide.bs.modal', function () {
        document.getElementById("itemAddForm").reset();
    });

    getItemList();

    initGoalDataGrid(1);
    initTaskDataGrid();


});


function initTree(){
    myDiagram =
        $go(go.Diagram, "myDiagramDiv",
            {
                "autoScale":go.Diagram.Uniform,
                "undoManager.isEnabled": true,
                layout: $go(go.TreeLayout, // specify a Diagram.layout that arranges trees
                    {angle: 0, layerSpacing: 35})
            });
    myDiagram.nodeTemplate =
        $go(go.Node, "Horizontal",
            {background: "#44CCFF"},

            $go(go.TextBlock, "请选择左侧要查看的类别",
                {margin: 10, stroke: "white", font: "bold 20px sans-serif", isMultiline: true,wrap: go.TextBlock.WrapFit,width:250},
                new go.Binding("text", "des"),
                new go.Binding("editable", "editable"),
                new go.Binding("key", "id")
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
            {editable: true, key: "1", name: "请点击选择左侧类别"}
        ];
    myDiagram.model = model;
    myDiagram.linkTemplate =
        $go(go.Link,
            {routing: go.Link.Orthogonal, corner: 6},
            $go(go.Shape, {strokeWidth: 1, stroke: "#055"}));

}

/**
 * 获取并刷新类别列表
 */
function getItemList() {
    var des = $("#itemDesSearch").val();
    console.info(des);
    $.ajax({
        type: "post",
        url: "/wbs/task/getItems",
        data: {"des": des},
        dataType: "json",
        success: function (result) {
            if (result.success) {
                console.info(result);
                var menuList = "";
                $.each(result.data, function (i, item) {
                    menuList += "<li class=\"list-group-item\">";
                    // menuList +=item.des;
                    menuList += i + "、<a  href='javascript:;' onclick='openItem(\"" + item.id + "\",\"" + item.des + "\")' ><span>" + item.des + "</span></a>";
                    menuList += "</li>";
                });
                $("#item_list_ul").html('');
                ;
                $("#item_list_ul").append(menuList);
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

/**
 * 保存类别
 */
function saveItem(){
    var des=$("#itemDes").val();
    $.ajax({
        type: "post",
        url: "/wbs/task/saveItem",
        data: {"des": des},
        dataType: "json",
        success: function (result) {
            if (result.success) {
                console.log("保存成功！");
                getItemList();
                $('#itemAddDialog').modal('hide');
            }
        }
    });
}
/**
 * 目标类别
 */
function saveGoal(){
    var goal={itemId:selectedItemId};
    goal.des=$("#goalDesForAdd").val();
    goal.itemId=globalItemData.selectedItemId;
    $.ajax({
        type: "post",
        url: "/wbs/task/saveGoal",
        data: goal,
        dataType: "json",
        success: function (result) {
            if (result.success) {
                console.log("目标保存成功！");
                openItem();
                $('#goalAddDialog').modal('hide');
            }
        }
    });
}

/**
 * 打开所选类别
 * @param id
 * @param des
 */
function openItem(id, des) {
    $("#taskInfo").show();
    $("#goalInfo").show();

    globalVm.data={};
    globalItemData.selectedItemId=id;
    globalItemData.selectedItemDes=des;
    globalItemData.hello="变了";
    globalVm.data=globalItemData;
    initGoalDataGrid(1);
    initTaskDataGrid();
    reloadGoalList();
    reloadTaskList();



    $.ajax({
        type: "post",
        url: "/wbs/task/getTaskTreeByItemId",
        data: {itemId:globalItemData.selectedItemId},
        dataType: "json",
        success: function (result) {
            if (result.success) {
                console.info(result.data);
                myDiagram.model.nodeDataArray=result.data;
                var nodeDataArray=[];
                var itemNode={editable: false, key: "-1",  des: globalItemData.selectedItemDes};
                nodeDataArray.push(itemNode);
                $.each(result.data, function (i, item) {
                    item.key=item.id;
                    //最上层任务
                    if(item.parent==-1){
                        item.parent=item.goalId*(-1);
                    }else if(item.parent==-2){//目标
                        item.parent=-1;
                        item.key=item.id*(-1);
                    }
                    nodeDataArray.push(item);
                    console.info(item);
                });
                myDiagram.model.nodeDataArray=nodeDataArray;
            }
        }
    });
}

/**
 * 初始化目标列表
 * @param itemId
 */
function initGoalDataGrid(itemId){
    $('#goalDg').datagrid({
        fitColumns:true,
        singleSelect:true,
        checkOnSelect:true,
        url:'/wbs/task/getGoals',
        queryParams:{itemId:-1},
        columns:[[
            {field:'id',title:'id',hidden:true},
            {field:'itemId',title:'itemId',hidden:true},
            {field:'des',title:'目标描述'},
            {field:'op',title:'操作',width:160,formatter:goalOpFormat}
        ]],
        loadFilter: function(result){
            return result.data;
        }
    });
}
/**
 * 初始化任务列表
 * @param itemId
 */
function initTaskDataGrid(){
    $('#taskDg').treegrid({
        fitColumns:true,
        singleSelect:true,
        checkOnSelect:true,
        idField:'id',
        treeField:'des',
        url:'/wbs/task/getTaskTreeListByItemId',
        queryParams:{itemId:-1},
        columns:[[
            {field:'id',title:'id',hidden:true},
            {field:'goalId',title:'goalId',hidden:true},
            {field:'parent',title:'parentId',hidden:true},
            {field:'des',title:'任务描述'},
            {field:'responsePerson',title:'责任人'},
            {field:'plan',title:'实施计划'},
            {field:'implementation',title:'执行人'},
            {field:'confirmer',title:'确认人'},
            {field:'op',title:'操作',width:160,formatter:taskOpFormat}
        ]],
        loadFilter: function(result){
            console.info(result.data);
            for(var i=0,length=result.data.length;i<length;i++){
                result.data[i]._parentId=result.data[i].parent;
            }
            console.info(result.data);
            return result.data;
        }
    });
}

function reloadGoalList(){
    $('#goalDg').datagrid('reload',{itemId:globalItemData.selectedItemId,test:1});
}
function reloadTaskList(){
    $('#taskDg').treegrid('reload',{itemId:globalItemData.selectedItemId});
}


function goalOpFormat(rowIndex, rowData){
    return '<a href="#" onclick="editGoal()">编辑</a>'
            +' <a href="#" onclick="deletGoal()">删除</a>'
            +' <a href="javascript:;" onclick="openTaskAddDialog(\'' +rowData.id+'\',\''+rowData.des+'\',-1,\'\')">分解任务</a>';
}
function taskOpFormat(rowIndex, rowData){
    return '<a href="#" onclick="editGoal()">编辑</a>'
            +' <a href="#" onclick="deletGoal()">删除</a>'
            +' <a href="javascript:;" onclick="openTaskAddDialog(\'' +rowData.goalId+'\',\''+rowData.des+'\',\''+rowData.id+'\',\''+rowData.des+'\')">分解任务</a>';
}

/**
 * 打开添加任务对话框，并赋值
 * @param goalId
 * @param goalDes
 */
function openTaskAddDialog(goalId,goalDes,parentId,parentDes){
    $('#taskAddDialog').modal('show');
    $("#itemDesIdForTaskAdd").val(globalItemData.selectedItemDes);
    $("#goalIdForTaskAdd").val(goalId);
    $("#parentDesAdd").val(parentDes);
    $("#goalDesForTaskAdd").val(goalDes);
    $("#parentAdd").val(parentId);
    if(parentId=="-1"){
        $("#parent_task").hide();
    }else{
        $("#parent_task").show();
    }
}

/**
 * 保存任务
 */
function saveTask(){
    var data={};
    data.goalId=$("#goalIdForTaskAdd").val();
    data.des=$("#taskDesForTaskAdd").val();
    data.responsePerson=$("#responsePersonAdd").val();
    data.plan=$("#planAdd").val();
    data.implementation=$("#implementationAdd").val();
    data.confirmer=$("#confirmerAdd").val();
    data.parent=$("#parentAdd").val();
    $.ajax({
        type: "post",
        url: "/wbs/task/saveTask",
        data: data,
        dataType: "json",
        success: function (result) {
            if (result.success) {
                console.log("任务保存成功！");
                openItem(globalItemData.selectedItemId,globalItemData.selectedItemDes);
                $('#taskAddDialog').modal('hide');
            }
        }
    });
}