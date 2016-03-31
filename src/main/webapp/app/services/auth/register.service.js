(function () {
    'use strict';

    angular
        .module('onlinerRealtPagesApp')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();
