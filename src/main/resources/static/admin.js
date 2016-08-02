

// declare a new module called 'myApp', and make it require the `ng-admin` module as a dependency
var myApp = angular.module('homeauto', ['ng-admin']);
// declare a function to run when the module bootstraps (during the 'config' phase)
myApp.config(['NgAdminConfigurationProvider', function (nga) {
    // create an admin application
    var admin = nga.application('HomeAuto Admin')
    .baseApiUrl('http://192.168.0.10:8081/homeauto/'); // main API endpoint;
    
    var outlets = nga.entity('outlets');
    // set the fields of the user entity list view
    outlets.listView().fields([
        nga.field('id'),              
        nga.field('name').isDetailLink(true),
        nga.field('onCode'),
        nga.field('offCode'),
        nga.field('pulse'),
        nga.field('running', 'boolean') 
    ]);
    
    outlets.creationView().fields([
        nga.field('name')
        	.attributes({ placeholder: '3 chars min' })
        	.validation({ required: true, minlength: 3, maxlength: 100 }),
        nga.field('onCode', 'number')
        	.attributes({ placeholder: 'Must be a number between 4 and 10 digits' })
        	.validation({ required: true, minlength: 4, maxlength: 10 }),     
        nga.field('offCode', 'number')
        	.attributes({ placeholder: 'Must be a number between 4 and 10 digits' })
        	.validation({ required: true, minlength: 4, maxlength: 10 }),     
        nga.field('pulse', 'number')
        	.attributes({ placeholder: 'Must be a number between 2 and 5 digits' })
        	.validation({ required: true, minlength: 2, maxlength: 5 }),                   
    ]);
    
    outlets.editionView().fields(outlets.creationView().fields());
    // add the user entity to the admin application
    admin.addEntity(outlets);
    
    // attach the admin application to the DOM and execute it
    nga.configure(admin);
}]);