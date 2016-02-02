define(['app/service/authService', 'app/service/navService', 'app/service/accountService', './password', './account', "knockout", "text!./navbar.html"],
    function (authService, navService,  accountService, passwordViewModal, accountViewModal, ko, navbarTemplate) {
        "use strict";

        function navbarViewModel() {
            var self = this;

            self.passwordViewModal = passwordViewModal;
            self.accountViewModal = accountViewModal;

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

            self.showPasswordModal = function () {
                $('#passwordModal').modal();
            };

            self.showAccountModal = function () {
                accountService.get(
                    function (data) {
                        self.accountViewModal.account(data);
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
                $('#accountModal').modal();
            };


            return self;
        }


        return {viewModel: navbarViewModel, template: navbarTemplate};
    });
