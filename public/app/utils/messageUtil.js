/*
 This's util for making various messages. By default main.scala.html includes message's placeholder. So, in additional
 you have possibility to create message by adding message's placeholder in necessary place(f.e. in modal dialog) and
 don't forget to add dependency of this module.
 */
define(['jquery'],
    function ($) {
        "use strict";

        function createWarningMesssage(message) {
            $('.message_placeholder').html('<div class="alert alert-warning fade in">'+
               '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>'+
             '<strong>'+ message +'</strong></div>');
        }

        function createSuccessMesssage(message) {
            $('.message_placeholder').html('<div class="alert alert-success fade in">'+
                '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>'+
                '<strong>'+ message +'</strong></div>');
        }

        function createInfoMesssage(message) {
            $('.message_placeholder').html('<div class="alert alert-info fade in">'+
                '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>'+
                '<strong>'+ message +'</strong></div>');
        }

        function createErrorMesssage(message) {
            $('.message_placeholder').html('<div class="alert alert-danger fade in">'+
                '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>'+
                '<strong>'+ message +'</strong></div>');
        }

        function deleteMessage() {
            $('.message_placeholder').empty();
        }

        return {
            createWarningMessage: createWarningMesssage,
            createSuccessMessage: createSuccessMesssage,
            createInfoMesssage: createInfoMesssage,
            createErrorMesssage: createErrorMesssage,
            deleteMessage: deleteMessage
        };
    });