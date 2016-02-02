define(['app/service/accountService', 'app/service/navService', "knockout"],
    function (accountService, navService, ko) {
        "use strict";

        function AccountViewModel() {
            var self = this;
            self.account = ko.observable({});
            self.updateAccount = function () {
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
            return self;
        }

        return new AccountViewModel();
    });