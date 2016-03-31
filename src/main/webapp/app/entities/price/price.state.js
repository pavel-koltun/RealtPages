(function() {
    'use strict';

    angular
        .module('onlinerRealtPagesApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('price', {
            parent: 'entity',
            url: '/price?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'onlinerRealtPagesApp.price.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/price/prices.html',
                    controller: 'PriceController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('price');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('price-detail', {
            parent: 'entity',
            url: '/price/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'onlinerRealtPagesApp.price.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/price/price-detail.html',
                    controller: 'PriceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('price');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Price', function($stateParams, Price) {
                    return Price.get({id : $stateParams.id});
                }]
            }
        })
        .state('price.new', {
            parent: 'price',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/price/price-dialog.html',
                    controller: 'PriceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                priceUsd: null,
                                priceRuble: null,
                                created: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('price', null, { reload: true });
                }, function() {
                    $state.go('price');
                });
            }]
        })
        .state('price.edit', {
            parent: 'price',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/price/price-dialog.html',
                    controller: 'PriceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Price', function(Price) {
                            return Price.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('price', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('price.delete', {
            parent: 'price',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/price/price-delete-dialog.html',
                    controller: 'PriceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Price', function(Price) {
                            return Price.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('price', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
