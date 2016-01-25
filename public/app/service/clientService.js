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

            var add = function (client, done, error, always) {
                utils.send(
                    "api/client",
                    "POST",
                    JSON.stringify(client),
                    done,
                    error,
                    always
                );
            };

            return {
                list: list,
                add: add
            }
        }

        return new ClientService();
    });