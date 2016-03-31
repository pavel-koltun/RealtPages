(function() {
    'use strict';

    angular
        .module('onlinerRealtPagesApp')
        .factory('PriceSearch', PriceSearch);

    PriceSearch.$inject = ['$resource'];

    function PriceSearch($resource) {
        var resourceUrl =  'api/_search/prices/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
