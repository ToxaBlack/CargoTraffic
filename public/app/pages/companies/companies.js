define(['app/utils/utils', "knockout", "jquery", "text!./companies.html"],
    function (utils, ko, $, listTemplate) {
    "use strict";

    function companiesViewModel() {
        var self = this;
        self.companies = ko.observableArray([]);
        self.checkedCompanies = ko.observableArray([]);
        self.hasNextPage = ko.observable(false);
        self.hasPreviousPage = ko.observable(false);
        self.allChecked = false;
        self.COMPANIES_PER_PAGE = 3;

        utils.send("api/companies", "GET", {"id": "1", "companies": "4", "ascOrder": "true"},
            function (data) {
                if (data.length === self.COMPANIES_PER_PAGE + 1) {
                    self.hasNextPage(true);
                    data.pop();
                } else {
                    self.hasNextPage(false);
                }
                self.hasPreviousPage(false);
                self.companies(data);
            },
            function (data) {
                utils.goTo("error");
            });

        self.nextPage = function () {
            if (!self.hasNextPage()) return;
            var nextPageFirstCompanyId = self.companies()[self.companies().length - 1].id + 1;
            utils.send("api/companies", "GET",
                {"id": nextPageFirstCompanyId, "companies": "4", "ascOrder": "true"},
                function (data) {
                    if (data.length === self.COMPANIES_PER_PAGE + 1) {
                        self.hasNextPage(true);
                        data.pop();
                    } else {
                        self.hasNextPage(false);
                    }
                    self.hasPreviousPage(true);
                    self.companies(data);
                },
                function (data) {
                    utils.goTo("error");
                });

        };

        self.previousPage = function () {
            if (!self.hasPreviousPage()) return;
            utils.send("api/companies", "GET",
                {"id": self.companies()[0].id, "companies": "4", "ascOrder": "false"},
                function (data) {

                    if (data.length === self.COMPANIES_PER_PAGE + 1) {
                        self.hasPreviousPage(true);
                        data.shift();
                    } else {
                        self.hasPreviousPage(false);
                    }
                    self.hasNextPage(true);
                    self.companies(data);
                },
                function () {
                    utils.goTo("error");
                });

        };

        self.checkedCompany = function () {
            console.log("checkedCompany called");
            if ($.inArray(this, self.checkedCompanies()) === -1)
                self.checkedCompanies.push(this);
            else
                self.checkedCompanies.remove(this);
        };

        self.checkAll = function () {
            console.log("in checkAll");
            /*if (self.allChecked) {
             //self.allChecked = false;
             self.checkedCompanies().filter(function (obj) {
             return $.inArray(obj, self.companies()) === -1;
             })
             }
             else {
             //self.allChecked = true;
             self.checkedCompanies(self.companies());
             //$.extend(self.checkedCompanies(), self.companies());
             }
             console.log(self.checkedCompanies().length);
             console.log(self.companies().length);
             if (self.allChecked) self.allChecked = false; else self.allChecked = true;*/
        };

        /*utils.ajax("api/companies", "GET",  {"id": "1", "companies": "4", "ascOrder": "true"},
         function (reply) {
         switch (reply.status) {
         case "SUCCESS":
         if (reply.data.length === self.COMPANIES_PER_PAGE + 1) {
         self.hasNextPage(true);
         reply.data.pop();
         } else {
         self.hasNextPage(false);
         }
         self.hasPreviousPage(false);
         self.companies(reply.data);
         break;
         case "ERROR":
         break;
         case "UNAUTHENTICATED":
         utils.goTo("login");
         break;
         default:
         break;
         }
         });*/

        return self;
    }

    /*companiesViewModel.prototype.checkedCompany = function(company) {
     console.log("checkedCompany called");
     if ($.inArray(company, self.checkedCompanies()) === -1)
     self.checkedCompanies.push(company.id);
     else
     self.checkedCompanies.remove(company.id);
     };*/

    /*companiesViewModel.prototype.hasNextPage = ko.observable(false);

     companiesViewModel.prototype.nextPage = function() {
     if (!companiesViewModel.prototype.hasNextPage()) return;
     var nextPageFirstCompanyId = self.companies()[self.companies().length - 1].id + 1;
     utils.ajax("api/companies", "GET",
     {"id": nextPageFirstCompanyId, "companies": "4", "ascOrder": "true" },
     function (reply) {
     switch (reply.status) {
     case "SUCCESS":
     if (reply.data.length === self.COMPANIES_PER_PAGE + 1) {
     self.hasNextPage(true);
     reply.data.pop();
     } else {
     self.hasNextPage(false);
     }
     self.hasPreviousPage(true);
     self.companies(reply.data);
     break;
     case "ERROR":
     break;
     case "UNAUTHENTICATED":
     utils.goTo("login");
     break;
     default:
     break;
     }
     });
     };*/

    return {viewModel: companiesViewModel, template: listTemplate};
});

