define(['app/service/accountService', 'app/service/navService', 'app/service/barService', "knockout", "text!./password.html"],
    function (accountService, navService, bar, ko, passwordTemplate) {
    "use strict";

    function passwordViewModel() {
        bar.go(50);
        var self = this;
        self.oldPassword = ko.observable();
        self.newPassword = ko.observable();
        self.confirmPassword = ko.observable();
        self.error = ko.observable();

        self.save = function () {
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

        bar.go(100);
        return self;
    }

    return {viewModel: passwordViewModel, template: passwordTemplate};
});