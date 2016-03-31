(function() {
    'use strict';
    angular
        .module('onlinerRealtPagesApp')
        .factory('Price', Price);

    Price.$inject = ['$resource', 'DateUtils'];

    function Price ($resource, DateUtils) {
        var resourceUrl =  'api/prices/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.created = DateUtils.convertDateTimeFromServer(data.created);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
