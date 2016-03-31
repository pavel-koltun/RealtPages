(function() {
    'use strict';

    angular
        .module('onlinerRealtPagesApp')
        .factory('ApartmentSearch', ApartmentSearch);

    ApartmentSearch.$inject = ['$resource'];

    function ApartmentSearch($resource) {
        var resourceUrl =  'api/_search/apartments/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
