(function() {
    'use strict';

    angular
        .module('onlinerRealtPagesApp')
        .factory('notificationInterceptor', notificationInterceptor);

    notificationInterceptor.$inject = ['$q', 'AlertService'];

    function notificationInterceptor ($q, AlertService) {
        var service = {
            response: response
        };

        return service;

        function response (response) {
            var alertKey = response.headers('X-onlinerRealtPagesApp-alert');
            if (angular.isString(alertKey)) {
                AlertService.success(alertKey, { param : response.headers('X-onlinerRealtPagesApp-params')});
            }
            return response;
        }
    }
})();
