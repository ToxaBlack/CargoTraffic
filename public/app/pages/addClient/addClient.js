define(['app/service/clientService', 'app/service/navService', 'app/service/barService', "knockout", "text!./addClient.html"],
    function (clientService, navService, bar, ko, addClientTemplate) {
        "use strict";

        function addClientViewModel() {
            bar.go(50);
            var self = this;
            self.company = ko.observable({});
            self.admin = ko.observable({});
            self.confirmPassword = ko.observable();
            self.add = function () {
                clientService.add(self.company(), self.admin(),
                    function (data) {
                        navService.mainPage();
                    },
                    function (data) {
                        switch (data.status) {
                            case 403:
                                navService.navigateTo("login");
                                break;
                            default:
                                navService.navigateTo("error");
                        }
                    });
            };


            bar.go(100);
            return self;
        }

        return {viewModel: addClientViewModel, template: addClientTemplate};
    });