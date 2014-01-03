var ContentController = {
	collection : null,
	
	init : function() {
		this.collection = new App.Collection.ContentCollection();
		this.collection.fetch({
			success : function(result) {
				new App.View.ImageList( { model : result.models });
			}
		});
	},
	
	search : function(searchText) {
		var results = this.collection.find(function(model) {
			return model.get('name').indexOf(searchText) >= 0;
		});
		new App.View.ImageList({ model : results });
	}
};