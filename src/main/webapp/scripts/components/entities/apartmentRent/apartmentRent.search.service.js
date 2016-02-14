'use strict';

angular.module('onlinerrealtpagesApp')
    .factory('ApartmentRentSearch', function ($resource) {
        return $resource('api/_search/apartmentRents/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
