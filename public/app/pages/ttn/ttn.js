
define(['app/service/navService', 'app/service/barService', "knockout", 'jquery',"text!./ttn.html",'app/pages/warehouses/warehouses'],
    function (navService, bar, ko, $,ttnTemplate, warehouses) {
        "use strict";

        function ttnViewModel() {
            bar.go(50);
            var self = this;
            self.DialogVisible = ko.observable(false);
            self.closeDialog = function () {
                $('#warehouses').modal("hide");
            };

            self.choose = function() {
                alert(warehouses.viewModel().checkedWarehouses);
            };

            bar.go(100);
            return self;
        }

        return {viewModel: ttnViewModel, template: ttnTemplate};
    });