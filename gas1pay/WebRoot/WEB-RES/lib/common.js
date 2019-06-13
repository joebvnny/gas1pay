history.pushState(null, null, document.URL);
window.addEventListener('popstate', function() {
    history.pushState(null, null, document.URL);
});

(function($) {
    $.fn.mask = function(label, delay) {
        $(this).each(function() {
            if(delay !== undefined && delay > 0) {
                var element = $(this);
                element.data("_mask_timeout", setTimeout(function() {
                    $.maskElement(element, label);
                }, delay));
            } else {
                $.maskElement($(this), label);
            }
        });
    };

    /**
     * Removes mask from the element(s). Accepts both single and multiple
     * selectors.
     */
    $.fn.unmask = function() {
        $(this).each(function() {
            $.unmaskElement($(this));
        });
    };

    /**
     * Checks if a single element is masked. Returns false if mask is delayed or
     * not displayed.
     */
    $.fn.isMasked = function() {
        return this.hasClass("masked");
    };

    $.maskElement = function(element, label) {

        // if this element has delayed mask scheduled then remove it and display
        // the new one
        if(element.data("_mask_timeout") !== undefined) {
            clearTimeout(element.data("_mask_timeout"));
            element.removeData("_mask_timeout");
        }

        if(element.isMasked()) {
            $.unmaskElement(element);
        }

        if(element.css("position") == "static") {
            element.addClass("masked-relative");
        }

        element.addClass("masked");

        var maskDiv = $('<div class="loadmask"></div>');

        // auto height fix for IE
        if(navigator.userAgent.toLowerCase().indexOf("msie") > -1) {
            maskDiv.height(element.height() + parseInt(element.css("padding-top")) + parseInt(element.css("padding-bottom")));
            maskDiv.width(element.width() + parseInt(element.css("padding-left")) + parseInt(element.css("padding-right")));
        }

        // fix for z-index bug with selects in IE6
        if(navigator.userAgent.toLowerCase().indexOf("msie 6") > -1) {
            element.find("select").addClass("masked-hidden");
        }

        element.append(maskDiv);

        if(label !== undefined) {
            var maskMsgDiv = $('<div class="loadmask-msg" style="display:none;"></div>');
            maskMsgDiv.append('<div>' + label + '</div>');
            element.append(maskMsgDiv);

            // calculate center position
            maskMsgDiv.css("top", Math.round(element.height() / 2
                    - (maskMsgDiv.height() - parseInt(maskMsgDiv.css("padding-top")) - parseInt(maskMsgDiv.css("padding-bottom"))) / 2)
                    + "px");
            maskMsgDiv.css("left", Math.round(element.width() / 2
                    - (maskMsgDiv.width() - parseInt(maskMsgDiv.css("padding-left")) - parseInt(maskMsgDiv.css("padding-right"))) / 2)
                    + "px");

            maskMsgDiv.show();
        }

    };

    $.unmaskElement = function(element) {
        // if this element has delayed mask scheduled then remove it
        if(element.data("_mask_timeout") !== undefined) {
            clearTimeout(element.data("_mask_timeout"));
            element.removeData("_mask_timeout");
        }

        element.find(".loadmask-msg,.loadmask").remove();
        element.removeClass("masked");
        element.removeClass("masked-relative");
        element.find("select").removeClass("masked-hidden");
    };

    $.fn.serializeObject = function() { // Prepping for JSON
        var o = {};
        var a = this.serializeArray();
        $.each(a, function() {
            if(o[this.name]) {
                if(!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    };
    $.ajaxSetup({
        headers : {
            dataType : 'json'
        },
        dataType : 'json',
        type : 'POST'
    });
    $.extend($.fn.validatebox.defaults.rules, {
        regexp : {
            validator : function(value, param) {
                var pattern = RegExp(param[0]);
                return pattern.test(value);
            },
            message : '{1}'
        }
    });

})(jQuery);

var myapp = myapp || {};
/**
 * datagrid 对象列的显示
 */
myapp.dgObjFmt = function(value, rec) {
    if(typeof this.field == 'string' && rec) {
        var v = rec;
        var props = this.field.split('.');
        for( var i = 0; i < props.length; i++) {
            v = v[props[i]];
            if(v == undefined) {
                break;
            }
        }
        return v == undefined ? '' : v;
    }
    return '';
};

/**
 * 默认一些form提交行为
 */
myapp.formOpts = function(opts) {
    var optSuccess = opts.success;
    var optOnSuccess = opts.onSubmit;
    var defaultOpts = {};
    var opts = $.extend(defaultOpts, opts);
    opts.success = function(data) {
        try {
            if(opts.maskEl) {
                $(opts.maskEl).unmask();
            }
            var resp = $.parseJSON(data);
            if(resp.code === 0) {
                if(opts.enableSuccMsg) {
                    // $.messager.alert('提示', '操作成功', 'info');
                }
            } else {
                if(!opts.disableFailMsg) {
                    $.messager.alert('错误', resp.message, 'error');
                }
            }
            if(optSuccess) {
                optSuccess.call(this, data, resp);
            }
        } catch(e) {
            if(!opts.disableFailMsg) {
                $.messager.alert('警告', data, 'warning');
            }
        }
    };
    opts.onSubmit = function() {
        var rtn = true;
        if(typeof optOnSuccess == 'function') {
            rtn = optOnSuccess.apply(this, arguments);
        }
        if(rtn === true && opts.maskEl) {
            $(opts.maskEl).mask('提交中 ... ');
        }
        return rtn;
    };
    return opts;
};

/**
 * 默认一些datagrid行为
 */
myapp.dgOpts = function(opts) {
    var defaultOpts = {
        striped : true,
        singleSelect : true,
        fitColumns : true,
        pagination : true,
        rownumbers : true,
        loadFilter : function(odata) {
            if(!odata.rows && odata.data) {
                odata.rows = odata.data;
            }
            if((typeof odata.message == 'string') && odata.code !== 0 && !opts.disableFailMsg) {
                $.messager.alert('错误', odata.message, 'error');
                odata.rows = [];
                odata.total = 0;
            }
            return odata;
        }
    };
    var opts = $.extend(defaultOpts, opts);
    return opts;
};

function getURLParameter(name) {
    return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search) || [, ""])[1].replace(/\+/g, '%20')) || null;
};
