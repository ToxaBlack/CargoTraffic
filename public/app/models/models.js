define(['knockout'],
    function (ko) {
        "use strict";

        function Goods() {
            var self = this;
            self.name = ko.observable();
            self.quantity = ko.observable();
            self.unit = ko.observable();
            self.storage = ko.observable();
            self.price = ko.observable();
        }

        return {
            Goods: Goods
        };
    });
