(function() {
    'use strict';
    angular
        .module('onlinerRealtPagesApp')
        .factory('ApartmentSale', ApartmentSale);

    ApartmentSale.$inject = ['$resource'];

    function ApartmentSale ($resource) {
        var resourceUrl =  'api/apartment-sales/:id';

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
