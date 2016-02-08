define(['app/service/navService', 'app/service/barService', "knockout", 'jquery',"text!./packingLists.html"],

    function (navService, bar, ko, $,ttnTemplate) {
        "use strict";

        function TTN(id, createDate) {
            var self = this;
            self.id = id;
            self.createDate = createDate;
        }

        function ttnViewModel() {
            bar.go(50);
            var self = this;

            self.ttns = ko.observableArray([
                new TTN(34, new Date()),
                new TTN(45, new Date()),
                new TTN(46, new Date()),
                new TTN(49, new Date())
            ]);

            self.toTTN = function () {
                 navService.navigateTo('checkPackingList');
            };
            bar.go(100);
            return self;
        }

        return {viewModel: ttnViewModel, template: ttnTemplate};
    });