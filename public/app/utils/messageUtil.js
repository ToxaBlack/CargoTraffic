/*
 This's util for making various messages. By default main.scala.html includes message's placeholder. So, in additional
 you have possibility to create message by adding message's placeholder in necessary place(f.e. in modal dialog) and
 don't forget to add dependency of this module.
 */
define(['jquery'],
    function ($) {
        "use strict";

        function createWarningMessage(message) {
            var placeholder = $('.message_placeholder');
            placeholder.fadeIn();
            placeholder.html('<div class="alert alert-warning fade in">'+
               '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>'+
             '<strong>'+ message +'</strong></div>');
            placeholder.fadeOut(5000);
        }

        function createSuccessMessage(message) {
            var placeholder = $('.message_placeholder');
            placeholder.fadeIn();
            placeholder.html('<div class="alert alert-success fade in">'+
                '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>'+
                '<strong>'+ message +'</strong></div>');
            placeholder.fadeOut(5000);
        }

        function createInfoMessage(message) {
            var placeholder = $('.message_placeholder');
            placeholder.fadeIn();
            placeholder.html('<div class="alert alert-info fade in">'+
                '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>'+
                '<strong>'+ message +'</strong></div>');
            placeholder.fadeOut(5000);
        }

        function createErrorMessage(message) {
            var placeholder = $('.message_placeholder');
            placeholder.fadeIn();
            placeholder.html('<div class="alert alert-danger fade in">'+
                '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>'+
                '<strong>'+ message +'</strong></div>');
            placeholder.fadeOut(5000);
        }

        //Deprecated method
        function deleteMessage() {
            $('.message_placeholder').empty();
        }

        return {
            createWarningMessage: createWarningMessage,
            createSuccessMessage: createSuccessMessage,
            createInfoMessage: createInfoMessage,
            createErrorMessage: createErrorMessage,
            deleteMessage: deleteMessage
        };
    });