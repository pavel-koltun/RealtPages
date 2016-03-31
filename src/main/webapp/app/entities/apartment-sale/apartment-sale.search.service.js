(function() {
    'use strict';

    angular
        .module('onlinerRealtPagesApp')
        .factory('ApartmentSaleSearch', ApartmentSaleSearch);

    ApartmentSaleSearch.$inject = ['$resource'];

    function ApartmentSaleSearch($resource) {
        var resourceUrl =  'api/_search/apartment-sales/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
