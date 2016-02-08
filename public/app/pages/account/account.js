define(['app/service/accountService', 'app/service/navService', 'app/service/barService', "knockout", "text!./account.html"],
    function (accountService, navService, bar, ko, accountTemplate) {
        "use strict";

        function accountViewModel() {
            bar.go(50);
            var self = this;
            self.account = ko.observable({});
            self.update = function () {
                accountService.updateAccount(self.account(),
                    function (data) {
                        navService.mainPage();
                    },
                    function (data) {
                        navService.catchError(data);
                    });
            };

            accountService.get(
                function (data) {
                    self.account(data);
                },
                function (data) {
                    navService.catchError(data);
                },
                function () {
                    bar.go(100);
                });


            return self;
        }

        return {viewModel: accountViewModel, template: accountTemplate};
    });