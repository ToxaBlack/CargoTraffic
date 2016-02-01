
define(['app/service/navService', 'app/service/barService', "knockout", 'jquery',"text!./ttn.html"],
    function (navService, bar, ko, $,ttnTemplate) {
        "use strict";

        function ttnViewModel() {
            bar.go(50);
            var self = this;
            self.DialogVisible = ko.observable(false);
            self.closeDialog = function () {
                $('#warehouses-popup').modal("hide");
            };

            self.choose = function() {
                var context = ko.contextFor($("#warehouses")[0]);
                alert(context.$data.checkedWarehouses());
            };

            bar.go(100);
            return self;
        }

        return {viewModel: ttnViewModel, template: ttnTemplate};
    });