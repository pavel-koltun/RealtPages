 'use strict';

angular.module('onlinerrealtpagesApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-onlinerrealtpagesApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-onlinerrealtpagesApp-params')});
                }
                return response;
            }
        };
    });
