(function() {
    'use strict';
    angular
        .module('onlinerRealtPagesApp')
        .factory('ApartmentRent', ApartmentRent);

    ApartmentRent.$inject = ['$resource'];

    function ApartmentRent ($resource) {
        var resourceUrl =  'api/apartment-rents/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
