define(['app/service/navService', 'app/service/barService', "knockout", 'jquery',"text!./packingLists.html"],

    function (navService, bar, ko, $,packingListTemplate) {
        "use strict";

        function PackingList(id, createDate) {
            var self = this;
            self.id = id;
            self.createDate = createDate;
        }

        function packingListViewModel() {
            bar.go(50);
            var self = this;

            self.packingLists = ko.observableArray([
                new PackingList(34, new Date()),
                new PackingList(45, new Date()),
                new PackingList(46, new Date()),
                new PackingList(49, new Date())
            ]);
            self.toPackingList = function () {
                navService.navigateTo("checkPackingLists");
            };
            bar.go(100);
            return self;
        }

        return {viewModel: packingListViewModel, template: packingListTemplate};
    });