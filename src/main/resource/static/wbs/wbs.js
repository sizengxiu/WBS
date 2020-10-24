var globalURL = "localhost:8080";

var $go = go.GraphObject.make;
var myDiagram;



var globalVm;
var globalItemData={selectedItemDes:'' };



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
    initItemSeleteControl();
    //$("#itemAddDialog").modal({backdrop: false, keyboard: false});
    initTree();

    //此处是为了解决对话框再次打开数据任然还在的问题
    $('#itemAddDialog').on('hide.bs.modal', function () {
        document.getElementById("itemAddForm").reset();
    });
    $('#goalAddDialog').on('hide.bs.modal', function () {
        document.getElementById("goalAddForm").reset();
    });
    $('#taskAddDialog').on('hide.bs.modal', function () {
        document.getElementById("taskAddForm").reset();
    });


    initTaskDataGrid();


});

/**
 * 初始化目标下拉款框
 */
function initItemSeleteControl(){
    $('#item_select').combobox({
        url:'/wbs/task/getItems',
        valueField:'id',
        textField:'des',
        loadFilter: function(result){
            return result.data;
        },
        onSelect:function(record){
            openItem(record.id,record.des);
        }
    });
}


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

            $go(go.TextBlock, "请选择要查看的类别",
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
    globalVm.data=globalItemData;
    initTaskDataGrid();
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
 * 初始化任务列表
 * @param itemId
 */
function initTaskDataGrid(){
    $('#taskDg').treegrid({
        fitColumns:true,
        singleSelect:true,
        checkOnSelect:true,
        idField:'id',
        treeField:'typeName',
        url:'/wbs/task/getTaskTreeListByItemId',
        queryParams:{itemId:-1},
        columns:[[
            {field:'id',title:'id',hidden:true},
            {field:'goalId',title:'goalId',hidden:true},
            {field:'parent',title:'parentId',hidden:true},
            {field:'typeName',title:'分类',width:150},
            {field:'des',title:'描述',width:300},
            {field:'responsePerson',title:'责任人',width:60},
            {field:'plan',title:'实施计划',width:80},
            {field:'implementation',title:'执行人',width:60},
            {field:'confirmer',title:'确认人',width:60},
            {field:'op',title:'操作',width:160,formatter:taskOpFormat}
        ]],
        loadFilter: function(result){
            return result.data;
        }
    });
}


function reloadTaskList(){
    $('#taskDg').treegrid('reload',{itemId:globalItemData.selectedItemId});
}


/**
 * 格式化操作列
 * @param rowIndex
 * @param rowData
 * @returns {string}
 */
function taskOpFormat(rowIndex, rowData){
    //type=0 类别，1为目标，2为任务
    var data = JSON.stringify(rowData);
    if(rowData.type==1){
        return "<a href='#' onclick='openEditGoalDialog("+data+")'>编辑</a>"
            +" <a href='#' onclick='deletGoal(\"" +rowData.id+"\")'>删除</a>"
            +" <a href='javascript:;' onclick='openTaskAddDialog(\"" +rowData.id+"\",\""+rowData.des+"\",-1,\"\")'>分解任务</a>";
    }else if(rowData.type==2){
        return "<a href='#' onclick='openEditTaskDialog("+data+")'>编辑</a>"
            +" <a href='#' onclick='deletTask(\"" +rowData.id+"\")'>删除</a>"
            +" <a href='javascript:;' onclick='openTaskAddDialog(\"" +rowData.goalId+"\",\""+rowData.des+"\",\""+rowData.id+"\",\""+rowData.des+"\")'>分解任务</a>";
    }
    return "<a href='#' onclick='openEditItemDialog("+data+")'>编辑</a>"
        +" <a href='#' onclick='deletItem(\"" +rowData.id+"\")'>删除</a>"
        +" <a href='javascript:;' onclick='openGoalAddDialog(\"" +rowData.goalId+"\",\""+rowData.des+"\",\""+rowData.id+"\",\""+rowData.des+"\")'>新增目标</a>";


}


/**
 * 打开类别编辑对话框
 */
function openEditItemDialog(data){
    $('#itemUpdateDialog').modal('show');
    $('#itemIdUpdate').val(data.id);
    $('#itemDesUpdate').val(data.des);
}
/**
 * 打开新增目标对话框
 */
function openGoalAddDialog(){
    $('#goalAddDialog').modal('show');
}

/**
 * 打开目标编辑对话框
 */
function openEditGoalDialog(data){
    $('#goalUpdateDialog').modal('show');
    $('#goalIdUpdate').val(data.id);
    $('#goalDesForUpdate').val(data.des);
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
 * 打开任务编辑对话框
 * @param rowData
 */
function openEditTaskDialog(data){
    $('#taskUpdateDialog').modal('show');
    $("#goalIdForTaskUpdate").val(data.goalId);
    $("#taskDesForTaskUpdate").val(data.des);
    $("#responsePersonUpdate").val(data.responsePerson);
    $("#planUpdate").val(data.plan);
    $("#implementationUpdate").val(data.implementation);
    $("#confirmerUpdate").val(data.confirmer);
    $("#parentUpdate").val(data.parent);
    $("#taskIdUpdate").val(data.id);
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
            }else{
                layer.alert(result.message,{icon: 2});
            }
        }
    });
}

/**
 * 编辑任务
 */
function editTask(){
    var data={};
    data.des=$("#taskDesForTaskUpdate").val();
    data.responsePerson=$("#responsePersonUpdate").val();
    data.plan=$("#planUpdate").val();
    data.implementation=$("#implementationUpdate").val();
    data.confirmer=$("#confirmerUpdate").val();
    data.id=$("#taskIdUpdate").val();
    $.ajax({
        type: "post",
        url: "/wbs/task/updateTask",
        data: data,
        dataType: "json",
        success: function (result) {
            if (result.success) {
                console.log("任务保存成功！");
                openItem(globalItemData.selectedItemId,globalItemData.selectedItemDes);
                $('#taskUpdateDialog').modal('hide');
            }else{
                layer.alert(result.message,{icon: 2});
            }
        }
    });
}


/**
 * 删除任务
 * @param taskId
 */
function deletTask(taskId){

    layer.confirm('删除该任务会连同其子任务一起删除，确定要删除吗?', {icon: 3, title:'删除确认'}, function(index){
        //do something
        $.ajax({
            type: "post",
            url: "/wbs/task/deleteTaskTreeByTaskId",
            data: {taskId:taskId},
            dataType: "json",
            success: function (result) {
                if (result.success) {
                    layer.msg("共计删除了"+result.data+"个任务！");
                    openItem(globalItemData.selectedItemId,globalItemData.selectedItemDes);
                }else{
                    layer.alert("任务删除失败！");
                }
            }
        });
        layer.close(index);
    });
}
/**
 * 保存目标
 */
function saveGoal(){
    var goal={itemId:globalItemData.selectedItemId};
    goal.des=$("#goalDesForAdd").val();
    goal.itemId=globalItemData.selectedItemId;
    if(goal.des==''){
        layer.alert("目标描述不能为空！",{icon: 2});
        return;
    }

    $.ajax({
        type: "post",
        url: "/wbs/task/saveGoal",
        data: goal,
        dataType: "json",
        success: function (result) {
            if (result.success) {
                console.log("目标保存成功！");
                openItem(globalItemData.selectedItemId,globalItemData.selectedItemDes);
                $('#goalAddDialog').modal('hide');
            }
        }
    });
}

/**
 * 编辑目标
 */
function editGoal(){
    var goal={};
    goal.des=$("#goalDesForUpdate").val();
    goal.id=$("#goalIdUpdate").val();

    $.ajax({
        type: "post",
        url: "/wbs/task/updateGoal",
        data: goal,
        dataType: "json",
        success: function (result) {
            if (result.success) {
                openItem(globalItemData.selectedItemId,globalItemData.selectedItemDes);
                layer.msg("目标更新成功！");
                $('#goalUpdateDialog').modal('hide');
            }else{
                layer.alert(result.message,{icon: 2});
            }
        }
    });
}
/**
 * 删除目标及其子任务
 * @param goalId
 */
function deletGoal(goalId){

    layer.confirm('删除该目标会连同其子任务一起删除，确定要删除吗?', {icon: 3, title:'删除确认'}, function(index){
        //do something
        $.ajax({
            type: "post",
            url: "/wbs/task/deleteGoalByGoalId",
            data: {goalId:goalId},
            dataType: "json",
            success: function (result) {
                if (result.success) {
                    layer.msg("删除了1个目标和"+(result.data-1)+"个任务！");
                    openItem(globalItemData.selectedItemId,globalItemData.selectedItemDes);
                }else{
                    layer.alert("任务删除失败！");
                }
            }
        });
        layer.close(index);
    });
}

/**
 * 保存类别
 */
function editItem(){
    var item={};
    item.id=$('#itemIdUpdate').val();
    item.des=$('#itemDesUpdate').val();
    if(item.des==''){
        layer.alert("类别描述不能为空！",{icon: 2});
        return;
    }
    $.ajax({
        type: "post",
        url: "/wbs/task/updateItem",
        data: item,
        dataType: "json",
        success: function (result) {
            if (result.success) {
                $('#item_select').combobox("reload");
                $('#itemUpdateDialog').modal('hide');
                layer.msg("类别修改成功！");
            }else{
                layer.alert(result.message);
            }
        }
    });
}

/**
 * 保存类别
 */
function saveItem(){
    var des=$("#itemDes").val();
    if(des==''){
        layer.alert("类别描述不能为空！",{icon: 2});
        return;
    }
    $.ajax({
        type: "post",
        url: "/wbs/task/saveItem",
        data: {"des": des},
        dataType: "json",
        success: function (result) {
            if (result.success) {
                console.log("保存成功！");
                $('#item_select').combobox("reload");
                $('#itemAddDialog').modal('hide');
            }
        }
    });
}



/**
 * 删除目标及其子任务
 * @param goalId
 */
function deletItem(itemId){

    layer.confirm('删除该类别会连同其下的目标和任务一起删除，确定要删除吗?', {icon: 3, title:'删除确认'}, function(index){
        $.ajax({
            type: "post",
            url: "/wbs/task/deleteItemByItemId",
            data: {itemId:globalItemData.selectedItemId},
            dataType: "json",
            success: function (result) {
                if (result.success) {
                    layer.msg("类别删除成功！");
                    location.reload();
                }else{
                    layer.alert("类别删除失败！");
                }
            }
        });
        layer.close(index);
    });
}