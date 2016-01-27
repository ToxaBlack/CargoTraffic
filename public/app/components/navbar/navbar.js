define(['app/service/authService', 'app/service/navService', 'app/service/accountService', "knockout", "text!./navbar.html"],
    function (authService, navService, accountService, ko, navbarTemplate) {
        "use strict";

        function navbarViewModel() {
            var self = this;

            self.mainPage = function () {
                navService.mainPage();
            };

            self.logout = function () {
                authService.logout(
                    function () {
                        var context = ko.contextFor($("body")[0]);
                        context.$data.roles([]);
                        navService.navigateTo("login");
                    })
            };


            self.oldPassword = ko.observable();
            self.newPassword = ko.observable();
            self.confirmPassword = ko.observable();
            self.error = ko.observable();

            self.updatePassword = function () {
                if (self.newPassword() === self.confirmPassword())
                    accountService.updatePassword(self.oldPassword(), self.newPassword(),
                        function (data) {
                            navService.mainPage();
                        },
                        function (data) {
                            switch (data.status) {
                                case 400:
                                    self.error(data.responseText);
                                    break;
                                case 403:
                                    navService.navigateTo("login");
                                    break;
                                default:
                                    navService.navigateTo("error");
                            }
                        });
                else self.error("Passwords don't match");
            };

            self.account = ko.observable({});
            self.update = function () {
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



        return {viewModel: navbarViewModel, template: navbarTemplate};
    });
