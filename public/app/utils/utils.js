define(['jquery','crossroads','history'],
    function ($, crossroads) {
        "use strict";

        function send(url, method, data, doneFunk, failFunk, alwaysFunk) {
            $.ajax({
                url: url,
                method: method,
                data: data,
                contentType: 'application/json'
            }).done(doneFunk)
                .fail(failFunk)
                .always(alwaysFunk);
        }

        function goTo(page) {
            History.pushState({
                urlPath: '/' + page
            }, $(this).text(), '/' + page);
        }

        return {
            send: send,
            goTo: goTo
        };
    });
