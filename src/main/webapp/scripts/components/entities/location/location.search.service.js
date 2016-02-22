'use strict';

angular.module('onlinerrealtpagesApp')
    .factory('LocationSearch', function ($resource) {
        return $resource('api/_search/locations/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
