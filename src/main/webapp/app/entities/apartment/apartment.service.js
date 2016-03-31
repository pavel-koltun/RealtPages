(function() {
    'use strict';
    angular
        .module('onlinerRealtPagesApp')
        .factory('Apartment', Apartment);

    Apartment.$inject = ['$resource', 'DateUtils'];

    function Apartment ($resource, DateUtils) {
        var resourceUrl =  'api/apartments/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.created = DateUtils.convertDateTimeFromServer(data.created);
                    data.updated = DateUtils.convertDateTimeFromServer(data.updated);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
