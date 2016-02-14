'use strict';

angular.module('onlinerrealtpagesApp')
    .factory('ApartmentRent', function ($resource, DateUtils) {
        return $resource('api/apartmentRents/:id', {}, {
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
    });
