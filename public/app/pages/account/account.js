define(['app/service/accountService', 'app/service/navService', 'app/service/barService', "knockout", "text!./account.html"],
    function (accountService, navService, bar, ko, accountTemplate) {
        "use strict";

        function accountViewModel() {
            bar.go(50);
            var self = this;
            self.account = ko.observable({});
            self.save = function () {
                accountService.updateAccount(self.account(),
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

            accountService.get(
                function (data) {
                    self.account(data);
                },
                function (data) {
                    switch (data.status) {
                        case 403:
                            navService.navigateTo("login");
                            break;
                        default:
                            navService.navigateTo("error");
                    }
                },
                function () {
                    bar.go(100);
                });


            return self;
        }

        return {viewModel: accountViewModel, template: accountTemplate};
    });