App.Model.Content = Backbone.Model.extend({
	urlRoot : '/boosetube-service/rest/content',
	idAttribute: '_id',
	defaults : {
		name : '',
		description : ''
	}
});

App.Collection.ContentCollection = Backbone.Collection.extend({
	model : App.Model.Content,
	url : '/boosetube-service/rest/content'
});