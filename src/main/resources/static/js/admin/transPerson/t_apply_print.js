$('#print').click(function() {
    var formContents = document.getElementsByClassName('form-content');
    // for(var i=0; i<formContents.length; i++ ) {
    //     if(/^\s*$/.test(formContents[i].value)) {
    //         alert('请填写完整信息!');
    //         return false;
    //     }
    // }

    // bdhtml=window.document.body.innerHTML;
    // sprnstr="<!--startprint-->";
    // eprnstr="<!--endprint-->";
    // prnhtml=bdhtml.substr(bdhtml.indexOf(sprnstr)+17);
    // prnhtml=prnhtml.substring(0,prnhtml.indexOf(eprnstr));
    // window.document.body.innerHTML=prnhtml;
    // if(validator.form()){
    //     // window.print();
        var id=$('#id').html();
    var birthday=$('#birthday').val();
    //     var gwbh=$('#gwbh').val();
    //     var yfrq=$('#yfrq').val();
    //     window.location = "/wjsc?xgdm=TransPer&xgid=" +id;
    window.location = "transper/transPerPrint?xgdm=TransPer&xgid=" +id+"&birthday="+birthday;
        // AjaxTool.post('document/bhht',{id:id, gwbh:gwbh ,yfrq:yfrq}, function (data) {
        //     if (data.success){
        //         window.location = "/wjsc?xgdm=jtgw&xgid=" +id;
        //     }
        //     alert(data.message);
        // })
    // }

});




$('.date-picker').datetimepicker({
    format:'yyyy/mm/dd',
    language: 'zh-CN',
    weekStart: 1,
    todayBtn:  1,
    autoclose: 1,
    todayHighlight: 1,
    startView: 2,
    minView: 2,
    forceParse: 0
});

var validator = $('#d_apply_print_form').validate({
    errorElement: 'span', //default input error message container
    errorClass: 'error-tips', // default input error message class
    // rules: {
    //     gwbh: {
    //         required: true,
    //         maxlength: 50
    //     },
    //
    //     yfrq :{
    //         required: true,
    //         dateISO: true
    //     }
    //
    // },
    // messages: {
    //     gwbh: {
    //         maxlength: "公文编号超过50个字"
    //     },
    //     yfrq: {
    //         dateISO: "日期格式不正确"
    //     },
    // }
});

$('#back').click(function () {
    var tabId = ($('#back').data('tabId'));
    AjaxTool.getHtml('transper/getApplyTables',function (html) {
        $('.page-content').html(html);
        // $('#'+tabId).trigger('click');
    });
});
