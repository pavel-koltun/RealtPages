'use strict';

angular.module('onlinerrealtpagesApp')
    .factory('ApartmentSaleSearch', function ($resource) {
        return $resource('api/_search/apartmentSales/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
