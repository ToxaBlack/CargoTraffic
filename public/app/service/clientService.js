define(["app/utils/utils"],
    function (utils) {
        "use strict";
        function ClientService() {


            var list = function (id, numberOfClients, ascOrder, done, error, always) {
                utils.send(
                    "api/clients",
                    "GET",
                    {"id": id, "clients": numberOfClients, "ascOrder": ascOrder},
                    done,
                    error,
                    always
                );
            };

            var add = function (client, admin, done, error, always) {
                utils.send(
                    "api/client",
                    "POST",
                    JSON.stringify({client: {name: client.name(), date: client.date}, admin: {surname: admin.surname(), email: admin.email()}}),
                    done,
                    error,
                    always
                );
            };

            var lock = function (clients, done, error, always) {
                utils.send(
                    "api/clients/lock",
                    "PUT",
                    JSON.stringify(clients),
                    done,
                    error,
                    always
                );
            };

            var unlock = function (clients, done, error, always) {
                utils.send(
                    "api/clients/unlock",
                    "PUT",
                    JSON.stringify(clients),
                    done,
                    error,
                    always
                );
            };

            return {
                list: list,
                add: add,
                lock: lock,
                unlock: unlock
            }
        }

        return new ClientService();
    });