(function() {
    'use strict';

    angular
        .module('onlinerRealtPagesApp')
        .factory('ApartmentRentSearch', ApartmentRentSearch);

    ApartmentRentSearch.$inject = ['$resource'];

    function ApartmentRentSearch($resource) {
        var resourceUrl =  'api/_search/apartment-rents/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
