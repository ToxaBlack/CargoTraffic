define(['app/utils/utils', "knockout", "text!./password.html"], function (utils, ko, passwordTemplate) {
    "use strict";

    function passwordViewModel() {
        var self = this;
        self.oldPassword = ko.observable();
        self.password = ko.observable();
        self.confirmPassword = ko.observable();
        self.error = ko.observable();

        self.save = function () {
            if (self.password() === self.confirmPassword())
                utils.ajax("api/account/password", "PUT", JSON.stringify({
                        oldPassword: self.oldPassword(),
                        password: self.password
                    }
                    ),
                    function (data) {
                        utils.goTo("account");
                    },
                    function (data) {
                        utils.goTo("error");
                    });
            else self.error("Passwords don't match");
        };

        return self;
    }

    return {viewModel: passwordViewModel, template: passwordTemplate};
});