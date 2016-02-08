define(['app/service/accountService', 'app/service/navService', "knockout"],
    function (accountService, navService, ko) {
        "use strict";

        var validator;

        function AccountViewModel() {
            var self = this;
            self.account = ko.observable({});


            self.updateAccount = function () {
                if (validate()) {
                    $('#accountModal').modal('hide');
                    validator.resetForm();
                    accountService.updateAccount(self.account(),
                        function (data) {
                            navService.mainPage();
                        },
                        function (data) {
                            navService.catchError(data);
                        });
                }
            };

            self.cancel = function() {
                $('#accountModal').modal('hide');
                if (validator) validator.resetForm();
            };
        }

        function validate() {
            validator = $('#accountForm').validate({
                rules: {
                    birthDate: {
                        customDate: true,
                    },
                    email: {
                        email: true
                    }
                },
                messages: {
                    surname: {
                        required: "Please enter surname."
                    }
                }
            });

            return validator.form();
        }

        return new AccountViewModel();
    });