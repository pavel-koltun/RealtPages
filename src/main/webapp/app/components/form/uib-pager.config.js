(function () {
    'use strict';

    angular
        .module('onlinerRealtPagesApp')
        .config(pagerConfig);

    function pagerConfig(uibPagerConfig, paginationConstants) {
        uibPagerConfig.itemsPerPage = paginationConstants.itemsPerPage;
        uibPagerConfig.previousText = '«';
        uibPagerConfig.nextText = '»';
    }
})();
