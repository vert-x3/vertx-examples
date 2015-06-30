angular.module('CrudApp', []).config(['$routeProvider', function ($routeProvider) {
    $routeProvider.
        when('/', {templateUrl: '/tpl/lists.html', controller: ListCtrl}).
        when('/add-user', {templateUrl: '/tpl/add-new.html', controller: AddCtrl}).
        when('/edit/:id', {templateUrl: '/tpl/edit.html', controller: EditCtrl}).
        otherwise({redirectTo: '/'});
}]);

function ListCtrl($scope, $http) {
    $http.get('/api/users').success(function (data) {
        $scope.users = data;
    });
}

function AddCtrl($scope, $http, $location) {
    $scope.master = {};
    $scope.activePath = null;

    $scope.add_new = function (user, AddNewForm) {

        $http.post('/api/users', user).success(function () {
            $scope.reset();
            $scope.activePath = $location.path('/');
        });

        $scope.reset = function () {
            $scope.user = angular.copy($scope.master);
        };

        $scope.reset();

    };
}

function EditCtrl($scope, $http, $location, $routeParams) {
    var id = $routeParams.id;
    $scope.activePath = null;

    $http.get('/api/users/' + id).success(function (data) {
        $scope.user = data;
    });

    $scope.update = function (user) {
        $http.put('/api/users/' + id, user).success(function (data) {
            $scope.user = data;
            $scope.activePath = $location.path('/');
        });
    };

    $scope.delete = function (user) {
        var deleteUser = confirm('Are you absolutely sure you want to delete '+ user.id +' ?');
        if (deleteUser) {
            $http.delete('/api/users/' + id, user);
            $scope.activePath = $location.path('/');
        }
    };
}