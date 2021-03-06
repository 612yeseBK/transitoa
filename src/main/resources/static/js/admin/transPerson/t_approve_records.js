var tabs = document.getElementById('tab').getElementsByTagName('button');
for(var i=0; i<tabs.length; i++) {
    tabs[i].onclick = function () {
        var tabId = this.id;
        //若当前按钮已选中则点击不再触发事件
        if($(this).hasClass('button')) {return};
        AjaxTool.get('transper/transperspContent', {
                lx: this.id, bz: 'sp'},function (data) {
                if(data.success) {
                    var str = "";
                    var cons = data.content;
                    for(var i=0;i<cons.length;i++) {
                        str += "<tr>";
                        str += "<td>"+cons[i].job+"</td>";
                        str += "<td>"+cons[i].applyTime+"</td>";
                        str += "<td>"+cons[i].userName+"</td>";
                        switch (tabId) {
                            case "dsp":
                                str += "<td>待审批</td>";
                                str += "<td><div onclick='seeApprove(\""+cons[i].id +'\",\"'+ tabId+"\")'>查看</div>" +
                                    "<div onclick='pass(\"" + cons[i].id +"\")'>通过</div></td>";
                                break;
                            case "yth":
                                str += "<td>已退回</td>";
                                str += "<td><div onclick='seeApprove(\""+cons[i].id +'\",\"'+ tabId+"\")'>查看</div></td>";
                                break;
                            default:
                                str += "<td><div>"+cons[i].state+"</div></td>";
                                str += "<td><div onclick='seeApprove(\""+cons[i].id +'\",\"'+ tabId+"\")'>查看</div></td>";
                                break;
                        }
                        str += "</tr>";
                    }
                    var mTable = $('#d-approve-table').DataTable();
                    mTable.destroy();
                    $('#d-approve-tbody').html(str);
                    init();
                }
            }
        );
        $(this).removeClass("button-active").addClass("button");
        for(var j=0;j<tabs.length;j++) {
            if(tabs[j] != this) {
                $(tabs[j]).removeClass("button").addClass("button-active")
            }
        }
    }
}


function seeApprove(id,tabId) {
    console.log(id)
    AjaxTool.html('transper/getOneCheck',{id: id},function (html) {
        $('.portlet-body').html(html);
        switch (tabId) {
            case "ysp":
                $('.operation').attr('style','display:none');
                break;
            default:
                break;
        }
        $('#back').data('tabId',tabId);
    });
}

function pass(id) {
    AjaxTool.post('transper/commitCheck',{
        cljg: '通过', id:id},function (data) {
        alert(data.message);
        var table = $('#d-approve-table').DataTable();
        if(data.success) {
            table.rows('#'+id).remove().draw();
            // refreshDocument();
        }
    });
}

function refreshDocument(){
    var n = $('#发文审批').text();
    n--;
    if(n == 0){
        $('#发文审批').remove();
    }else{
        $('#发文审批').text(n);
    }
    var count = $('#发文管理').text();
    count--;
    if (count == 0){
        $('#发文管理').remove();
    }else{
        $('#发文管理').text(count);
    }
}

function init() {
    DatatableTool.initDatatable("d-approve-table", [{
        'width': '30%',
        'targets': 0
    },
        {
            'orderable' : false,
            'targets' : [ 4 ]
        }, {
            "searchable" : false,
            "targets" : [ 4 ]
        }], [ [ 1, "desc" ] ]);
}

jQuery(document).ready(function() {
    init();
});