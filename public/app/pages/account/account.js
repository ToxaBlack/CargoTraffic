define(['app/service/accountService', 'app/service/navService', "knockout", "text!./account.html"],
    function (accountService, navService, ko, accountTemplate) {
    "use strict";

    function accountViewModel() {
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
            });

        return self;
    }

    return {viewModel: accountViewModel, template: accountTemplate};
});