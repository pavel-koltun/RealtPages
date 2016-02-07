'use strict';

angular.module('onlinerrealtpagesApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


